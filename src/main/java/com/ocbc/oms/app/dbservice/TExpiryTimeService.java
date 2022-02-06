package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.error.api.ExpiryTimeException;
import com.ocbc.oms.app.model.TExpiryTime;
import com.ocbc.oms.app.model.TOrderType;
import com.ocbc.oms.app.model.dto.TExpiryTimeVo;
import com.ocbc.oms.app.repository.TExpiryTimeMapper;
import com.ocbc.oms.app.repository.TOrderTypeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TExpiryTimeService {
    private final TExpiryTimeMapper tExpiryTimeMapper;

    private final TOrderTypeMapper tOrderTypeMapper;

    public TExpiryTimeService(TExpiryTimeMapper tExpiryTimeMapper, TOrderTypeMapper tOrderTypeMapper) {
        this.tExpiryTimeMapper = tExpiryTimeMapper;
        this.tOrderTypeMapper = tOrderTypeMapper;
    }

    public Boolean insertExpiryTime(TExpiryTimeVo tExpiryTimeVo) {
        try {
            TExpiryTime tExpiryTime = new TExpiryTime();
            BeanUtils.copyProperties(tExpiryTimeVo, tExpiryTime);
            return tExpiryTime.insertOrUpdate();
        } catch (Exception e) {
            throw new ExpiryTimeException(APIErrorConstant.ExpiryTimeInsertOrUpdateExceptionMessage, APIErrorConstant.ExpiryTimeInsertOrUpdateException);
        }
    }

    public List<TExpiryTimeVo> findExpiryTimes() {
        List<TExpiryTimeVo> tExpiryTimeVos = new ArrayList<>();
        List<TOrderType> tOrderTypes = tOrderTypeMapper.selectList(null);
        List<TExpiryTime> expiryTimeList = tExpiryTimeMapper.selectList(null);
        Map<Integer, TExpiryTime> tExpiryTimeMap = expiryTimeList.stream().collect(Collectors.toMap(TExpiryTime::getOrderTypeId, e -> e, (t1, t2) -> t1));
        for (TOrderType tOrderType : tOrderTypes) {
            TExpiryTimeVo tExpiryTimeVo = new TExpiryTimeVo();
            tExpiryTimeVo.setOrderTypeId(tOrderType.getId());
            tExpiryTimeVo.setOrderTypeName(tOrderType.getName());
            TExpiryTime tExpiryTime = tExpiryTimeMap.get(tOrderType.getId());
            if (tExpiryTime == null) {
                tExpiryTime = new TExpiryTime();
                tExpiryTime.setOrderTypeId(tOrderType.getId());
                tExpiryTime.setDays(0);
                tExpiryTime.setHours(0);
                tExpiryTime.setMinutes(0);
            }
            BeanUtils.copyProperties(tExpiryTime, tExpiryTimeVo);
            //add to list
            tExpiryTimeVos.add(tExpiryTimeVo);
        }
        return tExpiryTimeVos;
    }
}
