package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
public class CFSOrderDetails {
    private String longCustomerName;
    private String ccyPair;
    private String dealtccy;
    private String direction;
    @DecimalMin(value = "0", message = "price cannot be less than 0")
    private BigDecimal price;
    private LocalDateTime expireTimestamp;
    @DecimalMin(value = "0", message = "dealtAmount cannot be less than 0")
    private BigDecimal dealtAmount;
    @DecimalMin(value = "0", message = "contraAmount cannot be less than 0")
    private BigDecimal contraAmount;

    @Builder(builderMethodName = "cfsOrderDetailsBuilder")
    public CFSOrderDetails(String ccyPair, String dealtccy, String direction, BigDecimal price, LocalDateTime expireTimestamp, BigDecimal dealtAmount, BigDecimal contraAmount, String longCustomerName) {
        this.ccyPair = ccyPair;
        this.dealtccy = dealtccy;
        this.direction = direction;
        this.price = price;
        this.expireTimestamp = expireTimestamp;
        this.dealtAmount = dealtAmount;
        this.contraAmount = contraAmount;
        this.longCustomerName = longCustomerName;
    }
}
