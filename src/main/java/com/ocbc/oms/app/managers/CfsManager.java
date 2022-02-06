package com.ocbc.oms.app.managers;

import com.ocbc.oms.app.dbservice.CfsPropertiesService;
import com.ocbc.oms.app.dbservice.CustomerSegmentCodeService;
import com.ocbc.oms.app.error.application.ApplicationErrorConstant;
import com.ocbc.oms.app.error.application.InvalidInputMapException;
import com.ocbc.oms.app.model.TCfsProperties;
import com.ocbc.oms.app.model.TCustomerSegmentCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@DependsOn("flywayInitializer")
@Slf4j
public class CfsManager {
    private final CfsPropertiesService cfsPropertiesService;

    private final CustomerSegmentCodeService customerSegmentCodeService;
    private Map<String, TCustomerSegmentCode> customerSegmentCodeMap;

    public CfsManager(CfsPropertiesService cfsPropertiesService,
                      CustomerSegmentCodeService customerSegmentCodeService) {
        this.cfsPropertiesService=cfsPropertiesService;
        this.customerSegmentCodeService = customerSegmentCodeService;
    }

    public List<TCfsProperties> getCfsPropertiesUsingCIF(String cifNumber) {
        return cfsPropertiesService.getCfsPropertiesByCifNumber(cifNumber);
    }

    public List<TCfsProperties> getCfsPropertiesUsingOrderId(Long orderId) {
        return cfsPropertiesService.getCfsPropertiesByOrderId(orderId);
    }

    public int addCfsProperties(TCfsProperties tCfsProperties) {
        return cfsPropertiesService.addCfsProperties(tCfsProperties);
    }

    public List<TCfsProperties> getAllCfsProperties(){
        return cfsPropertiesService.getAllCfsProperties();
    }

    public int getCustomerSegmentIdFromMap(String customerSegmentCode) {
        if(StringUtils.isNotBlank(customerSegmentCode) && customerSegmentCodeMap.containsKey(customerSegmentCode)) {
            return customerSegmentCodeMap.get(customerSegmentCode).getId();
        }
        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
                .append(" -> customerSegmentCodeMap")
                .toString(),
                ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    private void initializeCustomerSegementCodeMap() {
        customerSegmentCodeMap = new HashMap<>();
        List<TCustomerSegmentCode> fromDatabase = customerSegmentCodeService.getAllCustomerSegmentCodes();
        fromDatabase.forEach(customerSegmentCode ->{
            log.debug("Adding customerSegmentCode:{}", customerSegmentCode);
            customerSegmentCodeMap.put(customerSegmentCode.getSegmentCode(), customerSegmentCode);
        });
    }

    @PostConstruct
    private void initialize() {
        initializeCustomerSegementCodeMap();
    }
}
