package com.ocbc.oms.app.managers;

import com.ocbc.oms.app.dbservice.CurrencyPairService;
import com.ocbc.oms.app.dbservice.CurrencyService;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.error.api.InvalidCurrencyPairException;
import com.ocbc.oms.app.error.application.ApplicationErrorConstant;
import com.ocbc.oms.app.error.application.InvalidInputMapException;
import com.ocbc.oms.app.model.TCurrency;
import com.ocbc.oms.app.model.TCurrencyPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@DependsOn("flywayInitializer")
public class CurrencyManager {
    private final CurrencyService currencyService;
    private Map<String, TCurrency> currencyMap;

    private final CurrencyPairService currencyPairService;
    private Map<String, TCurrencyPair> currencyPairMap;
    public Map<Integer, TCurrencyPair> currencyPairById;

    public CurrencyManager(CurrencyService currencyService, CurrencyPairService currencyPairService) {
        this.currencyService = currencyService;
        this.currencyPairService = currencyPairService;
    }

    public Integer getCurrencyFromMap(String currency) {
        if (StringUtils.isNotBlank(currency) && currencyMap.containsKey(currency)) {
            return currencyMap.get(currency).getId();
        }
//        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
//                .append(" -> currencyMap")
//                .toString(),
//                ApplicationErrorConstant.InvalidTypeExceptionCode);
        return null;
    }

    public String getCurrencyFromMap(Integer currencyId) {
        if (currencyId != null && currencyId > 0) {
            for (String key : currencyMap.keySet()) {
                if (currencyId.intValue() == currencyMap.get(key).getId().intValue()) {
                    return currencyMap.get(key).getCode();
                }
            }
        }
//        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
//                .append(" -> currencyPairMap")
//                .toString(),
//                ApplicationErrorConstant.InvalidTypeExceptionCode);
        return null;
    }

    public Integer getCurrencyPairFromMap(String currencyPair) {
        if (StringUtils.isNotBlank(currencyPair) && currencyPairMap.containsKey(currencyPair)) {
            return currencyPairMap.get(currencyPair).getId();
        }
        throw new InvalidCurrencyPairException(APIErrorConstant.MissingCurrencyPairExceptionMessage, APIErrorConstant.MissingCurrencyPairExceptionCode);
    }

    public boolean isExistCurrencyPair(String currencyPair) {
        if (StringUtils.isBlank(currencyPair)) {
            return false;
        }
        return currencyPairMap.containsKey(currencyPair);
    }

    public TCurrencyPair getCurrencyPairFromMap(Integer currencyPairId) {
        if (currencyPairId != null && currencyPairId > 0) {
            for (String key : currencyPairMap.keySet()) {
                if (currencyPairId.intValue() == currencyPairMap.get(key).getId().intValue()) {
                    return currencyPairMap.get(key);
                }
            }
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> currencyPairMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public TCurrency getCurrencyById(Integer id) {
        return currencyService.getCurrencyById(id);
    }

    private void initializeCurrencyMap() {
        currencyMap = new HashMap<>();
        List<TCurrency> fromDatabase = currencyService.getAllCurrencies();
        fromDatabase.forEach(currency -> {
            log.debug("Adding currency:{}", currency);
            currencyMap.put(currency.getCode(), currency);
        });
    }

    private void initializeCurrencyPairMap() {
        currencyPairMap = new HashMap<>();
        currencyPairById = new HashMap<>();
        List<TCurrencyPair> fromDatabase = currencyPairService.getAllCurrencyPairs();
        fromDatabase.forEach(currencyPair -> {
            log.debug("Adding currencyPair:{}", currencyPair);
            currencyPairMap.put(currencyPair.getCcyPair(), currencyPair);
            currencyPairById.put(currencyPair.getId(), currencyPair);
        });
    }

    @PostConstruct
    private void initialize() {
        initializeCurrencyMap();
        initializeCurrencyPairMap();
    }

    public Boolean updateTCurrencyPair(TCurrencyPair tCurrencyPair) {
        this.getCurrencyPairFromMap(tCurrencyPair.getCcyPair().toUpperCase());
        return currencyPairService.updateTCurrencyPair(tCurrencyPair);
    }

    public Boolean calNoFlyZone(String ccyPair, String orderType, BigDecimal price, String direction){
        return currencyPairService.calNoFlyZone(ccyPair,orderType,price,direction);
    }
}
