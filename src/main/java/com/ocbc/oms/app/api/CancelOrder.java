package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelOrder {
    private Long orderId;
    private String reason;
    private String result;
}
