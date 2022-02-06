package com.ocbc.oms.app.model;

import lombok.Data;

@Data
public class BatchCancel {
    private Long tradeId;
    private Long orderId;
}
