package com.ocbc.oms.app.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocbc.oms.app.model.TEntity;
import com.ocbc.oms.app.model.dto.EntityDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TEntityMapper extends BaseMapper<TEntity> {

    @Select("select id as value,name as label from t_entity where enable = '1'")
    List<EntityDto> findEntity();
}
