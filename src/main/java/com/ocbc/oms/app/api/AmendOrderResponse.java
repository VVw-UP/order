package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AmendOrderResponse {
    private Long orderId;
    private String status;
}
