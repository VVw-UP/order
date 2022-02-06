package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.oms.app.model.dto.TraderSpreadDto;
import com.ocbc.oms.app.consts.DataBaseConst;
import com.ocbc.oms.app.repository.TraderSpreadMapper;
import com.ocbc.oms.app.model.TraderSpread;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

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
public class TraderSpreadService {

    private final TraderSpreadMapper traderSpreadMapper;

    public List<TraderSpread> find(Integer userId) {
        QueryWrapper<TraderSpread> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataBaseConst.USER_ID, userId);
        return traderSpreadMapper.selectList(queryWrapper);
    }


    public Boolean insert(TraderSpreadDto traderSpreadDto) {
        if (StringUtils.isEmpty(traderSpreadDto.getCcyPar())) {
            return false;
        }
        TraderSpread target = new TraderSpread();
        BeanUtils.copyProperties(traderSpreadDto, target);
        return traderSpreadMapper.insert(target) == 1;
    }


    public Boolean updateSpread(TraderSpreadDto traderSpreadDto) {
        TraderSpread target = new TraderSpread();
        BeanUtils.copyProperties(traderSpreadDto, target);
        return traderSpreadMapper.updateById(target) == 1;
    }


    public Boolean deleteSpread(Integer id) {
        return traderSpreadMapper.deleteById(id) == 1;
    }


    public boolean selectCcy(String ccyPar, Integer userId, Integer id) {
        if (StringUtils.isEmpty(ccyPar) || StringUtils.isEmpty(userId)) {
            return true;
        }
        QueryWrapper<TraderSpread> wrapper = new QueryWrapper<>();
        wrapper.eq(DataBaseConst.TRADER_CCY_PAR, ccyPar);
        wrapper.eq(DataBaseConst.USER_ID, userId);
        if (!StringUtils.isEmpty(id)) {
            wrapper.ne(DataBaseConst.ID, id);
        }
        return !CollectionUtils.isEmpty(traderSpreadMapper.selectList(wrapper));
    }

}
