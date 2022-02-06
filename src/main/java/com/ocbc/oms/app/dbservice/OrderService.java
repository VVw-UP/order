package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ocbc.oms.app.model.PageEntity;
import com.ocbc.oms.app.model.TOrder;
import com.ocbc.oms.app.repository.TOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class OrderService {

    public final TOrderMapper tOrderMapper;

    public OrderService(TOrderMapper tOrderMapper) {
        this.tOrderMapper = tOrderMapper;
    }

    public List<TOrder> getAllOrders(PageEntity pageEntity) {
        Page<TOrder> tOrderPage = new Page<>(pageEntity.getPageNum(), pageEntity.getPageSize());
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.orderBy(true, false, "id");
        IPage<TOrder> iPage = tOrderMapper.selectPage(tOrderPage, wrapper);
        return iPage.getRecords();
    }

    public List<TOrder> getAllOrdersByIds(List<Integer> ids) {
        return tOrderMapper.selectBatchIds(ids);
    }

    public List<TOrder> getOrderByUniqId(String id) {
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uniq_id", id);
        return tOrderMapper.selectList(queryWrapper);
    }

    public List<TOrder> getOrderById(Long id) {
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return tOrderMapper.selectList(queryWrapper);
    }

    public int addOrder(TOrder order) {
        return tOrderMapper.insert(order);
    }

    public int queryOrderCount() {
        return tOrderMapper.selectList(null).size();
    }
}
