package com.ocbc.oms.app.config.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Custom reject policy, when the thread pool in full, discard all threads in queue
 *
 * @author Junpeng.He
 */
@Slf4j
public class DiscardAllPolicy implements RejectedExecutionHandler {



    /**
     * remove all task in queue when the thread pool if full, and submit the latest price to the pool,
     * because we always want the latest price.
     * when a thread pool hit saturated status, all prices in the queue actually are expired prices, we don't need them.
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            executor.getQueue().clear();
            executor.execute(r);
            String threadName = r.getClass().getSimpleName();
            log.info("DiscardAllPolicy Triggered, cleared queue and submitted the latest thread: \n{}", threadName);
        }
    }
}
