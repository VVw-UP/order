package com.ocbc.oms.app.config.thread.pool;

import com.ocbc.oms.app.model.dto.FxDataPrice;
import com.ocbc.oms.app.config.thread.SourcePriceThread;
import com.ocbc.oms.app.config.thread.ThreadPoolFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: source price
 * @Author zhenMing.pan
 * @Date 2021/11/22 17:26
 * @Version V1.0.0
 **/
@Component
public class SourcePriceThreadPool {
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final int QUEUE_SIZE = POOL_SIZE * 10;
    private final ThreadPoolExecutor threadPoolExecutor = ThreadPoolFactory.createDynamicThreadPool("price", 1, POOL_SIZE, QUEUE_SIZE);

    public void submit(FxDataPrice fxDataPrice) {
        SourcePriceThread sourcePriceThread = new SourcePriceThread(fxDataPrice);
        threadPoolExecutor.submit(sourcePriceThread);
    }
}
