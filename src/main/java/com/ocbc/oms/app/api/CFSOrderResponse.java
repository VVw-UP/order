package com.ocbc.oms.app.api;

import com.ocbc.oms.app.model.CFSOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CFSOrderResponse {
    private List<CFSOrder> rows;
    private int count;
    private String aggregates;
    private String quantity;
    private String selectAll;
    private String selectedInventories;
    private String selection;
    private String tradeIds;
    private String tradeStatusIds;
}
