package com.ocbc.oms.app.managers;

import com.ocbc.oms.app.dbservice.DirectionService;
import com.ocbc.oms.app.dbservice.WatchTypeService;
import com.ocbc.oms.app.model.TDirection;
import com.ocbc.oms.app.model.TWatchType;
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
public class DirectionWatchTypeManager {
    private final DirectionService directionService;
    private Map<String, TDirection> directionMap;

    private final WatchTypeService watchTypeService;
    private Map<String, TWatchType> watchTypeMap;

    public DirectionWatchTypeManager(DirectionService directionService,
                                     WatchTypeService watchTypeService) {
        this.directionService = directionService;
        this.watchTypeService = watchTypeService;
    }

    private void initializeDirectionMap() {
        directionMap = new HashMap<>();
        List<TDirection> fromDatabase = directionService.getAllDirections();
        fromDatabase.forEach(direction -> {
            log.debug("Adding direction:{}", direction);
            directionMap.put(direction.getCode(), direction);
        });
    }

    public Integer getIdByCode(String code) {
        TDirection tDirection = directionMap.get(code);
        if (tDirection == null) {
            return null;
        }
        return tDirection.getId();
    }

    public String getCodeById(Integer id) {
        for (TDirection value : directionMap.values()) {
            if (value.getId().equals(id)) {
                return value.getCode();
            }
        }
        return null;
    }

    private void initializeWatchTypeMap() {
        watchTypeMap = new HashMap<>();
        List<TWatchType> fromDatabase = watchTypeService.getAllWatchTypes();
        fromDatabase.forEach(watchType -> {
            log.debug("Adding watchType:{}", watchType);
            watchTypeMap.put(watchType.getCode(), watchType);
        });
    }

    @PostConstruct
    private void initialize() {
        initializeDirectionMap();
        initializeWatchTypeMap();
    }
}
