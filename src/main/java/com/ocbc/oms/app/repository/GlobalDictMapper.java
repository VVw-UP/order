package com.ocbc.oms.app.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocbc.oms.app.model.GlobalDict;
import com.ocbc.oms.app.model.dto.GlobalDictDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 全局字典表 Mapper 接口
 * </p>
 *
 * @author Hzy
 * @since 2021-10-21
 */
public interface GlobalDictMapper extends BaseMapper<GlobalDict> {

    @Select("select id as value,name as label from global_dict where p_id = '1'")
    List<GlobalDictDto> findSegmentType();

    @Select("select id as value,name as label from global_dict where p_id = '3'")
    List<GlobalDictDto> findMurexBookType();
}
