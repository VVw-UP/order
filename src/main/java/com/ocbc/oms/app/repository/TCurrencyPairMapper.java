package com.ocbc.oms.app.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocbc.oms.app.model.TCurrencyPair;
import com.ocbc.oms.app.model.dto.GlobalDictDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TCurrencyPairMapper extends BaseMapper<TCurrencyPair> {

    @Select("select ccy_pair from t_currency_pair where enable = '1'")
    List<String> findCcyPair();

    @Select("select id as value,ccy_pair as label from t_currency_pair where enable = '1'")
    List<GlobalDictDto> findCcyPairs();
}
