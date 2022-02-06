package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TCounterparty;
import com.ocbc.oms.app.repository.TCounterpartyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounterpartyService {
    private final TCounterpartyMapper tCounterpartyMapper;

    public CounterpartyService(TCounterpartyMapper tCounterpartyMapper) {
        this.tCounterpartyMapper = tCounterpartyMapper;
    }

    public List<TCounterparty> getAllCounterparties() {
        return tCounterpartyMapper.selectList(null);
    }
}
