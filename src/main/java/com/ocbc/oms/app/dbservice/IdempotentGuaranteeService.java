package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ocbc.oms.app.consts.DataBaseConst;
import com.ocbc.oms.app.model.TIdempotentGuarantee;
import com.ocbc.oms.app.repository.TIdempotentGuaranteeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author hzy
 * @since 2021-11-16
 */
@Service
@AllArgsConstructor
public class IdempotentGuaranteeService {

    private final TIdempotentGuaranteeMapper idempotentGuaranteeMapper;


    /**
     * 保证定时任务执行的幂等性
     *
     * @param name 执行的组件名称
     * @return 设置结果
     */
    public boolean updateComponentIdempotent(String name, Integer value) {
        LocalDateTime now = LocalDateTime.now();
        UpdateWrapper<TIdempotentGuarantee> wrapper = new UpdateWrapper<>();
        wrapper.eq(DataBaseConst.T_IDEMPOTENT_GUARANTEE_COMPONENT_NAME, name);
        wrapper.ge(DataBaseConst.T_IDEMPOTENT_GUARANTEE_UPDATE_TIME, now.minusSeconds(10));
        //将满足条件的组件value值设置为1,修改成功则获取执行机会
        return idempotentGuaranteeMapper.update(new TIdempotentGuarantee(name, value, now), wrapper) > 0;
    }
}
