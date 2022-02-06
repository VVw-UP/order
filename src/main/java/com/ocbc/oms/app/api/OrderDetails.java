package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
public class OrderDetails extends CFSOrderDetails {
    private Boolean allowPartialFill=false;
    private Boolean callRequired=false;


    @Builder(builderMethodName = "orderDetailsBuilder")
    public OrderDetails(String ccyPair, String dealtccy, String direction, BigDecimal price, Boolean allowPartialFill, LocalDateTime expireTimestamp, Boolean callRequired, BigDecimal dealtAmount, BigDecimal contraAmount,String longCustomerName) {
        super(ccyPair, dealtccy, direction, price, expireTimestamp, dealtAmount, contraAmount,longCustomerName);
        this.allowPartialFill = allowPartialFill;
        this.callRequired = callRequired;
    }
}
