package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TProductType;
import com.ocbc.oms.app.repository.TProductTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService {
    private final TProductTypeMapper tProductTypeMapper;
    public ProductTypeService(TProductTypeMapper tProductTypeMapper) {
        this.tProductTypeMapper = tProductTypeMapper;
    }

    public List<TProductType> getAllProductTypes() {
        return tProductTypeMapper.selectList(null);
    }
}
