package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TCurrency;
import com.ocbc.oms.app.repository.TCurrencyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    private  final TCurrencyMapper tCurrencyMapper;
    public CurrencyService(TCurrencyMapper tCurrencyMapper) {
        this.tCurrencyMapper = tCurrencyMapper;
    }

    public List<TCurrency> getAllCurrencies() {
        return tCurrencyMapper.selectList(null);
    }

    public TCurrency getCurrencyById(Integer id){
        return tCurrencyMapper.selectById(id);
    }
}
