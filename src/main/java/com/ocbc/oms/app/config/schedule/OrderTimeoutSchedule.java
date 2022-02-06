package com.ocbc.oms.app.config.schedule;


import com.ocbc.oms.app.config.cache.GlobalCache;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.dbservice.LimitTrade;
import com.ocbc.oms.app.dbservice.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Order timeout update
 *
 * @author hzy
 * @since 2021-11-16
 */
@Slf4j
@Component
public class OrderTimeoutSchedule {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private GlobalCache globalCache;
    @Autowired
    private LimitTrade limitTrade;

    @Scheduled(cron = "${schedule.cron.orderTimeOut}")
    public void refreshCache() {
        //查询幂等性表，尝试获取执行机会
        /*if (!idempotentGuaranteeService.updateComponentIdempotent(name, 1)) {
            log.info("The remaining services have performed the task!");
            return;
        }*/
        //当前时间
        LocalDateTime date = LocalDateTime.now();
        //获取缓存集合
        Map<Long, LocalDateTime> orderTimeoutCache = globalCache.getOrderTimeoutCache();
        if (CollectionUtils.isEmpty(orderTimeoutCache)) {
            return;
        }
        orderTimeoutCache.forEach((key, value) -> {
            //获取已超时订单的id
            if (date.compareTo(value) >= 0) {
                try {
                    //修改订单状态
                    int i = tradeService.updateTrades(key);
                    if (i > 0 ){
                        //更新成功时,socket 推送
                        limitTrade.noticeSocket(key);
                    }
                    //更新缓存
                    orderTimeoutCache.remove(key);
                    TradeCache.removeTrade(key);
                    log.info("Order timeout update succeeded!");
                } catch (Exception e) {
                    log.error("Order timeout update task execution failed!");
                }
            }
        });

    }
}
