package com.ocbc.oms.app.managers;


import com.ocbc.oms.app.dbservice.TimeInForceService;
import com.ocbc.oms.app.dbservice.TimezoneService;
import com.ocbc.oms.app.model.TTimeInForce;
import com.ocbc.oms.app.model.TTimezone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@DependsOn("flywayInitializer")
public class TimeManager {
    private final TimeInForceService timeInForceService;
    private Map<String, TTimeInForce> timeInForceMap;

    private final TimezoneService timezoneService;
    private Map<String, TTimezone> timezoneMap;

    public TimeManager(TimeInForceService timeInForceService,
                       TimezoneService timezoneService) {
        this.timeInForceService = timeInForceService;
        this.timezoneService = timezoneService;
    }

    private void initializeTimeInForceMap() {
        timeInForceMap = new HashMap<>();
        List<TTimeInForce> fromDatabase = timeInForceService.getAllTimeInForce();
        fromDatabase.forEach(timeInForce ->{
            log.debug("Adding timeInForce:{}", timeInForce);
            timeInForceMap.put(timeInForce.getCode(), timeInForce);
        });
    }

    private void initializeTimezoneMap() {
        timezoneMap = new HashMap<>();
        List<TTimezone> fromDatabase = timezoneService.getAllTimezones();
        fromDatabase.forEach(timezone ->{
            log.debug("Adding timezone:{}", timezone);
            timezoneMap.put(timezone.getCode(), timezone);
        });
    }

    @PostConstruct
    private void initialize() {
        initializeTimeInForceMap();
        initializeTimezoneMap();
    }
}
