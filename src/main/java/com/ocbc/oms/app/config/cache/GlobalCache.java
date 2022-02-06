package com.ocbc.oms.app.config.cache;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global local cache
 *
 * @author hzy
 * @since 2021-11-16
 */
@Component
public class GlobalCache {

    /**
     * order timeout cache
     */
    private static final ConcurrentHashMap<Long, LocalDateTime> ORDER_TIMEOUT_CACHE = new ConcurrentHashMap<>(1024);

    public Map<Long, LocalDateTime> getOrderTimeoutCache() {
        return ORDER_TIMEOUT_CACHE;
    }
}
