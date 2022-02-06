package com.ocbc.oms.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.ocbc.oms.app.consts.CommonConstants.CONNECT_TIME_OUT;
import static com.ocbc.oms.app.consts.CommonConstants.READ_TIME_OUT;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(READ_TIME_OUT);
        factory.setConnectTimeout(CONNECT_TIME_OUT);
        return factory;
    }
}