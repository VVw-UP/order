package com.ocbc.oms.app.config.thread;

import com.ocbc.oms.app.dbservice.LimitTrade;
import com.ocbc.oms.app.managers.CallOdaTradeManager;
import com.ocbc.oms.app.model.dto.FxDataPrice;
import com.ocbc.oms.app.util.ApplicationContextProvider;

/**
 * @Description: source price thread
 * @Author zhenMing.pan
 * @Date 2021/11/22 17:31
 * @Version V1.0.0
 **/
public class SourcePriceThread implements Runnable{

    private static final LimitTrade limitTrade =  ApplicationContextProvider.getBean(LimitTrade.class);
    private static final CallOdaTradeManager callOdaTradeManager =  ApplicationContextProvider.getBean(CallOdaTradeManager.class);

    private FxDataPrice fxDataPrice;

    public SourcePriceThread(FxDataPrice fxDataPrice) {
        this.fxDataPrice = fxDataPrice;
    }

    @Override
    public void run() {
        // limit order
        limitTrade.inputLimitTrade(fxDataPrice);
        // call order
        callOdaTradeManager.processTradeAndNotify(fxDataPrice);
    }
}
