package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocbc.oms.app.model.dto.CustomerSegmentDto;
import com.ocbc.oms.app.consts.DataBaseConst;
import com.ocbc.oms.app.repository.CustomerSegmentMapper;
import com.ocbc.oms.app.model.CustomerSegment;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CustomerSegmentService {

    private final CustomerSegmentMapper customerSegmentMapper;

    public Boolean insert(CustomerSegmentDto customerSegmentDto) {
        CustomerSegment target = new CustomerSegment();
        BeanUtils.copyProperties(customerSegmentDto, target);
        return customerSegmentMapper.insert(target) == 1;
    }

    public Boolean updateCustomerSegment(CustomerSegmentDto customerSegmentDto) {
        CustomerSegment target = new CustomerSegment();
        BeanUtils.copyProperties(customerSegmentDto, target);
        return customerSegmentMapper.updateById(target) == 1;
    }

    public Boolean deleteCustomerSegment(Integer id) {
        return customerSegmentMapper.deleteById(id) == 1;
    }

    public List<CustomerSegment> find(Integer userId) {
        QueryWrapper<CustomerSegment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataBaseConst.USER_ID, userId);
        return customerSegmentMapper.selectList(queryWrapper);
    }
}
