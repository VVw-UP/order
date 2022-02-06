package com.ocbc.oms.app.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RealTimePriceVo {
    private BigDecimal bidRate;
    private BigDecimal askRate;
}
