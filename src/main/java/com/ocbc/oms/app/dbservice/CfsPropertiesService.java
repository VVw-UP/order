package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.oms.app.model.TCfsProperties;
import com.ocbc.oms.app.repository.TCfsPropertiesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CfsPropertiesService {
    private final TCfsPropertiesMapper tCfsPropertiesMapper;
    public CfsPropertiesService(TCfsPropertiesMapper tCfsPropertiesMapper) {this.tCfsPropertiesMapper=tCfsPropertiesMapper;}

    public int addCfsProperties(TCfsProperties tCfsProperties) { return tCfsPropertiesMapper.insert(tCfsProperties);}

    public List<TCfsProperties> getCfsPropertiesByOrderId(Long orderId) {
        QueryWrapper<TCfsProperties> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return tCfsPropertiesMapper.selectList(queryWrapper);
    }

    public List<TCfsProperties> getCfsPropertiesByCifNumber(String cifNumber) {
        QueryWrapper<TCfsProperties> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cif_number", cifNumber);
        return tCfsPropertiesMapper.selectList(queryWrapper);
    }

    public List<TCfsProperties> getAllCfsProperties() {
        return tCfsPropertiesMapper.selectList(null);
    }
}
