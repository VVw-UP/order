package com.ocbc.oms.app.config;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQTopicConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Session;

/**
 * @author mqt
 * @version 1.0
 * @date 2021/8/10 10:52
 */
@ConditionalOnProperty(prefix = "ibm.mq", value = "enable", havingValue = "true")
@Slf4j
@EnableConfigurationProperties({MQConfigurationProperties.class, MQConfigurationPropertiesBeanPostProcessor.class})
@Configuration
public class IbmMQConnectionConfig {


    @Bean
    public MQTopicConnectionFactory topicConnectionFactory(MQConfigurationProperties properties) {
        return (new MyMQConnectionFactoryFactory(properties)).createConnectionFactory(MQTopicConnectionFactory.class);
    }

    @Bean
    public MQQueueConnectionFactory queueConnectionFactory(MQConfigurationProperties properties) {
        return (new MyMQConnectionFactoryFactory(properties)).createConnectionFactory(MQQueueConnectionFactory.class);
    }


    /**
     * 消费者监听
     *
     * @param topicConnectionFactory r
     * @return r
     */
    @Bean
    @Primary
    public SimpleJmsListenerContainerFactory topicContainer(MQTopicConnectionFactory topicConnectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(topicConnectionFactory);
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        factory.setPubSubDomain(Boolean.TRUE);
        return factory;
    }

    /**
     * 消费者监听
     *
     * @param queueConnectionFactory r
     * @return r
     */
    @Bean
    public SimpleJmsListenerContainerFactory queueContainer(MQQueueConnectionFactory queueConnectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(queueConnectionFactory);
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        return factory;
    }


    @Bean
    @Primary
    public JmsTemplate queueTemplate(MQQueueConnectionFactory queueConnectionFactory) {
        return new JmsTemplate(queueConnectionFactory);
    }

    /**
     * Send
     *
     * @param topicConnectionFactory topicConnectionFactory
     * @return JmsTemplate
     */
    @Bean
    JmsTemplate topicTemplate(MQTopicConnectionFactory topicConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(topicConnectionFactory);
        // 操作域
        jmsTemplate.setPubSubDomain(Boolean.TRUE);
        return jmsTemplate;
    }

}
