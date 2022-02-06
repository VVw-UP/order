package com.ocbc.oms.app.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ocbc.oms.app.config.RateSourceConfig;
import com.ocbc.oms.app.util.RateSourceChangePo;
import com.ocbc.oms.app.util.TradeDataObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

@Component
@Slf4j
@ServerEndpoint("/webSocket/tradeData/{sid}")
public class RateSourceIdConsumer {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DATE_FORMAT).create();

    @Autowired
    RateSourceConfig rateSourceConfig;

    @JmsListener(destination = "${ibm.mq.topic.changeSourceTopic}", containerFactory = "topicContainer")
    public void receiveMqSynMessage(String message) {
        log.debug("receiveMqSynMessage:{}", message);
        if (message == null) {
            return;
        }
        try {
            TradeDataObj dataObj = gson.fromJson(message, TradeDataObj.class);
            if (dataObj != null) {
                RateSourceChangePo sourceChangePo = dataObj.getSourceChangePo();
                if (sourceChangePo != null) {
                    log.info("sourceChangeId: {} ", sourceChangePo.getRateSourceId());
                    rateSourceConfig.setRateSourceId(sourceChangePo.getRateSourceId());
                    return;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}