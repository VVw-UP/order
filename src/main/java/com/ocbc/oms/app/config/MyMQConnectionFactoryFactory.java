package com.ocbc.oms.app.config;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;

import javax.jms.JMSException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author mqt
 * @version 1.0
 * @date 2021/8/10 14:18
 */
public class MyMQConnectionFactoryFactory {
    private final MQConfigurationProperties properties;

    MyMQConnectionFactoryFactory(MQConfigurationProperties properties) {
        this.properties = properties;
    }

    public <T extends MQConnectionFactory> T createConnectionFactory(Class<T> factoryClass) {

        try {
            T cf = this.createConnectionFactoryInstance(factoryClass);
            String qmName = this.properties.getQueueManager();
            cf.setStringProperty("XMSC_WMQ_QUEUE_MANAGER", qmName);
            String channel = this.properties.getChannel();
            String connName = this.properties.getConnName();
            if (!this.isNullOrEmpty(channel) && !this.isNullOrEmpty(connName)) {
                cf.setStringProperty("XMSC_WMQ_CONNECTION_NAME_LIST", connName);
                cf.setStringProperty("XMSC_WMQ_CHANNEL", channel);
                cf.setIntProperty("XMSC_WMQ_CONNECTION_MODE", 1);
            } else {
                cf.setIntProperty("XMSC_WMQ_CONNECTION_MODE", 0);
            }

            String u = this.properties.getUser();
            if (!this.isNullOrEmpty(u)) {
                cf.setStringProperty("XMSC_USERID", u);
            }
            String password = this.properties.getPassword();
            if (!this.isNullOrEmpty(password)) {
                cf.setStringProperty("XMSC_PASSWORD", password);
            }
            cf.setBooleanProperty("XMSC_USER_AUTHENTICATION_MQCSP", this.properties.isUserAuthenticationMQCSP());

            if (!this.isNullOrEmpty(this.properties.getSslCipherSuite())) {
                cf.setStringProperty("XMSC_WMQ_SSL_CIPHER_SUITE", this.properties.getSslCipherSuite());
            }

            if (!this.isNullOrEmpty(this.properties.getSslCipherSpec())) {
                cf.setStringProperty("XMSC_WMQ_SSL_CIPHER_SPEC", this.properties.getSslCipherSpec());
            }
            return cf;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | JMSException var8) {
            throw new IllegalStateException("Unable to create MQConnectionFactory" + "", var8);
        }
    }

    private <T extends MQConnectionFactory> T createConnectionFactoryInstance(Class<T> factoryClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return factoryClass.getConstructor().newInstance();
    }


    boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
