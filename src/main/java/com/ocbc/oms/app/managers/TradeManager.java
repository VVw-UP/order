package com.ocbc.oms.app.managers;

import com.ocbc.oms.app.api.CFSOrderRequest;
import com.ocbc.oms.app.dbservice.TradeService;
import com.ocbc.oms.app.dbservice.TradeStatusService;
import com.ocbc.oms.app.model.TTrade;
import com.ocbc.oms.app.model.TTradeStatus;
import com.ocbc.oms.app.model.dto.TradeDateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ocbc.oms.app.consts.CommonConstants.DIRECTION_BUY;
import static com.ocbc.oms.app.consts.CommonConstants.DIRECTION_SELL;

@Component
@Slf4j
@DependsOn("flywayInitializer")
public class TradeManager {
    private final TradeStatusService tradeStatusService;
    private final DirectionWatchTypeManager directionWatchTypeManager;
    private Map<String, TTradeStatus> tradeStatusMap;
    public Map<Long, TTradeStatus> tradeStatusById;

    private final TradeService tradeService;

    public TradeManager(TradeStatusService tradeStatusService, TradeService tradeService, DirectionWatchTypeManager directionWatchTypeManager) {
        this.tradeStatusService = tradeStatusService;
        this.tradeService = tradeService;
        this.directionWatchTypeManager = directionWatchTypeManager;
    }

    public int addTrade(TTrade tTrade) {
        return tradeService.addTrade(tTrade);
    }

    public TradeDateDto getTradeDateAndValueDate(String ccyPair){
        return tradeService.getTradeDateAndValueDate(ccyPair);
    }

    public List<TTrade> getTradeByOrderId(Long orderId) {
        return tradeService.getTradeByOrderId(orderId);
    }

    public int updateTrade(TTrade tTrade) {
        return tradeService.updateTrade(tTrade);
    }

    public List<TTrade> queryTrades() {
        return tradeService.findAllTrade();
    }

    public void reCalcPrice(BigDecimal price, TTrade trade, String sideType, Integer minorUnits) {
        tradeService.calculatePrice(price, trade, sideType, minorUnits);
    }

    public Integer exchangeTradeSide(String tradeSide) {
        return tradeSide.equals(DIRECTION_SELL) ? directionWatchTypeManager.getIdByCode(DIRECTION_BUY) : directionWatchTypeManager.getIdByCode(DIRECTION_SELL);
    }

    public BigDecimal reCalculateLimitPrice(Integer minorUnits, String ccyPair, BigDecimal dealtAmount, BigDecimal basePrice, String side){
        return tradeService.calculateLimitPrice(minorUnits, ccyPair, dealtAmount, basePrice, side);
    }

    private void initializeTradeStatusMap() {
        tradeStatusMap = new HashMap<>();
        tradeStatusById = new HashMap<>();
        List<TTradeStatus> fromDatabase = tradeStatusService.getAllTradeStatuses();
        fromDatabase.forEach(tradeStatus -> {
            log.debug("Adding tradeStatus:{}", tradeStatus);
            tradeStatusMap.put(tradeStatus.getCode(), tradeStatus);
            tradeStatusById.put(tradeStatus.getId(), tradeStatus);
        });
    }

    @PostConstruct
    private void initialize() {
        initializeTradeStatusMap();
    }
}
