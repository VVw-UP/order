package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TDirection;
import com.ocbc.oms.app.repository.TDirectionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectionService {
    private final TDirectionMapper tDirectionMapper;
    public DirectionService(TDirectionMapper tDirectionMapper) {
        this.tDirectionMapper = tDirectionMapper;
    }

    public List<TDirection> getAllDirections() {
        return tDirectionMapper.selectList(null);
    }
}
