package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TTimezone;
import com.ocbc.oms.app.repository.TTimezoneMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimezoneService {
    private final TTimezoneMapper tTimezoneMapper;
    public TimezoneService(TTimezoneMapper tTimezoneMapper) {
        this.tTimezoneMapper = tTimezoneMapper;
    }

    public List<TTimezone> getAllTimezones() {
        return tTimezoneMapper.selectList(null);
    }
}
