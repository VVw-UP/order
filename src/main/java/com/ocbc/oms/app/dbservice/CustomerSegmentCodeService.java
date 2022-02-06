package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TCustomerSegmentCode;
import com.ocbc.oms.app.repository.TCustomerSegmentCodeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerSegmentCodeService {
    private final TCustomerSegmentCodeMapper tCustomerSegmentCodeMapper;
    public CustomerSegmentCodeService(TCustomerSegmentCodeMapper tCustomerSegmentCodeMapper) {
        this.tCustomerSegmentCodeMapper=tCustomerSegmentCodeMapper;
    }

    public List<TCustomerSegmentCode> getAllCustomerSegmentCodes() {
        return tCustomerSegmentCodeMapper.selectList(null);
    }
}
