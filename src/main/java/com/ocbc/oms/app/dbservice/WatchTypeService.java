package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TWatchType;
import com.ocbc.oms.app.repository.TWatchTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchTypeService {
    private final TWatchTypeMapper tWatchTypeMapper;
    public WatchTypeService(TWatchTypeMapper tWatchTypeMapper) {
        this.tWatchTypeMapper = tWatchTypeMapper;

    }

    public List<TWatchType> getAllWatchTypes() {
        return tWatchTypeMapper.selectList(null);
    }
}
