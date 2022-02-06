package com.ocbc.oms.app.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Data
@Component
public class RateSourceConfig {

    @Value("${rateSource.api.baseUrl}")
    private String rateSourceBaseUrl;
    @Value("${rateSource.api.getRateSourceIdUrl}")
    private String getRateSourceIdUrl;

    private volatile int rateSourceId = 1;

    @PostConstruct
    public void init() {
        try {
            rateSourceId = new RestTemplate().postForObject(rateSourceBaseUrl+getRateSourceIdUrl, null, int.class);
        } catch (Exception e) {
            log.debug("can not get rateSourceId from GT,{}",e);
            return;
        }
    }
}
