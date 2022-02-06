package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TOrderStatus;
import com.ocbc.oms.app.repository.TOrderStatusMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderStatusService {
    private final TOrderStatusMapper tOrderStatusMapper;

    public OrderStatusService(TOrderStatusMapper tOrderStatusMapper) {
        this.tOrderStatusMapper = tOrderStatusMapper;
    }

    public List<TOrderStatus> getAllOrderStatus() {
        return tOrderStatusMapper.selectList(null);
    }
}
