package com.ocbc.oms.app.config.init;

import com.ocbc.oms.app.dbservice.TExpiryTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author mqt
 * @version 1.0
 * @date 2021/11/22 15:40
 */
@Component
public class ExpireTimestampCacheInit implements ApplicationRunner {
    @Autowired
    private TExpiryTimeService expiryTimeService;
    @Override
    public void run(ApplicationArguments args)   {

        expiryTimeService.findExpiryTimes();
    }
}
