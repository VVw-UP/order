package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.dto.GlobalDictDto;
import com.ocbc.oms.app.repository.GlobalDictMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 全局字典表 服务实现类
 * </p>
 *
 * @author Hzy
 * @since 2021-10-21
 */
@Service
@AllArgsConstructor
public class GlobalDictService {

    private final GlobalDictMapper globalDictMapper;

    
    public List<GlobalDictDto> findSegmentType() {
        return globalDictMapper.findSegmentType();
    }

    
    public List<GlobalDictDto> findMurexBookType() {
        return globalDictMapper.findMurexBookType();
    }
}
