package com.ocbc.oms.app.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CFSOrderExpiry {
    private String orderType;
    private Integer expiryInDays;
    private Integer expiryInHours;
    private Integer expiryInMinutes;
}
