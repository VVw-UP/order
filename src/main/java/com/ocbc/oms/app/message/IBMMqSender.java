package com.ocbc.oms.app.message;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Import(JmsAutoConfiguration.class)
@Slf4j
@ConditionalOnProperty(prefix = "ibm.mq", value = "enable", havingValue = "true")
public class IBMMqSender {


    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource
    private JmsTemplate topicTemplate;
    @Value("${ibm.mq.socket.topic}")
    private String socketTopic;

    /**
     * send offer to marketData
     *
     * @param jsonStr
     */
    public void sendPersistOffer(String jsonStr) {
        topicTemplate.convertAndSend(socketTopic, jsonStr);
    }




}
