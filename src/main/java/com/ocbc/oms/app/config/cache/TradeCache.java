package com.ocbc.oms.app.config.cache;

import com.ocbc.oms.app.model.TTrade;
import com.ocbc.oms.app.model.dto.RealTimePriceVo;
import com.ocbc.oms.app.model.dto.TradeDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description: trade cache
 * @Author zhenMing.pan
 * @Date 2021/11/18 11:29
 * @Version V1.0.0
 **/
@Slf4j
public class TradeCache {

    /**
     * active trade cache
     * key: tradeId
     * value: trade
     */
    private static final ConcurrentHashMap<Long, TradeDto> TRADE_ACTIVE_CACHE = new ConcurrentHashMap<>();

    /**
     * notify the waterline(up to it / down to it)
     * key: ccyPair
     * value: midPrice
     */
    private static final ConcurrentHashMap<String, BigDecimal> PRICE_LEVEL_CACHE = new ConcurrentHashMap<>();

    /**
     * Real-time price cache
     * key: ccyPair
     * value: TCurrencyPair
     */
    private static ConcurrentHashMap<String, RealTimePriceVo> REALTIME_PRICE_CACHE = new ConcurrentHashMap<>();

    public static ConcurrentMap<Long, TradeDto> getActiveTrade() {
        return TRADE_ACTIVE_CACHE;
    }

    public static void putActiveTrade(Integer orderTypeId, String ccyPair, TTrade tTrade) {
        Long id = tTrade.getId();
        if (!CollectionUtils.isEmpty(TRADE_ACTIVE_CACHE)) {
            TradeDto oldTradeDto = TRADE_ACTIVE_CACHE.get(id);
            if (oldTradeDto != null && tTrade.getWatchPrice() != null) {
                oldTradeDto.setPrice(tTrade.getPrice());
                oldTradeDto.setWatchPrice(tTrade.getWatchPrice());
                TRADE_ACTIVE_CACHE.put(id, oldTradeDto);
                return;
            }
        }

        TradeDto tradeDto = new TradeDto();
        BeanUtils.copyProperties(tTrade, tradeDto);
        if (orderTypeId != null) {
            tradeDto.setOrderTypeId(orderTypeId);
        }
        if (StringUtils.isNotBlank(ccyPair)) {
            tradeDto.setCcyPair(ccyPair);
        }
        TRADE_ACTIVE_CACHE.put(id, tradeDto);
    }

    public static void putActiveTrade(TTrade tTrade) {
        putActiveTrade(null, null, tTrade);
    }

    public static void removeTrade(Long tradeId) {
        if (CollectionUtils.isEmpty(TRADE_ACTIVE_CACHE) || !TRADE_ACTIVE_CACHE.containsKey(tradeId)) {
            return;
        }
        TRADE_ACTIVE_CACHE.remove(tradeId);
    }

    public static void clear() {
        TRADE_ACTIVE_CACHE.clear();
    }

    /**
     * ------------------ for price level notify --------------------------
     */

    public static void cacheCallOrderPriceLevel(String ccyPair, BigDecimal midPrice) {
        PRICE_LEVEL_CACHE.put(ccyPair, midPrice);
    }

    public static BigDecimal getCallOrderPriceLevel(String ccyPair) {
        if (CollectionUtils.isEmpty(PRICE_LEVEL_CACHE)) {
            return BigDecimal.ZERO;
        }
        return PRICE_LEVEL_CACHE.get(ccyPair);
    }


    public static void clearCallOrderPriceLevel() {
        log.info("cache clear before, size: {}", getSize());
        PRICE_LEVEL_CACHE.clear();
        log.info("cache clear after, size: {}", getSize());
    }

    private static int getSize() {
        if (CollectionUtils.isEmpty(PRICE_LEVEL_CACHE)) {
            return 0;
        }
        return PRICE_LEVEL_CACHE.size();
    }

    /**
     * ------------------ TCurrencyPairVo cache ------------------------
     */
    public static RealTimePriceVo getRealTimePriceVo(String ccyPair) {
        return REALTIME_PRICE_CACHE.get(ccyPair);
    }

    public static ConcurrentHashMap<String, RealTimePriceVo> getAllRealTimePrice() {
        return REALTIME_PRICE_CACHE;
    }

    public static void putRealTimePriceVo(String ccyPair, BigDecimal bidRate, BigDecimal askRate) {
        RealTimePriceVo realTimePriceVo = new RealTimePriceVo();
        realTimePriceVo.setAskRate(askRate);
        realTimePriceVo.setBidRate(bidRate);
        REALTIME_PRICE_CACHE.put(ccyPair, realTimePriceVo);
    }
}
