package com.ocbc.oms.app.config.init;

import com.ocbc.oms.app.config.cache.GlobalCache;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.dbservice.TradeService;
import com.ocbc.oms.app.model.TTrade;
import com.ocbc.oms.app.model.dto.TradeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(-1)
public class OrderCacheInit implements CommandLineRunner {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private GlobalCache globalCache;

    @Override
    public void run(String... args) throws Exception {
        List<TTrade> list = tradeService.findByTradeStatusId();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //初始化缓存
        globalCache.getOrderTimeoutCache().putAll(list.stream().filter(e -> e.getExpireTimestamp() != null)
                .collect(Collectors.toMap(TTrade::getId, TTrade::getExpireTimestamp)));

        resetTradeWithOrderTypeCache();
    }

    /**
     * 获取trade信息附带trade对应的订单类型
     */
    public void resetTradeWithOrderTypeCache() {
        List<TradeDto> trades = tradeService.getTradeWithOrderType();
        if (CollectionUtils.isEmpty(trades)) {
            return;
        }
        TradeCache.getActiveTrade().putAll(trades.stream().collect(Collectors.toMap(TradeDto::getId, e -> e)));
    }


}
