package com.ocbc.oms.app.config;

import com.ibm.mq.spring.boot.MQConfigurationProperties;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 当isUseIBMCipherMappings 为true时不使用ssl连接
 * 避免其它ssl参数生效，导致使用ssl连接
 *
 * @author mqt
 * @version 1.0
 * @date 2021/7/23 10:51
 */
@Data
@ConfigurationProperties(
    prefix = "ibm.mq.ssl")
@Component
@ConditionalOnProperty(prefix = "ibm.mq", value = "enable", havingValue = "true")
public class MQConfigurationPropertiesBeanPostProcessor implements BeanPostProcessor {

    private String truststorePath;

    private String truststorePassword;

    private String keyStorePath;

    private String keyStorePassword;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof MQConfigurationProperties) {
            final MQConfigurationProperties properties = (MQConfigurationProperties) bean;
            if (properties.isUseIBMCipherMappings()) {
                properties.setSslCipherSpec(null);
                properties.setSslCipherSuite(null);
            } else {
                Properties systemProps = System.getProperties();
                systemProps.put("javax.net.ssl.trustStore", truststorePath);
                systemProps.put("javax.net.ssl.trustStorePassword", truststorePassword);
                systemProps.put("javax.net.ssl.keyStore", keyStorePath);
                systemProps.put("javax.net.ssl.keyStorePassword", keyStorePassword);
                System.setProperties(systemProps);
            }
        }
        return bean;
    }

}
