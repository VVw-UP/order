package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TOrderType;
import com.ocbc.oms.app.repository.TOrderTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTypeService {
    private final TOrderTypeMapper tOrderTypeMapper;

    public OrderTypeService(TOrderTypeMapper tOrderTypeMapper) {
        this.tOrderTypeMapper = tOrderTypeMapper;
    }

    public List<TOrderType> getAllOrderTypes() {
        return tOrderTypeMapper.selectList(null);
    }
}
