package com.ocbc.oms.app.config.schedule;


import com.ocbc.oms.app.config.cache.GlobalCache;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.dbservice.TradeService;
import com.ocbc.oms.app.config.init.OrderCacheInit;
import com.ocbc.oms.app.model.TTrade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Order timeout update
 *
 * @author hzy
 * @since 2021-11-16
 */
@Slf4j
@Component
public class ResetSchedule {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private GlobalCache globalCache;

    @Autowired
    private OrderCacheInit orderCacheInit;

    @Scheduled(cron = "${schedule.cron.reset}")
    public void refreshCache() {
        log.info("Start resetting order timeout cache!");
        List<TTrade> list = tradeService.findByTradeStatusId();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //初始化缓存
        Map<Long, LocalDateTime> orderTimeoutCache = globalCache.getOrderTimeoutCache();
        orderTimeoutCache.clear();
        TradeCache.clear();
        orderTimeoutCache.putAll(list.stream().filter(e -> e.getExpireTimestamp() != null).collect(Collectors.toMap(TTrade::getId, TTrade::getExpireTimestamp)));
        orderCacheInit.resetTradeWithOrderTypeCache();
        log.info("End order timeout cache update!");
    }

}
