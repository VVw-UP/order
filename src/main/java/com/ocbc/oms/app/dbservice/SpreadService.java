package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ocbc.oms.app.consts.DataBaseConst;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.error.api.SpreadInsertException;
import com.ocbc.oms.app.model.Spread;
import com.ocbc.oms.app.model.dto.SpreadDelDto;
import com.ocbc.oms.app.model.dto.SpreadVo;
import com.ocbc.oms.app.repository.SpreadMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hzy
 * @since 2021-10-20
 */
@Service
@AllArgsConstructor
public class SpreadService {

    private final SpreadMapper spreadMapper;


    public List<SpreadVo> find(Integer userId) {
        QueryWrapper<Spread> wrapper = new QueryWrapper<>();
        wrapper.eq(DataBaseConst.USER_ID, userId);
        Map<String, List<Spread>> map = spreadMapper.selectList(wrapper).stream().collect(Collectors.groupingBy(Spread::getCcyPar));
        List<SpreadVo> spreadVos = new ArrayList<>();
        map.forEach((key, val) -> spreadVos.add(new SpreadVo(key, userId, val)));
        return spreadVos;
    }

    @Transactional(rollbackFor = SpreadInsertException.class)
    public Boolean insert(SpreadVo spreadVo) {
        try {
            for (Spread value : spreadVo.getValues()) {
                spreadMapper.insert(value);
            }
            return true;
        } catch (Exception e) {
            throw new SpreadInsertException(e.getMessage() + "," + APIErrorConstant.SpreadInsertOrUpdateExceptionMessage);
        }
    }

    @Transactional(rollbackFor = SpreadInsertException.class)
    public Boolean updateSpread(SpreadVo spreadVo) {
        try {
            for (Spread value : spreadVo.getValues()) {
                spreadMapper.updateById(value);
            }
            return true;
        } catch (Exception e) {
            throw new SpreadInsertException(e.getMessage() + "," + APIErrorConstant.SpreadInsertOrUpdateExceptionMessage);
        }
    }


    public Boolean deleteSpread(SpreadDelDto spreadDelDto) {
        UpdateWrapper<Spread> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(DataBaseConst.TRADER_CCY_PAR, spreadDelDto.getCcy());
        updateWrapper.eq(DataBaseConst.USER_ID, spreadDelDto.getUserId());
        return spreadMapper.delete(updateWrapper) > 0;
    }

    public boolean selectCcy(SpreadVo spreadVo) {
        String ccyPar = spreadVo.getCcy();
        Integer userId = spreadVo.getUserId();
        List<Spread> values = spreadVo.getValues();
        if (StringUtils.isEmpty(ccyPar) || StringUtils.isEmpty(userId) || CollectionUtils.isEmpty(values)) {
            return true;
        }
        List<Integer> ids = values.stream().filter(e -> !StringUtils.isEmpty(e.getId()))
                .map(Spread::getId).collect(Collectors.toList());
        QueryWrapper<Spread> wrapper = new QueryWrapper<>();
        wrapper.eq(DataBaseConst.TRADER_CCY_PAR, ccyPar);
        wrapper.eq(DataBaseConst.USER_ID, userId);
        //id not null == update
        if (!CollectionUtils.isEmpty(ids)) {
            wrapper.notIn(DataBaseConst.ID, ids);
        }
        return !CollectionUtils.isEmpty(spreadMapper.selectList(wrapper));
    }
}
