package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TEntity;
import com.ocbc.oms.app.model.dto.EntityDto;
import com.ocbc.oms.app.repository.TEntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityService {
    private final TEntityMapper tEntityMapper;
    public EntityService(TEntityMapper tEntityMapper) {
        this.tEntityMapper = tEntityMapper;
    }

    public List<TEntity> getAllEntities() { return tEntityMapper.selectList(null); }

    public List<EntityDto> findEntity() {
        return tEntityMapper.findEntity();
    }
}
