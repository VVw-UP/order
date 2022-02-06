package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TTimeInForce;
import com.ocbc.oms.app.repository.TTimeInForceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeInForceService {
    private final TTimeInForceMapper tTimeInForceMapper;
    public TimeInForceService(TTimeInForceMapper tTimeInForceMapper) {
        this.tTimeInForceMapper = tTimeInForceMapper;
    }

    public List<TTimeInForce> getAllTimeInForce() {
        return tTimeInForceMapper.selectList(null);
    }
}
