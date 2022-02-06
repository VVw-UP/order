package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TTradeStatus;
import com.ocbc.oms.app.repository.TTradeStatusMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeStatusService {
    private final TTradeStatusMapper tTradeStatusMapper;
    public TradeStatusService(TTradeStatusMapper tTradeStatusMapper) {
        this.tTradeStatusMapper = tTradeStatusMapper;
    }

    public List<TTradeStatus> getAllTradeStatuses() {
        return tTradeStatusMapper.selectList(null);
    }
}
