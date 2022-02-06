package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.error.api.CurrencyPairUpdateException;
import com.ocbc.oms.app.error.api.NoRealTimeQuotationException;
import com.ocbc.oms.app.model.CcyPairs;
import com.ocbc.oms.app.model.TCurrency;
import com.ocbc.oms.app.model.TCurrencyPair;
import com.ocbc.oms.app.model.dto.GlobalDictDto;
import com.ocbc.oms.app.model.dto.RealTimePriceVo;
import com.ocbc.oms.app.repository.TCurrencyMapper;
import com.ocbc.oms.app.repository.TCurrencyPairMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.ocbc.oms.app.consts.CommonConstants.DIRECTION_BUY;
import static com.ocbc.oms.app.consts.CommonConstants.DIRECTION_SELL;

@Service
public class CurrencyPairService {
    private final TCurrencyPairMapper tCurrencyPairMapper;

    public CurrencyPairService(TCurrencyPairMapper tCurrencyPairMapper) {
        this.tCurrencyPairMapper = tCurrencyPairMapper;
    }

    @Autowired
    private TCurrencyMapper tCurrencyMapper;

    public List<TCurrencyPair> getAllCurrencyPairs() {
        return tCurrencyPairMapper.selectList(null);
    }

    public List<String> findCcyPair() {
        return tCurrencyPairMapper.findCcyPair();
    }

    public List<CcyPairs> allPair() {
        List<String> ccyPairs = tCurrencyPairMapper.findCcyPair();
        List<CcyPairs> result = new ArrayList<>();
        for (String ccyPair : ccyPairs) {
            String[] split = ccyPair.split("/");
            result.add(new CcyPairs(ccyPair, split[0], split[1]));
            result.add(new CcyPairs(ccyPair, split[1], split[0]));
        }
        return result;
    }

    public List<GlobalDictDto> findCcyPairs() {
        return tCurrencyPairMapper.findCcyPairs();
    }

    public Boolean updateTCurrencyPair(TCurrencyPair tCurrencyPair) {
        try {
            UpdateWrapper<TCurrencyPair> tCurrencyPairUpdateWrapper = new UpdateWrapper<>();
            tCurrencyPairUpdateWrapper.eq("ccy_pair", tCurrencyPair.getCcyPair().toUpperCase());
            tCurrencyPairMapper.update(tCurrencyPair, tCurrencyPairUpdateWrapper);
            return true;
        } catch (Exception e) {
            throw new CurrencyPairUpdateException(APIErrorConstant.CurrencyPairUpdateExceptionMessage, APIErrorConstant.CurrencyPairUpdateExceptionCode);
        }
    }

    public Boolean calNoFlyZone(String ccyPair, String orderType, BigDecimal price, String direction) {
        boolean flag = false;
        String capitalCcyPair = ccyPair.toUpperCase();
        //get the tCurrencyPairVo
        RealTimePriceVo realTimePriceVo = TradeCache.getRealTimePriceVo(capitalCcyPair);
        if (realTimePriceVo == null) {
            throw new NoRealTimeQuotationException(APIErrorConstant.NoRealTimeQuotationExceptionMessage, APIErrorConstant.NoRealTimeQuotationExceptionCode);
        }
        BigDecimal bid = realTimePriceVo.getBidRate();
        BigDecimal ask = realTimePriceVo.getAskRate();
        //get TCurrencyPair noFlyZonePips
        QueryWrapper<TCurrencyPair> tCurrencyPairQueryWrapper = new QueryWrapper<>();
        tCurrencyPairQueryWrapper.eq("ccy_pair", capitalCcyPair);
        Integer noFlyZonePips = tCurrencyPairMapper.selectOne(tCurrencyPairQueryWrapper).getNoFlyZonePips();
        //get TCurrency minorUnits
        String[] ccys = capitalCcyPair.split("[/]");
        String ccy1 = ccys[0];
        QueryWrapper<TCurrency> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", ccy1);
        Integer minorUnits = tCurrencyMapper.selectOne(queryWrapper).getMinorUnits();
        //calculate pip
        BigDecimal pip = BigDecimal.valueOf(noFlyZonePips * Math.pow(10, -minorUnits));
        if ("call".equalsIgnoreCase(orderType)) {
            //call no fly zone
            BigDecimal midCallNfz = bid.add(ask).divide(new BigDecimal("2"), minorUnits, RoundingMode.HALF_UP);
            BigDecimal maxCallNfz = midCallNfz.add(pip);
            BigDecimal minCallNfz = midCallNfz.subtract(pip);
            flag = price.compareTo(minCallNfz) > -1 && price.compareTo(maxCallNfz) < 1;
        }
        if ("limit".equalsIgnoreCase(orderType)) {
            if (DIRECTION_SELL.equals(direction)) {
                //limit bid no flay zone
                BigDecimal addBid = bid.add(pip);
                BigDecimal subBid = bid.subtract(pip);
                flag = price.compareTo(subBid) > -1 && price.compareTo(addBid) < 1;
            } else if (DIRECTION_BUY.equals(direction)) {
                //limit sak no fly zone
                BigDecimal addAsk = ask.add(pip);
                BigDecimal subAsk = ask.subtract(pip);
                flag = price.compareTo(subAsk) > -1 && price.compareTo(addAsk) < 1;
            }
        }
        return flag;
    }
}
