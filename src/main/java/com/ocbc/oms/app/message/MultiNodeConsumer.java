package com.ocbc.oms.app.message;


import com.ocbc.oms.app.event.Event;
import com.ocbc.oms.app.event.factory.EventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


/**
 * @author pzm
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "ibm.mq", value = "enable", havingValue = "true")
public class MultiNodeConsumer {

    /**
     * Multi Node Notice socket
     *
     * @param message spot price json
     */
    @JmsListener(destination = "${ibm.mq.socket.topic}", containerFactory = "topicContainer")
    public void productAggregatePrice(String message) {
        try {
            log.info("Event message : {}", message);
            Event event = EventFactory.create(message);
            event.execute();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}
