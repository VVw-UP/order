package com.ocbc.oms.app.dbservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.config.kafka.OrderKafkaSender;
import com.ocbc.oms.app.consts.DataConstant;
import com.ocbc.oms.app.managers.DbOMSManager;
import com.ocbc.oms.app.message.IBMMqSender;
import com.ocbc.oms.app.model.CFSOrder;
import com.ocbc.oms.app.model.dto.FxDataPrice;
import com.ocbc.oms.app.model.dto.TradeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @Description: limit trade done
 * @Author zhenMing.pan
 * @Date 2021/11/18 16:18
 * @Version V1.0.0
 **/
@Slf4j
@Component
public class LimitTrade {

    @Autowired
    private DbOMSManager dbOMSManager;
    @Autowired
    private TradeService tradeService;
    @Autowired(required = false)
    private IBMMqSender ibmMqSender;

    @Autowired
    private OrderKafkaSender orderKafkaSender;

    public void inputLimitTrade(FxDataPrice fxDataPrice) {
        ConcurrentMap<Long, TradeDto> activeTrade = TradeCache.getActiveTrade();
        // orderTypeId : 2  limit order
        List<TradeDto> limitList = activeTrade.values().stream().filter(trade -> fxDataPrice.getCcyPair().equals(trade.getCcyPair()) && trade.getOrderTypeId() == 2)
            .collect(Collectors.toList());
        BigDecimal bidRate = fxDataPrice.getBidRate();
        BigDecimal askRate = fxDataPrice.getAskRate();
        // DirectionId : 1 buy 、2 sell
        List<TradeDto> buyLimitList = limitList.stream().filter(tradeDto -> tradeDto.getDirectionId() == 1).collect(Collectors.toList());
        List<TradeDto> sellLimitList = limitList.stream().filter(tradeDto -> tradeDto.getDirectionId() == 2).collect(Collectors.toList());
        buyLimitList.forEach(tradeDto -> {
            if (askRate.compareTo(tradeDto.getWatchPrice()) <= 0) {
                fillTrade(activeTrade, tradeDto);
            }
        });
        sellLimitList.forEach(tradeDto -> {
            if (bidRate.compareTo(tradeDto.getWatchPrice()) >= 0) {
                fillTrade(activeTrade, tradeDto);
            }
        });

    }

    private void fillTrade(ConcurrentMap<Long, TradeDto> activeTrade, TradeDto tradeDto) {
        //修改订单状态
        int count = tradeService.fillTrades(tradeDto.getId(), dbOMSManager.getFilled());
        if (count > 0) {

            log.info("CFS limit fill : " + JSON.toJSONString(tradeDto));
            orderKafkaSender.sendOrderMsg(tradeDto.getOrderId(), "FILLED");
            noticeSocket(tradeDto.getOrderId(), DataConstant.UPDATE);
        }
        activeTrade.remove(tradeDto.getId());


    }

    private void noticeSocket(Long orderId, String eventType) {
        //Socket 通知前端
        CFSOrder cfsOrder = dbOMSManager.assemblingCfsOrder(orderId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DataConstant.EVENT_TYPE, eventType);
        jsonObject.put(DataConstant.DATA, cfsOrder);
        try {
            ibmMqSender.sendPersistOffer(jsonObject.toJSONString());
        } catch (Exception ignored) {
            //todo OCBC暂时不支持IBM MQ
        }
    }

    /**
     * tradeId --> orderId
     *
     * @param tradeId tradeId
     */
    public void noticeSocket(Long tradeId) {
        TradeDto tradeDto = TradeCache.getActiveTrade().get(tradeId);
        if (tradeDto == null) {
            return;
        }
        noticeSocket(tradeDto.getOrderId(), DataConstant.UPDATE);
    }

    /**
     * tradeId --> orderId
     *
     * @param orderId orderId
     */
    public void noticeSocketByOrderId(Long orderId, String eventType) {

        noticeSocket(orderId, eventType);
    }
}
