package com.ocbc.oms.app.message;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocbc.oms.app.config.RateSourceConfig;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.config.thread.pool.SourcePriceThreadPool;
import com.ocbc.oms.app.consts.DataConstant;
import com.ocbc.oms.app.managers.CurrencyManager;
import com.ocbc.oms.app.model.dto.FxDataPrice;
import com.ocbc.oms.app.websocket.TradeSocket;
import com.ocbc.oms.app.websocket.WebSocketResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


/**
 * @author pzm
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "ibm.mq", value = "enable", havingValue = "true")
public class PriceConsumer {


    @Autowired
    private SourcePriceThreadPool sourcePriceThreadPool;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private TradeSocket tradeSocket;
    @Autowired
    private RateSourceConfig rateSourceConfig;

    /**
     * spot price
     *
     * @param message spot price json
     */
    @JmsListener(destination = "${ibm.mq.rate.topic}", containerFactory = "topicContainer")
    public void productAggregatePrice(String message) {
        try {
            FxDataPrice fxDataPrice = JSON.parseObject(message, FxDataPrice.class);
            if (!currencyManager.isExistCurrencyPair(fxDataPrice.getCcyPair())) {
                return;
            }
            if (StringUtils.isBlank(message)
                    || JSON.parseObject(message).getIntValue("rateSourceId") != rateSourceConfig.getRateSourceId()) {
                return;
            }
            log.debug("consumer rate msg:getCcyPair:{},getBidRate:{},getAskRate:{}", fxDataPrice.getCcyPair(), fxDataPrice.getBidRate(), fxDataPrice.getAskRate());
            TradeCache.putRealTimePriceVo(fxDataPrice.getCcyPair(), fxDataPrice.getBidRate(), fxDataPrice.getAskRate());
            sourcePriceThreadPool.submit(fxDataPrice);
            //socket 推送报价
            JSONObject jsonObject = JSON.parseObject(message);
            jsonObject.put("bidRate", fxDataPrice.getBidRate());
            jsonObject.put("askRate", fxDataPrice.getAskRate());
            tradeSocket.sendMessage(JSON.toJSONString(new WebSocketResponse<>(jsonObject, DataConstant.MARKET_RATE)));
        } catch (Exception ex) {
            log.error("Sorry, an exception has occurred.", ex);
        }
    }

}
