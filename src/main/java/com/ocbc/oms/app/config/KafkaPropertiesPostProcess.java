package com.ocbc.oms.app.config;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author mqt
 * @version 1.0
 * @date 2021/10/20 13:39
 */
@Setter
@Component
@ConditionalOnProperty(prefix = "spring.kafka.jaas", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "spring.kafka.jaas.config")
public class KafkaPropertiesPostProcess implements BeanPostProcessor {

    private String krb5Path;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof KafkaProperties) {
            System.setProperty("java.security.krb5.conf", krb5Path);
        }
        return bean;
    }
}
