package com.ocbc.oms.app.config.thread;

import java.util.concurrent.*;

/**
 * @Description: 线程池工厂
 * @Author zhenMing.pan
 * @Date 2021/11/22 17:23
 * @Version V1.0.0
 **/
public class ThreadPoolFactory {
    private static final RejectedExecutionHandler REJECT_POLICY = new DiscardAllPolicy();


    public static ThreadPoolExecutor createDynamicThreadPool(String threadNamePrefix, int corePoolSize, int maxPoolSize, int queueSize) {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue <>(queueSize),
                Executors.defaultThreadFactory(), REJECT_POLICY);
    }
}
