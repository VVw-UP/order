package com.ocbc.oms.app.api;

import com.ocbc.oms.app.model.CFSOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BulKeDitResponse {
    private Long actionId;
    private String actionCode;
    private BigDecimal fillAmt;
    private BigDecimal fillPrice;
    private Long distributeId;
    private Long ownerId;
    private List<Object> assignRiskManagers;
    private List<CFSOrder> bulkEditValueKeys;
    private List<Object> sendCreditTrade;
}
