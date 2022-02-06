package com.ocbc.oms.app.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResponse {
    private Integer orderId;
    private String productType;
    private String orderType;
    private String cfsRefNumber;
    private String client;
    private String channel;
    private String ccyPair;
    private String dealtccy;
    private String direction;
    private BigDecimal price;
    private BigDecimal dealtAmount;
    private BigDecimal contraAmount;
    private String cifNumber;
    private String status;
    private LocalDateTime createTimestamp;
    private LocalDateTime expireTimestamp;
    private LocalDateTime fillTimestamp;
    private String longCustomerName;
    private BigDecimal watchPrice;
}
