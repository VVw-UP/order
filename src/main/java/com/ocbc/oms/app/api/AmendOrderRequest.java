package com.ocbc.oms.app.api;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AmendOrderRequest {
    @Min(value = 0, message = "orderId cannot be negative")
    @NotNull(message = "orderId is not null")
    private Long orderId;
    @DecimalMin(value = "0", message = "price cannot be negative")
    private BigDecimal price;
    @DecimalMin(value = "0", message = "dealAmt cannot be negative")
    private BigDecimal dealAmt;
    @DecimalMin(value = "0", message = "contraAmt cannot be negative")
    private BigDecimal contraAmt;
    private LocalDateTime expireTimestamp;
}
