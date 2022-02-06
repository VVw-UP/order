package com.ocbc.oms.app.managers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.oms.app.api.AmendOrderRequest;
import com.ocbc.oms.app.api.AmendOrderResponse;
import com.ocbc.oms.app.api.BatchCancelRequest;
import com.ocbc.oms.app.api.BulKeDitResponse;
import com.ocbc.oms.app.api.CFSOrderEnquiryRequest;
import com.ocbc.oms.app.api.CFSOrderEnquiryResponse;
import com.ocbc.oms.app.api.CFSOrderExpiry;
import com.ocbc.oms.app.api.CFSOrderExpiryResponse;
import com.ocbc.oms.app.api.CFSOrderRequest;
import com.ocbc.oms.app.api.CFSOrderResponse;
import com.ocbc.oms.app.api.CancelOrder;
import com.ocbc.oms.app.api.OrderResponse;
import com.ocbc.oms.app.api.SearchParam;
import com.ocbc.oms.app.api.SearchResponse;
import com.ocbc.oms.app.config.cache.GlobalCache;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.config.kafka.OrderKafkaSender;
import com.ocbc.oms.app.consts.DataConstant;
import com.ocbc.oms.app.dbservice.LimitTrade;
import com.ocbc.oms.app.dbservice.TExpiryTimeService;
import com.ocbc.oms.app.error.api.*;
import com.ocbc.oms.app.error.application.InvalidInputMapException;
import com.ocbc.oms.app.model.CFSOrder;
import com.ocbc.oms.app.model.PageEntity;
import com.ocbc.oms.app.model.TCfsProperties;
import com.ocbc.oms.app.model.TCurrency;
import com.ocbc.oms.app.model.TCurrencyPair;
import com.ocbc.oms.app.model.TOrder;
import com.ocbc.oms.app.model.TTrade;
import com.ocbc.oms.app.model.TUiViewLabel;
import com.ocbc.oms.app.model.dto.TradeDateDto;
import com.ocbc.oms.app.repository.TCurrencyMapper;
import com.ocbc.oms.app.repository.TCurrencyPairMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ocbc.oms.app.consts.CommonConstants.DIRECTION_BUY;
import static com.ocbc.oms.app.consts.CommonConstants.DIRECTION_SELL;

@Component
@Slf4j
public class DbOMSManager {
    private final ClientUserChannelCounterpartyEntityManager clientUserChannelCounterpartyManager;
    private final CurrencyManager currencyManager;
    private final DirectionWatchTypeManager directionWatchTypeManager;
    private final OrderProductTypeManager orderProductTypeManager;
    private final TradeManager tradeManager;
    private final CfsManager cfsManager;
    private final TimeManager timeManager;

    private final TExpiryTimeService expiryTimeService;

    @Autowired
    private OrderKafkaSender orderKafkaSender;

    @Autowired
    private LimitTrade limitTrade;

    @Autowired
    private GlobalCache globalCache;

    @Autowired
    private TCurrencyPairMapper tCurrencyPairMapper;

    @Autowired
    private TCurrencyMapper tCurrencyMapper;

    public DbOMSManager(ClientUserChannelCounterpartyEntityManager clientUserChannelCounterpartyManager,
                        CurrencyManager currencyManager,
                        DirectionWatchTypeManager directionWatchTypeManager,
                        OrderProductTypeManager orderProductTypeManager,
                        TradeManager tradeManager,
                        CfsManager cfsManager,
                        TimeManager timeManager,
                        TExpiryTimeService expiryTimeService) {
        this.clientUserChannelCounterpartyManager = clientUserChannelCounterpartyManager;
        this.currencyManager = currencyManager;
        this.directionWatchTypeManager = directionWatchTypeManager;
        this.orderProductTypeManager = orderProductTypeManager;
        this.tradeManager = tradeManager;
        this.cfsManager = cfsManager;
        this.timeManager = timeManager;
        this.expiryTimeService = expiryTimeService;
    }

    public OrderResponse createCFSOrder(CFSOrderRequest orderRequest, String user) {
        log.debug("Processing cfsOrderRequest:{}", orderRequest);
        //校验过期时间
        orderProductTypeManager.validExpiryTime(orderRequest.getOrderType(), orderRequest.getOrderDetails().getExpireTimestamp());
        TOrder order = new TOrder();
        TTrade trade = new TTrade();
        TCfsProperties properties = new TCfsProperties();
        processCFSProductType(orderRequest, order);
        processOrderType(orderRequest, order);
        processUser(user, order, trade);
        processChannel(order);
        processCounterparty(order);
        processClient(orderRequest, order);
        processLongCustomerName(orderRequest, trade);
        addOrderStatusAndUUID(order);
        order.setCfsRefNumber(orderRequest.getCfsRefNumber());
        int state = orderProductTypeManager.addOrder(order);
        if (state == 1) {
            List<TOrder> savedInDB = orderProductTypeManager.getOrderByUniqId(order.getUniqId());
            order = savedInDB.get(savedInDB.size() - 1);
        }
        trade.setCcyPairId(currencyManager.getCurrencyPairFromMap(orderRequest.getOrderDetails().getCcyPair()));
        processOrderIdAndTradeStatus(order, trade, properties);
        processWatchTypeAndTimeInForceAndTimezone(trade);
        processCurrencyAndCurrencyPair(orderRequest, trade);
        processDirection(orderRequest);
        trade.setDirectionId(directionWatchTypeManager.getIdByCode(orderRequest.getOrderDetails().getDirection()));
        processPrice(orderRequest.getOrderDetails().getPrice(), orderRequest.getOrderType(), orderRequest.getOrderDetails().getDirection(),
                orderRequest.getOrderDetails().getCcyPair(), orderRequest.getOrderDetails().getDealtAmount(),trade.getDealtCcyId(), trade, properties);
        processCustomerSeg(orderRequest, properties);
        processCfsProperties(orderRequest, properties);
        trade.setExpireTimestamp(orderRequest.getOrderDetails().getExpireTimestamp());
        Integer dealtCcyId = trade.getDealtCcyIdOrig() == null ? trade.getDealtCcyId() : trade.getDealtCcyIdOrig();
        processAmt(orderRequest.getOrderDetails().getDealtAmount(), orderRequest.getOrderDetails().getContraAmount(), orderRequest.getOrderType(),
                orderRequest.getOrderDetails().getCcyPair(), dealtCcyId, trade);
        // tradeDate/valueDate calculate
        processNoFlayZone(orderRequest.getOrderDetails().getCcyPair(),orderRequest.getOrderType(),
                orderRequest.getOrderDetails().getPrice(),orderRequest.getOrderDetails().getDirection());
        TradeDateDto tradeDateDto = tradeManager.getTradeDateAndValueDate(orderRequest.getOrderDetails().getCcyPair());
        trade.setTradeDate(tradeDateDto.getTradeDate());
        trade.setSettlementDate(tradeDateDto.getValueDate());
        state = tradeManager.addTrade(trade);
        //Add to order cache
        if (trade.getTradeStatusId() == 4) {
            limitTrade.noticeSocketByOrderId(order.getId(), DataConstant.ADD);
            globalCache.getOrderTimeoutCache().put(trade.getId(), trade.getExpireTimestamp());
            TradeCache.putActiveTrade(order.getOrderTypeId(), orderRequest.getOrderDetails().getCcyPair(), trade);
        }
        if (state != 1) {
            log.debug("Trade not saved");
        }
        state = cfsManager.addCfsProperties(properties);
        if (state != 1) {
            log.debug("CfsProperties not saved");
        }
        log.debug("order:{}", order);
        log.debug("trade:{}", trade);
        log.debug("properties:{}", properties);
        // send to topic
        orderKafkaSender.sendOrderMsg(order.getId(), "CREATE");
        return OrderResponse.builder()
            .orderId(order.getId().intValue())
            .cifNumber(properties.getCifNumber())
            .build();
    }

    private void processNoFlayZone(String ccyPair,String orderType,BigDecimal price,String direction) {
        if ("LIMIT".equalsIgnoreCase(orderType) || "CALl".equalsIgnoreCase(orderType)) {
            if (currencyManager.calNoFlyZone(ccyPair,orderType,price,direction)){
                throw new NOFlyZoneException(APIErrorConstant.NOFlyZoneExceptionMessage,APIErrorConstant.NOFlyZoneExceptionCode);
            }
        }
    }

    public Integer getFilled() {
        return TradeStatus.getTradeStatusId(TradeStatus.FILLED);
    }

    private void processCFSProductType(CFSOrderRequest orderRequest, TOrder order) {
        if (StringUtils.isBlank(orderRequest.getProductType())) {
            throw new MissingProductTypeException(APIErrorConstant.MissingProductTypeExceptionMessage, APIErrorConstant.MissingProductTypeExceptionCode);
        }
        if (!"SPOT".equalsIgnoreCase(orderRequest.getProductType())) {
            throw new InvalidProductTypeException(APIErrorConstant.InvalidProductTypeExceptionMessage, APIErrorConstant.InvalidProductTypeExceptionCode);
        }
        order.setProductTypeId(orderProductTypeManager.getProductTypeIdFromMap(orderRequest.getProductType().toUpperCase()));
    }

    private void processLongCustomerName(CFSOrderRequest orderRequest, TTrade trade) {
        if (!"LIMIT".equalsIgnoreCase(orderRequest.getOrderType())) {
            return;
        }
        if (StringUtils.isBlank(orderRequest.getOrderDetails().getLongCustomerName())) {
            throw new MissingLongCustomerNameException(APIErrorConstant.MissingLongCustomerNameExceptionMessage, APIErrorConstant.MissingLongCustomerNameExceptionCode);
        }
        if (orderRequest.getOrderDetails().getLongCustomerName().length() > 200) {
            throw new LongCustomerNameLengthException(APIErrorConstant.LongCustomerNameLengthExceptionMessage, APIErrorConstant.LongCustomerNameLengthExceptionCode);
        }
        trade.setLongCustomerName(orderRequest.getOrderDetails().getLongCustomerName());
    }

    private void processOrderType(CFSOrderRequest orderRequest, TOrder order) {
        if (StringUtils.isBlank(orderRequest.getOrderType())) {
            throw new MissingOrderTypeException(APIErrorConstant.MissingOrderTypeExceptionMessage, APIErrorConstant.MissingOrderTypeExceptionCode);
        }
        try {
            order.setOrderTypeId(orderProductTypeManager.getOrderTypeId(orderRequest.getOrderType().toUpperCase()));
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new InvalidOrderTypeException(APIErrorConstant.InvalidOrderTypeExceptionMessage, APIErrorConstant.InvalidOrderTypeExceptionCode);
        }
    }

    private void processUser(String user, TOrder order, TTrade trade) {
        if (StringUtils.isBlank(user)) {
            throw new MissingUserException(APIErrorConstant.MissingUserExceptionMessage, APIErrorConstant.MissingUserExceptionCode);
        }
        try {
            LocalDateTime createdTime = LocalDateTime.now();
            order.setCreateBy(clientUserChannelCounterpartyManager.getUserIdFromMap(user));
            order.setCreateTimestamp(createdTime);
            order.setLastModifyBy(clientUserChannelCounterpartyManager.getUserIdFromMap(user));
            order.setLastModifyTimestamp(createdTime);
            order.setOnBehalfOf(clientUserChannelCounterpartyManager.getUserIdFromMap(user));
            trade.setCreateBy(clientUserChannelCounterpartyManager.getUserIdFromMap(user));
            trade.setCreateTimestamp(createdTime);
            trade.setLastModifyBy(clientUserChannelCounterpartyManager.getUserIdFromMap(user));
            trade.setLastModifyTimestamp(createdTime);
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new InvalidUserException(APIErrorConstant.InvalidUserExceptionMessage, APIErrorConstant.InvalidUserExceptionCode);
        }
    }

    private void processChannel(TOrder order) {
        try {
            order.setChannelId(clientUserChannelCounterpartyManager.getChannelIdFromMap(order.getCreateBy()));
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new MissingUserChannelConfigurationException(APIErrorConstant.MissingUserChannelConfigurationExceptionMessage, APIErrorConstant.MissingUserChannelConfigurationExceptionCode);
        }
    }

    private void processCounterparty(TOrder order) {
        try {
            order.setCounterpartyId(clientUserChannelCounterpartyManager.getCounterpartyFromMap(order.getCreateBy()));
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new MissingCounterpartyConfigurationException(APIErrorConstant.MissingCounterpartyConfigurationExceptionMessage, APIErrorConstant.MissingCounterpartyConfigurationExceptionCode);
        }
    }

    private void processClient(CFSOrderRequest orderRequest, TOrder order) {
        if (StringUtils.isBlank(orderRequest.getClient())) {
            throw new MissingClientException(APIErrorConstant.MissingClientConfigurationExceptionMessage, APIErrorConstant.MissingClientConfigurationExceptionCode);
        }
        try {
            order.setClientId(clientUserChannelCounterpartyManager.getClientIdFromMap(orderRequest.getClient().toUpperCase()));
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new InvalidClientException(APIErrorConstant.InvalidClientConfigurationExceptionMessage, APIErrorConstant.InvalidClientConfigurationExceptionCode);
        }
    }

    private void addOrderStatusAndUUID(TOrder order) {
        order.setOrderStatusId(OrderStatus.getOrderStatusId(OrderStatus.ACTIVE));
        order.setUniqId(UUID.randomUUID().toString());
    }

    private void processOrderIdAndTradeStatus(TOrder order, TTrade trade, TCfsProperties properties) {
        trade.setOrderId(order.getId());
        trade.setTradeStatusId(TradeStatus.getTradeStatusId(TradeStatus.ACTIVE));
        properties.setOrderId(order.getId());
    }

    private void processWatchTypeAndTimeInForceAndTimezone(TTrade trade) {
        trade.setWatchTypeId(WatchType.getWatchTypeId(WatchType.SYSTEM));
        trade.setTimeInForceId(TimeInForce.getTimeInForceId(TimeInForce.GoodToDuration));
        trade.setExpireTimezoneId(Timezone.getTimezoneId(Timezone.Singapore));
    }

    private void processDirection(CFSOrderRequest orderRequest) {
        if ("LIMIT".equalsIgnoreCase(orderRequest.getOrderType())) {
            if (StringUtils.isBlank(orderRequest.getOrderDetails().getDirection())) {
                throw new MissingDirectionException(APIErrorConstant.MissingDirectionExceptionMessage, APIErrorConstant.MissingDirectionExceptionCode);
            }
        }
    }

    private void processCurrencyAndCurrencyPair(CFSOrderRequest orderRequest, TTrade trade) {
        try {
            trade.setDealtCcyId(currencyManager.getCurrencyFromMap(orderRequest.getOrderDetails().getDealtccy().toUpperCase()));
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new InvalidCurrencyException(APIErrorConstant.InvalidCurrencyExceptionMessage, APIErrorConstant.InvalidCurrencyExceptionCode);
        }

        if ("LIMIT".equalsIgnoreCase(orderRequest.getOrderType())) {
            if (StringUtils.isBlank(orderRequest.getOrderDetails().getDealtccy())) {
                throw new MissingCurrencyException(APIErrorConstant.MissingCurrencyExceptionMessage, APIErrorConstant.MissingCurrencyExceptionCode);
            }

            TCurrencyPair currencyPair = currencyManager.currencyPairById.get(trade.getCcyPairId());
            boolean dealccyMatchBaseccy = trade.getDealtCcyId().equals(currencyPair.getBaseCcyId());
            boolean dealccyMatchTermccy = trade.getDealtCcyId().equals(currencyPair.getTermCcyId());
            if (!dealccyMatchBaseccy && !dealccyMatchTermccy) {
                throw new InvalidCurrencyException(APIErrorConstant.InvalidCurrencyCurrencyPairExceptionMessage, APIErrorConstant.InvalidCurrencyCurrencyPairExceptionCode);
            }
        }
        if (StringUtils.isBlank(orderRequest.getOrderDetails().getCcyPair())) {
            throw new MissingCurrencyPairException(APIErrorConstant.MissingCurrencyPairExceptionMessage, APIErrorConstant.MissingCurrencyPairExceptionCode);
        }
        try {
            trade.setCcyPairId(currencyManager.getCurrencyPairFromMap(orderRequest.getOrderDetails().getCcyPair().toUpperCase()));
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new InvalidCurrencyPairException(APIErrorConstant.InvalidCurrencyPairExceptionMessage, APIErrorConstant.InvalidCurrencyPairExceptionCode);
        }
    }

    private void processCustomerSeg(CFSOrderRequest orderRequest, TCfsProperties tCfsProperties) {
        if (StringUtils.isBlank(orderRequest.getCustomerSeg())) {
            throw new MissingCustomerSegException(APIErrorConstant.MissingCustomerSegExceptionMessage, APIErrorConstant.MissingCustomerSegExceptionCode);
        }
        try {
            tCfsProperties.setCustomerSegId(cfsManager.getCustomerSegmentIdFromMap(orderRequest.getCustomerSeg().toUpperCase()));
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new InvalidCustomerSegException(APIErrorConstant.InvalidCustomerSegExceptionMessage, APIErrorConstant.InvalidCustomerSegExceptionCode);
        }
    }

    /**
     * 订单price处理
     * @param price
     * @param orderType
     * @param direction
     * @param ccyPair
     * @param dealtAmt
     * @param dealtCcyId
     * @param trade
     * @param tCfsProperties
     */
    private void processPrice(BigDecimal price, String orderType, String direction, String ccyPair, BigDecimal dealtAmt,Integer dealtCcyId, TTrade trade, TCfsProperties tCfsProperties) {
        if (price == null) {
            throw new MissingPriceException(APIErrorConstant.MissingPriceExceptionMessage, APIErrorConstant.MissingPriceExceptionCode);
        }
        trade.setPrice(price);
        trade.setWatchPrice(price);
        if (tCfsProperties != null) {
            tCfsProperties.setAllInPrice(trade.getPrice());
        }
        // call单不计算watchPrice
        if (!"LIMIT".equalsIgnoreCase(orderType)) {
            return;
        }
        trade.setPriceOrig(price);
        trade.setDirectionIdOrig(directionWatchTypeManager.getIdByCode(direction));
        if (trade.getDirectionId() == null) {
            trade.setDirectionId(trade.getDirectionIdOrig());
        }
        if (trade.getDealtCcyId() == null) {
            trade.setDealtCcyId(dealtCcyId);
        }
        trade.setDealtCcyIdOrig(trade.getDealtCcyId());
        // Need to set up to get the correct price to watch -> Limit
        TCurrencyPair currencyPair = currencyManager.currencyPairById.get(currencyManager.getCurrencyPairFromMap(ccyPair));
        TCurrency tCurrency1 = currencyManager.getCurrencyById(currencyPair.getBaseCcyId());
        TCurrency tCurrency2 = currencyManager.getCurrencyById(currencyPair.getTermCcyId());
        Integer minorUnits = tCurrency1.getMinorUnits() < tCurrency2.getMinorUnits() ? tCurrency1.getMinorUnits() : tCurrency2.getMinorUnits();
        if (trade.getDealtCcyId().equals(currencyPair.getTermCcyId())) {
            // change trade side
            Integer side = tradeManager.exchangeTradeSide(direction);
            String code = directionWatchTypeManager.getCodeById(side);
            trade.setDirectionId(side);
            // change dealtCcy to baseCcy
            trade.setDealtCcyId(currencyPair.getBaseCcyId());
            tradeManager.reCalcPrice(price, trade, code, minorUnits);
            trade.setPrice(trade.getWatchPrice());
        } else {
            switch (direction) {
                case DIRECTION_BUY:
                    trade.setWatchPrice(price.setScale(minorUnits, BigDecimal.ROUND_DOWN));
                    break;
                case DIRECTION_SELL:
                    trade.setWatchPrice(price.setScale(minorUnits, BigDecimal.ROUND_UP));
                    break;
                default:
                    trade.setWatchPrice(trade.getPrice());
            }
        }
        // calculate limit price
        trade.setWatchPrice(tradeManager.reCalculateLimitPrice(minorUnits, ccyPair, dealtAmt, trade.getWatchPrice(), directionWatchTypeManager.getCodeById(trade.getDirectionId())));
    }

    private void processCfsProperties(CFSOrderRequest orderRequest, TCfsProperties tCfsProperties) {
        if (StringUtils.isBlank(orderRequest.getCifNumber())) {
            throw new MissingCifNumberException(APIErrorConstant.MissingCifNumberExceptionMessage, APIErrorConstant.MissingCifNumberExceptionCode);
        }
        if (StringUtils.isBlank(orderRequest.getProductCode())) {
            throw new MissingProductCodeException(APIErrorConstant.MissingProductCodeExceptionMessage, APIErrorConstant.MissingProductCodeExceptionCode);
        }
        if (StringUtils.isBlank(orderRequest.getChannelCode())) {
            throw new MissingChannelCodeException(APIErrorConstant.MissingChannelCodeExceptionMessage, APIErrorConstant.MissingChannelCodeExceptionCode);
        }
        tCfsProperties.setCifNumber(orderRequest.getCifNumber());
        tCfsProperties.setProductCode(orderRequest.getProductCode());
        tCfsProperties.setChannelCode(orderRequest.getChannelCode());
    }

    public CFSOrderEnquiryResponse processCFSEnquiry(String user, CFSOrderEnquiryRequest cfsOrderEnquiryRequest) {
        processUserForEnquiry(user);
        checkSearchParams(cfsOrderEnquiryRequest);
        return CFSOrderEnquiryResponse.builder().searchResultList(processSearchParams(cfsOrderEnquiryRequest)).build();
    }

    public CFSOrderEnquiryResponse exChangeOrig(List<SearchResponse> list) {
        return CFSOrderEnquiryResponse.builder().searchResultList(processOrigArgs(list)).build();
    }

    public void processAmt(BigDecimal dealtAmt, BigDecimal contraAmt, String orderType, String ccyPair, Integer dealtCcyId, TTrade trade) {
        trade.setDealtAmt(dealtAmt);
        trade.setContraAmt(contraAmt);
        if (!"LIMIT".equalsIgnoreCase(orderType)) {
            return;
        }

        QueryWrapper<TCurrencyPair> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ccy_pair", ccyPair);
        TCurrencyPair currencyPair = tCurrencyPairMapper.selectOne(queryWrapper);
        if (dealtCcyId.equals(currencyPair.getTermCcyId())) {
            trade.setDealtAmt(contraAmt);
            trade.setContraAmt(dealtAmt);
        } else {
            trade.setDealtAmt(dealtAmt);
            trade.setContraAmt(contraAmt);
        }
    }


    public CancelOrder cancelCFSOrder(String user, Long orderId) {
        //添加用户校验
        processUserForEnquiry(user);
        //校验入参
        if (orderId == null) {
            throw new MissOrderIdException(APIErrorConstant.MissOrderIdExceptionMessage, APIErrorConstant.MissOrderIdExceptionCode);
        }
        //查询trade
        List<TTrade> tradeResults = tradeManager.getTradeByOrderId(orderId);
        if (tradeResults.isEmpty()) {
            throw new MissOrderIdException(APIErrorConstant.InvalidOrderIdExceptionMessage, APIErrorConstant.InvalidOrderIdExceptionCode);
        }
        //将订单的状态改为cancelled
        TTrade tTrade = tradeResults.get(0);
        //校验是否处于no flay zone
        String ccyPair = currencyManager.getCurrencyPairFromMap(tTrade.getCcyPairId()).getCcyPair();
        List<TOrder> order = orderProductTypeManager.getOrderByOrderId(orderId);
        String orderType = orderProductTypeManager.getOrderTypeCodeById(order.get(0).getOrderTypeId());
        String codeById = directionWatchTypeManager.getCodeById(tTrade.getDirectionId());
        processNoFlayZone(ccyPair,orderType,tTrade.getPrice(),codeById);
        validStatus(tTrade.getTradeStatusId());
        tTrade.setTradeStatusId(TradeStatus.getTradeStatusId(TradeStatus.CANCELLED));
        tradeManager.updateTrade(tTrade);
        //清除缓存数据
        Map<Long, LocalDateTime> orderTimeoutCache = globalCache.getOrderTimeoutCache();
        limitTrade.noticeSocketByOrderId(tTrade.getOrderId(), DataConstant.UPDATE);
        orderTimeoutCache.remove(tTrade.getId());
        TradeCache.removeTrade(tTrade.getId());
        orderKafkaSender.sendOrderMsg(tTrade.getOrderId(), "CANCEL");
        return CancelOrder.builder().orderId(orderId).result("Order canceled success").build();
    }

    public AmendOrderResponse amendCFSSpotOrder(String user, AmendOrderRequest amendOrderRequest) {
        //添加用户校验
        processUserForEnquiry(user);
        //查询trade
        List<TTrade> tradeResults = tradeManager.getTradeByOrderId(amendOrderRequest.getOrderId());
        if (tradeResults == null || tradeResults.isEmpty()) {
            throw new MissOrderIdException(APIErrorConstant.InvalidOrderIdExceptionMessage, APIErrorConstant.InvalidOrderIdExceptionCode);
        }
        TTrade tTrade1 = tradeResults.get(0);
        validStatus(tTrade1.getTradeStatusId());
        //查询order
        List<TOrder> orderResults = orderProductTypeManager.getOrderByOrderId(amendOrderRequest.getOrderId());
        if (orderResults == null || orderResults.isEmpty()) {
            throw new MissOrderIdException(APIErrorConstant.InvalidOrderIdExceptionMessage, APIErrorConstant.InvalidOrderIdExceptionCode);
        }
        //校验no fly zone
        String ccyPair = currencyManager.getCurrencyPairFromMap(tTrade1.getCcyPairId()).getCcyPair();
        String orderType = orderProductTypeManager.getOrderTypeCodeById(orderResults.get(0).getOrderTypeId());
        String codeById = directionWatchTypeManager.getCodeById(tTrade1.getDirectionId());
        processNoFlayZone(ccyPair,orderType,amendOrderRequest.getPrice(),codeById);
        //校验过期时间（setting中设置的过期时间 > 页面的过期时间）
        orderProductTypeManager.validExpiryTime(orderProductTypeManager.getOrderTypeCodeById(orderResults.get(0).getOrderTypeId()), amendOrderRequest.getExpireTimestamp());
        //更新订单
        TTrade tTrade = new TTrade();
        BeanUtils.copyProperties(amendOrderRequest, tTrade);

        if (amendOrderRequest.getDealAmt() != null || amendOrderRequest.getPrice() != null) {
            TCurrencyPair currencyPairFromMap = currencyManager.getCurrencyPairFromMap(tTrade1.getCcyPairId());
            BigDecimal dealAmt = amendOrderRequest.getDealAmt() == null ? tTrade1.getDealtAmt() : amendOrderRequest.getDealAmt();
            BigDecimal contraAmt = amendOrderRequest.getContraAmt() == null ? tTrade1.getContraAmt() : amendOrderRequest.getContraAmt();
            BigDecimal price = amendOrderRequest.getPrice() == null ? tTrade1.getPrice() : amendOrderRequest.getPrice();
            Integer dealtCcyId = tTrade1.getDealtCcyIdOrig() == null ? tTrade1.getDealtCcyId() : tTrade1.getDealtCcyIdOrig();
            Integer directionId = tTrade1.getDirectionIdOrig() == null ? tTrade1.getDirectionId() : tTrade1.getDirectionIdOrig();
            processPrice(price, orderType, directionWatchTypeManager.getCodeById(directionId),
                    currencyPairFromMap.getCcyPair(), amendOrderRequest.getDealAmt(), dealtCcyId, tTrade, null);
            processAmt(dealAmt, contraAmt, orderType, currencyPairFromMap.getCcyPair(), dealtCcyId, tTrade);
        }


        tTrade.setId(tTrade1.getId());
        tTrade.setLastModifyTimestamp(LocalDateTime.now());
        tTrade.setLastModifyBy(clientUserChannelCounterpartyManager.getUserIdFromMap(user));
        tTrade.setExpireTimestamp(tTrade1.getExpireTimestamp());
        tTrade.setDirectionIdOrig(null);
        tTrade.setDirectionId(null);
        tTrade.setDealtCcyId(null);
        tTrade.setDealtCcyIdOrig(null);
        tradeManager.updateTrade(tTrade);
        //更新订单状态缓存数据
        if (tTrade.getExpireTimestamp() != null) {
            Map<Long, LocalDateTime> orderTimeoutCache = globalCache.getOrderTimeoutCache();
            limitTrade.noticeSocketByOrderId(tTrade.getOrderId(), DataConstant.UPDATE);
            orderTimeoutCache.put(tTrade.getId(), tTrade.getExpireTimestamp());
        }
        TradeCache.putActiveTrade(tTrade);
        Integer tradeStatusId = tradeResults.get(0).getTradeStatusId();
        String code = tradeManager.tradeStatusById.get(tradeStatusId.longValue()).getName();
        return AmendOrderResponse.builder().orderId(amendOrderRequest.getOrderId()).status(code).build();
    }

    public CFSOrderExpiryResponse expiryCFSSpotOrder(String user) {
        processUserForEnquiry(user);
        //获得所有订单类型对应的过期时间
        List<CFSOrderExpiry> expiryList = new ArrayList<>();
        expiryTimeService.findExpiryTimes().forEach(e -> {
            CFSOrderExpiry expiry = new CFSOrderExpiry(e.getOrderTypeName().toUpperCase(), e.getDays(), e.getHours(), e.getMinutes());
            expiryList.add(expiry);
        });
        return CFSOrderExpiryResponse.builder().results(expiryList).build();
    }

    public CFSOrderResponse queryCFSSpotOrder(String user, PageEntity pageEntity) {
        processUserForEnquiry(user);
        //获得分页起始行号
        int firstIndex = (pageEntity.getPageNum()-1) * pageEntity.getPageSize() +1;
        //获得所有的order
        List<TOrder> tOrders = orderProductTypeManager.queryOrders(pageEntity);
        //获得所有的trade
        List<TTrade> tTrades = tradeManager.queryTrades();
        Map<Long, TTrade> tTradeMap = tTrades.stream().collect(Collectors.toMap(TTrade::getOrderId, v -> v, (v1, v2) -> v1));
        //获得所有的cfsProperties
        List<TCfsProperties> tCfsPropertiesList = cfsManager.getAllCfsProperties();
        Map<Long, TCfsProperties> tCfsPropertiesMap = tCfsPropertiesList.stream().collect(Collectors.toMap(TCfsProperties::getOrderId, v -> v, (v1, v2) -> v1));
        //根据order_id获得所有的trade
        List<CFSOrder> cfsOrderList = new ArrayList<>();
        for (TOrder tOrder : tOrders) {
            CFSOrder cfsOrder = new CFSOrder();
            TTrade tTrade = tTradeMap.get(tOrder.getId());
            TCfsProperties tCfsProperties = tCfsPropertiesMap.get(tOrder.getId());
            if (tCfsProperties != null) {
                tOrder.setCfsRefNumber(tCfsProperties.getCifNumber());
            }
            if (tTrade != null) {
                cfsOrder.setRowNum(firstIndex++);
                BeanUtils.copyProperties(tOrder, cfsOrder);
                BeanUtils.copyProperties(tTrade, cfsOrder);
                //添加productTypeCode
                cfsOrder.setProductTypeCode(orderProductTypeManager.getProductTypeCodeFromId(tOrder.getProductTypeId()));
                //添加OrderStatusCode
                cfsOrder.setOrderStatusCode(orderProductTypeManager.getOrderStatusCodeById(tOrder.getOrderStatusId()));
                //添加OrderTypeCode
                cfsOrder.setOrderTypeCode(orderProductTypeManager.getOrderTypeCodeById(tOrder.getOrderTypeId()));
                //添加ccyPair
                cfsOrder.setCcyPair(currencyManager.getCurrencyPairFromMap(tTrade.getCcyPairId()).getCcyPair());
                //添加tradeStatusCode
                cfsOrder.setTradeStatusCode(tradeManager.tradeStatusById.get(tTrade.getTradeStatusId().longValue()).getCode());
                //添加createByName、lastModifyByName
                String createUserName = clientUserChannelCounterpartyManager.getUserNameFromMap(tOrder.getCreateBy());
                cfsOrder.setCreateByName(createUserName);
                String modifyUserName = clientUserChannelCounterpartyManager.getUserNameFromMap(tOrder.getLastModifyBy());
                cfsOrder.setLastModifyByName(modifyUserName);
                if (currencyManager.getCurrencyById(tTrade.getDealtCcyId()) != null){
                    String dealCurrency = currencyManager.getCurrencyById(tTrade.getDealtCcyId()).getCode();
                    cfsOrder.setDealCurrency(dealCurrency);
                }
                String dealCurrencyDirection = directionWatchTypeManager.getCodeById(tTrade.getDirectionId());
                cfsOrder.setDealCurrencyDirection(dealCurrencyDirection);
                //处理WatchPrice的小数位数
                TCurrencyPair currencyPair = currencyManager.currencyPairById.get(tTrade.getCcyPairId());
                TCurrency tCurrency1 = currencyManager.getCurrencyById(currencyPair.getBaseCcyId());
                TCurrency tCurrency2 = currencyManager.getCurrencyById(currencyPair.getTermCcyId());
                Integer minorUnits = tCurrency1.getMinorUnits() < tCurrency2.getMinorUnits() ? tCurrency1.getMinorUnits() : tCurrency2.getMinorUnits();
                if (tTrade.getDealtCcyId() != null && tTrade.getWatchPrice() != null){
                    String price =String.valueOf(tTrade.getWatchPrice().setScale(minorUnits, BigDecimal.ROUND_DOWN));
                    cfsOrder.setWatchPrice(price);
                }
                cfsOrderList.add(cfsOrder);
            }
        }
        //对结果进行按orderId进行排序
        cfsOrderList.sort((o1, o2) -> (int) (o2.getOrderId() - o1.getOrderId()));
        return CFSOrderResponse.builder().rows(cfsOrderList).count(orderProductTypeManager.queryOrderCount()).build();
    }

    public List<BulKeDitResponse> bulKeDitCFSSpotOrder(String user, Long orderStatus, Long orderId) {
        processUserForEnquiry(user);
        List<BulKeDitResponse> returnData = new ArrayList<>();
        Long[] actionIds = {5L};
        String[] actionCodes = {"CANCEL"};
        if (TradeStatus.getTradeStatusId(TradeStatus.ACTIVE) == orderStatus) {
            //查询订单信息
            CFSOrder cfsOrder = assemblingCfsOrder(orderId);
            List<CFSOrder> cfsOrders = new ArrayList<>();
            cfsOrders.add(cfsOrder);
            for (int i = 0; i < actionIds.length; i++) {
                BulKeDitResponse bulKeDitResponse = new BulKeDitResponse();
                bulKeDitResponse.setActionId(actionIds[i]);
                bulKeDitResponse.setActionCode(actionCodes[i]);
                bulKeDitResponse.setBulkEditValueKeys(cfsOrders);
                returnData.add(bulKeDitResponse);
            }
        }
        return returnData;
    }

    public CFSOrder assemblingCfsOrder(Long orderId) {
        CFSOrder cfsOrder = new CFSOrder();
        List<TOrder> orderResults = orderProductTypeManager.getOrderByOrderId(orderId);
        if (!orderResults.isEmpty()) {
            TOrder tOrder = orderResults.get(0);
            BeanUtils.copyProperties(tOrder, cfsOrder);
            //添加productTypeCode
            cfsOrder.setProductTypeCode(orderProductTypeManager.getProductTypeCodeFromId(tOrder.getProductTypeId()));
            //添加OrderStatusCode
            cfsOrder.setOrderStatusCode(orderProductTypeManager.getOrderStatusCodeById(tOrder.getOrderStatusId()));
            //添加OrderTypeCode
            cfsOrder.setOrderTypeCode(orderProductTypeManager.getOrderTypeCodeById(tOrder.getOrderTypeId()));
            //添加cifNumber
            List<TCfsProperties> tCfsProperties = cfsManager.getCfsPropertiesUsingOrderId(tOrder.getId());
            if (tCfsProperties != null && tCfsProperties.size() > 0) {
                cfsOrder.setCfsRefNumber(tCfsProperties.get(0).getCifNumber());
            }
            List<TTrade> tradeResults = tradeManager.getTradeByOrderId(tOrder.getId());
            if (!tradeResults.isEmpty()) {
                TTrade tTrade = tradeResults.get(0);
                BeanUtils.copyProperties(tTrade, cfsOrder);
                //添加ccyPair
                cfsOrder.setCcyPair(currencyManager.getCurrencyPairFromMap(tTrade.getCcyPairId()).getCcyPair());
                //添加tradeStatusCode
                cfsOrder.setTradeStatusCode(tradeManager.tradeStatusById.get(tTrade.getTradeStatusId().longValue()).getCode());
                //添加createByName、lastModifyByName
                String createUserName = clientUserChannelCounterpartyManager.getUserNameFromMap(tOrder.getCreateBy());
                cfsOrder.setCreateByName(createUserName);
                String modifyUserName = clientUserChannelCounterpartyManager.getUserNameFromMap(tOrder.getLastModifyBy());
                cfsOrder.setLastModifyByName(modifyUserName);
                //add dealCurrency、dealCurrencyDirection
                String dealCurrency = currencyManager.getCurrencyById(tTrade.getDealtCcyId()).getCode();
                cfsOrder.setDealCurrency(dealCurrency);
                String dealCurrencyDirection = directionWatchTypeManager.getCodeById(tTrade.getDirectionId());
                cfsOrder.setDealCurrencyDirection(dealCurrencyDirection);
                //处理WatchPrice的小数位数
                TCurrencyPair currencyPair = currencyManager.currencyPairById.get(tTrade.getCcyPairId());
                TCurrency tCurrency1 = currencyManager.getCurrencyById(currencyPair.getBaseCcyId());
                TCurrency tCurrency2 = currencyManager.getCurrencyById(currencyPair.getTermCcyId());
                Integer minorUnits = tCurrency1.getMinorUnits() < tCurrency2.getMinorUnits() ? tCurrency1.getMinorUnits() : tCurrency2.getMinorUnits();
                if (tTrade.getDealtCcyId() != null && tTrade.getWatchPrice() != null){
                    String price =String.valueOf(tTrade.getWatchPrice().setScale(minorUnits, BigDecimal.ROUND_DOWN));
                    cfsOrder.setWatchPrice(price);
                }
            }
        }
        return cfsOrder;
    }

    private void processUserForEnquiry(String user) {
        if (StringUtils.isBlank(user)) {
            throw new MissingUserException(APIErrorConstant.MissingUserExceptionMessage, APIErrorConstant.MissingUserExceptionCode);
        }
        try {
            Integer id = clientUserChannelCounterpartyManager.getUserIdFromMap(user);
            log.debug("User: {}->{}", user, id);
        } catch (InvalidInputMapException invalidInputMapException) {
            throw new InvalidUserException(APIErrorConstant.InvalidUserExceptionMessage, APIErrorConstant.InvalidUserExceptionCode);
        }
    }

    private List<SearchResponse> processOrigArgs(List<SearchResponse> list) {
        for (SearchResponse response : list) {
            List<TTrade> tradeByOrderId = tradeManager.getTradeByOrderId(response.getOrderId().longValue());
            TTrade tTrade = tradeByOrderId.get(0);
            if (!"LIMIT".equalsIgnoreCase(response.getOrderType())) {
                continue;
            }
            response.setWatchPrice(new BigDecimal(tTrade.getWatchPrice().stripTrailingZeros().toPlainString()));
            response.setPrice(new BigDecimal(tTrade.getPriceOrig().stripTrailingZeros().toPlainString()));
            response.setDealtccy(currencyManager.getCurrencyFromMap(tTrade.getDealtCcyIdOrig()));
            response.setDirection(directionWatchTypeManager.getCodeById(tTrade.getDirectionIdOrig()));
            TCurrencyPair currencyPair = tCurrencyPairMapper.selectById(tTrade.getCcyPairId());
            if (tTrade.getDealtCcyIdOrig().equals(currencyPair.getTermCcyId())) {
                response.setDealtAmount(new BigDecimal(tTrade.getContraAmt().stripTrailingZeros().toPlainString()));
                response.setContraAmount(new BigDecimal(tTrade.getDealtAmt().stripTrailingZeros().toPlainString()));
            }
            response.setDealtAmount(response.getDealtAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            response.setContraAmount(response.getContraAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return list;
    }

    private void checkSearchParams(CFSOrderEnquiryRequest cfsOrderEnquiryRequest) {
        if (cfsOrderEnquiryRequest == null || CollectionUtils.isEmpty(cfsOrderEnquiryRequest.getParams())) {
            throw new MissingSearchParamException(APIErrorConstant.MissingSearchParamExceptionMessage, APIErrorConstant.MissingSearchParamExceptionCode);
        }
        for (SearchParam input : cfsOrderEnquiryRequest.getParams()) {
            if (!StringUtils.equalsAnyIgnoreCase(input.getSearchType(), "orderId", "cifNumber", "cif")) {
                throw new InvalidSearchParamException(APIErrorConstant.InvalidSearchParamExceptionMessage, APIErrorConstant.InvalidSearchParamExceptionCode);
            }
        }
    }

    public List<SearchResponse> processSearchParams(CFSOrderEnquiryRequest cfsOrderEnquiryRequest) {
        List<SearchResponse> results = new ArrayList<>();
        for (SearchParam param : cfsOrderEnquiryRequest.getParams()) {
            String searchType = param.getSearchType();
            if ("cif".equalsIgnoreCase(searchType)) {
                searchType = "cifNumber";
            }
            switch (searchType) {
                case "orderId":
                    TOrder order = null;
                    TTrade trade = null;
                    TCfsProperties cfsProperties = null;
                    try {
                        List<TOrder> orderResults = orderProductTypeManager.getOrderByOrderId(Long.parseLong(param.getValue()));
                        if (!orderResults.isEmpty()) {
                            order = orderResults.get(0);
                            List<TTrade> tradeResults = tradeManager.getTradeByOrderId(order.getId());
                            if (!tradeResults.isEmpty()) {
                                trade = tradeResults.get(0);
                            }
                            List<TCfsProperties> cfsPropertiesResults = cfsManager.getCfsPropertiesUsingOrderId(order.getId());
                            if (!cfsPropertiesResults.isEmpty()) {
                                cfsProperties = cfsPropertiesResults.get(0);
                            }
                            SearchResponse searchResponse = SearchResponse.builder()
                                .ccyPair(currencyManager.getCurrencyPairFromMap(trade.getCcyPairId()).getCcyPair())
                                .orderId(order.getId().intValue())
                                .orderType(orderProductTypeManager.orderTypeById.get(order.getOrderTypeId()).getCode())
                                .productType(orderProductTypeManager.productTypeById.get(order.getProductTypeId()).getCode())
                                .status(tradeManager.tradeStatusById.get(trade.getTradeStatusId().longValue()).getCode())
                                .channel(clientUserChannelCounterpartyManager.getChannelNameFromMap(order.getChannelId()))
                                .cifNumber(cfsProperties.getCifNumber())
                                .cfsRefNumber(order.getCfsRefNumber())
                                .client(clientUserChannelCounterpartyManager.getClientNameFromMap(order.getClientId()))
                                .createTimestamp(order.getCreateTimestamp())
                                .contraAmount(trade.getContraAmt())
                                .dealtAmount(trade.getDealtAmt())
                                .dealtccy(currencyManager.getCurrencyFromMap(trade.getDealtCcyId()))
                                .expireTimestamp(trade.getExpireTimestamp())
                                .direction(directionWatchTypeManager.getCodeById(trade.getDirectionId()))
                                .fillTimestamp(trade.getFillTimestamp())
                                .price(trade.getPrice())
                                .longCustomerName(trade.getLongCustomerName())
                                .watchPrice(trade.getWatchPrice())
                                .build();
                            results.add(searchResponse);
                        }
                    } catch (NumberFormatException numberFormatException) {
                        log.warn("Unable to convert {}, returning empty resultset.", param.getValue());
                    }
                    break;
                case "cifNumber":
                    List<TCfsProperties> cfsPropertiesResults = cfsManager.getCfsPropertiesUsingCIF(param.getValue());
                    for (TCfsProperties properties : cfsPropertiesResults) {
                        List<TOrder> orderResults = orderProductTypeManager.getOrderByOrderId(properties.getOrderId());
                        List<TTrade> tradeResults = tradeManager.getTradeByOrderId(properties.getOrderId());
                        if (!orderResults.isEmpty() && !tradeResults.isEmpty()) {
                            TOrder order1 = orderResults.get(0);
                            TTrade trade1 = tradeResults.get(0);
                            SearchResponse searchResponse = SearchResponse.builder()
                                .ccyPair(currencyManager.getCurrencyPairFromMap(trade1.getCcyPairId()).getCcyPair())
                                .orderId(order1.getId().intValue())
                                .orderType(orderProductTypeManager.orderTypeById.get(order1.getOrderTypeId()).getCode())
                                .productType(orderProductTypeManager.productTypeById.get(order1.getProductTypeId()).getCode())
                                .status(tradeManager.tradeStatusById.get(trade1.getTradeStatusId().longValue()).getCode())
                                .channel(clientUserChannelCounterpartyManager.getChannelNameFromMap(order1.getChannelId()))
                                .cifNumber(properties.getCifNumber())
                                .cfsRefNumber(order1.getCfsRefNumber())
                                .client(clientUserChannelCounterpartyManager.getClientNameFromMap(order1.getClientId()))
                                .createTimestamp(order1.getCreateTimestamp())
                                .contraAmount(trade1.getContraAmt())
                                .dealtAmount(trade1.getDealtAmt())
                                .dealtccy(currencyManager.getCurrencyFromMap(trade1.getDealtCcyId()))
                                .expireTimestamp(trade1.getExpireTimestamp())
                                .direction(directionWatchTypeManager.getCodeById(trade1.getDirectionId()))
                                .fillTimestamp(trade1.getFillTimestamp())
                                .price(trade1.getPrice())
                                .longCustomerName(trade1.getLongCustomerName())
                                .watchPrice(trade1.getWatchPrice())
                                .build();
                            results.add(searchResponse);
                        }
                    }
                    break;
                default:
                    log.warn("Search Param is invalid");
                    break;
            }
        }
        return results;
    }

    public List<TUiViewLabel> mergeUiViewData(String user) {
        processUserForEnquiry(user);
        return OrderColumns.COLUMN_HEADERS;
    }

    /**
     * 只有active 状态能操作
     *
     * @param tradeStatusId tradeStatusId
     */
    public static void validStatus(Integer tradeStatusId) {
        if (!tradeStatusId.equals(TradeStatus.getTradeStatusId(TradeStatus.ACTIVE))) {
            throw new OnlyTheActiveStateCanBeModifiedException(APIErrorConstant.ONLY_THE_ACTIVE_STATE_CAN_BE_MODIFIED_MESSAGE, APIErrorConstant.ONLY_THE_ACTIVE_STATE_CAN_BE_MODIFIED);
        }
    }

    public void batchCancel(String user, BatchCancelRequest batchCancelRequest) {
        for (Long id : batchCancelRequest.getBulkEditValueKeys()) {
            this.cancelCFSOrder(user, id);
        }

    }
}

enum OrderStatus {
    DRAFT(1), ACTIVE(2), COMPLETED(3), PEND_ACK(4);
    private final int id;

    private OrderStatus(int id) {
        this.id = id;
    }

    public static int getOrderStatusId(OrderStatus orderStatus) {
        return orderStatus.id;
    }
}

enum TradeStatus {
    DRAFT(1), REJECTED(2), PEND_ACK(3), ACTIVE(4), RATE_TRIGGERED(5), INACTIVE(6), ACTIVE_PARTIAL_FILLED(7),
    EXPIRED_PARTIAL_FILLED(8), CANCELLED_PARTIAL_FILLED(9), FILLED(10), EXPIRED(11), CANCELLED(12), COMPLETED(13),
    COMPLETED_STP(14), CALL_REQUIRED(15);
    private final int id;

    private TradeStatus(int id) {
        this.id = id;
    }

    public static int getTradeStatusId(TradeStatus tradeStatus) {
        return tradeStatus.id;
    }
}

enum WatchType {
    MANUAL(1), SYSTEM(2);
    private final int id;

    private WatchType(int id) {
        this.id = id;
    }

    public static int getWatchTypeId(WatchType watchType) {
        return watchType.id;
    }
}

enum TimeInForce {
    GoodToDuration(1);
    private final int id;

    private TimeInForce(int id) {
        this.id = id;
    }

    public static int getTimeInForceId(TimeInForce timeInForce) {
        return timeInForce.id;
    }
}

enum Timezone {
    Singapore(1);
    private final int id;

    private Timezone(int id) {
        this.id = id;
    }

    public static int getTimezoneId(Timezone timezone) {
        return timezone.id;
    }
}

@Getter
enum OrderColumns {
    /**
     * 列表展示字段
     */
    ORDER_ID("orderId", "Order ID", 1),
    PRODUCT_TYPE_CODE("productTypeCode", "Product Type", 7),
    ORDER_TYPE_CODE("orderTypeCode", "Order Type", 4),
    ID("id", "Sub Order ID", 2),
    TRADE_STATUS_CODE("tradeStatusCode", "Sub Order Status", 3),
    SETTLEMENT_DATE("settlementDate", "Value Date", 6),
    TRADE_DATE("tradeDate", "Trade Date", 5),
    CCY_PAIR("ccyPair", "Currency Pair", 9),
    PRICE("price", "Limit Price", 10),
    ALLOW_PARTIAL_FILL("allowPartialFill", "Allow Partial Fill"),
    EXPIRE_TIMESTAMP("expireTimestamp", "Expire Time", 17),
    CALL_REQUIRED("callRequired", "Call Required", 16),
    DEALT_AMT("dealAmtStr", "Dealt Amount", 12),
    CONTRA_AMT("contraAmtStr", "Contra Amount", 13),
    CREATE_BY_NAME("createByName", "Creator ID", 8),
    CREATE_TIMESTAMP("createTimestamp", "Create Time"),
    LAST_MODIFY_BY_NAME("lastModifyByName", "Last Modified"),
    LAST_MODIFY_TIMESTAMP("lastModifyTimestamp", "Last Modify Time"),
    CFS_REF_NUMBER("cfsRefNumber", "Cif Number", 15),
    MESSAGE("message", "Message"),
    LONG_CUSTOMER_NAME("longCustomerName", "Long Customer Name", 16),
    WATCH_PRICE("watchPrice", "Watch Price", 11),
    DEAL_CURRENCY("dealCurrency", "Deal Currency", 9),
    DEAL_CURRENCY_DIRECTION("dealCurrencyDirection", "Deal Currency Direction", 9),
    MARKET_RATE("marketRate", "Market Rate", 14);

    private final String field;
    private final String name;
    private final int position;

    protected static final List<TUiViewLabel> COLUMN_HEADERS = new ArrayList<>();

    static {
        for (OrderColumns value : OrderColumns.values()) {
            TUiViewLabel tUiViewLabel = new TUiViewLabel();
            tUiViewLabel.setUiViewId(new BigDecimal(1000));
            tUiViewLabel.setGroupable((short) 0);
            tUiViewLabel.setPosition((long) value.getPosition());
            tUiViewLabel.setUiLabelCode(value.getField());
            tUiViewLabel.setHeaderName(value.getName());
            COLUMN_HEADERS.add(tUiViewLabel);
        }
        COLUMN_HEADERS.sort((e1, e2) -> (int) (e1.getPosition() - e2.getPosition()));
    }

    OrderColumns(String field, String name, int position) {
        this.field = field;
        this.name = name;
        this.position = position;
    }

    OrderColumns(String field, String name) {
        this.field = field;
        this.name = name;
        this.position = 999;
    }
}
