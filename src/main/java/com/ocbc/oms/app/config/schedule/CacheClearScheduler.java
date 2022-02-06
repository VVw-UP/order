package com.ocbc.oms.app.config.schedule;

import com.ocbc.oms.app.config.cache.TradeCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description cache clear
 * @author huijian.zhang
 * @date 2021.11.18 17:31
 */
@Component
public class CacheClearScheduler {

    @Scheduled(cron = "${schedule.cache.clear}")
    public void cacheClear() {
        TradeCache.clearCallOrderPriceLevel();
    }

}
