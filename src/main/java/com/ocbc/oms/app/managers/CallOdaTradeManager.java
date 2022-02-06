package com.ocbc.oms.app.managers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.consts.DataConstant;
import com.ocbc.oms.app.dbservice.TradeService;
import com.ocbc.oms.app.message.IBMMqSender;
import com.ocbc.oms.app.model.CFSOrder;
import com.ocbc.oms.app.model.dto.FxDataPrice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author huijian.zhang
 */
@Slf4j
@Component
public class CallOdaTradeManager {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private DbOMSManager dbOMSManager;
    @Autowired(required = false)
    private IBMMqSender ibmMqSender;

    /**
     * reminder order process
     *
     * @param fxDataPrice input price
     */
    public void processTradeAndNotify(FxDataPrice fxDataPrice) {
        if (fxDataPrice == null) {
            return;
        }
        BigDecimal bidRate = fxDataPrice.getBidRate();
        BigDecimal askRate = fxDataPrice.getAskRate();
        if (Objects.isNull(bidRate) || Objects.isNull(askRate)) {
            return;
        }
        String ccyPair = fxDataPrice.getCcyPair();
        TradeCache.getActiveTrade().values().forEach(tradeDto -> {
            if (!StringUtils.equalsIgnoreCase(ccyPair, tradeDto.getCcyPair()) || tradeDto.getOrderTypeId() != 1) {
                return;
            }
            Long tradeId = tradeDto.getId();

            BigDecimal price = tradeDto.getPrice();
            String[] result = price.toPlainString().split("\\.");
            int dps = result[1].length();
            BigDecimal midPrice = bidRate.add(askRate).divide(new BigDecimal(2), dps, RoundingMode.HALF_UP);
            //cache the price for next comparing
            if (price.compareTo(midPrice) == 0) {
                //trade数据更新入库
                int count = tradeService.fillTrades(tradeId, dbOMSManager.getFilled());
                if (count == 0) {
                    log.warn("tradeId: {}, trade update unsuccessfully", tradeId);
                    return;
                }

                //socket通知
                doNotifyBySocket(tradeDto.getOrderId(), ccyPair, midPrice);

                TradeCache.removeTrade(tradeId);
            }
            TradeCache.cacheCallOrderPriceLevel(ccyPair, midPrice);
        });
    }

    /**
     * notify front by socket
     *
     * @param orderId
     * @param ccyPair
     * @param midPrice
     */
    private void doNotifyBySocket(Long orderId, String ccyPair, BigDecimal midPrice) {
        CFSOrder cfsOrder = dbOMSManager.assemblingCfsOrder(orderId);
        JSONObject event = JSON.parseObject(JSON.toJSONString(cfsOrder));
        //价格触及提醒
        BigDecimal priceLevel = TradeCache.getCallOrderPriceLevel(ccyPair);
        if (priceLevel.compareTo(BigDecimal.ZERO) == 0) {
            //special hit
            event.put("message", "hit the reminder price");
        } else if (midPrice.compareTo(priceLevel) > 0) {
            //up hit
            event.put("message", "Upward hit the reminder price");
        } else {
            //down hit
            event.put("message", "Down hit the reminder price");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DataConstant.EVENT_TYPE, DataConstant.UPDATE);
        jsonObject.put(DataConstant.DATA, event);
        try {
            ibmMqSender.sendPersistOffer(jsonObject.toJSONString());
        } catch (Exception ignored) {
            //todo OCBC暂时不支持IBM MQ
        }
        log.info("send the message : {}", jsonObject.toJSONString());
    }
}
