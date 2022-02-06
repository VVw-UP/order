package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Integer orderId;
    private String cifNumber;
}
