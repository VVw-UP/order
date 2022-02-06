package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class TTrade {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Integer tradeStatusId;
    private LocalDateTime settlementDate;
    private LocalDateTime tradeDate;
    private Integer watchTypeId;
    private Integer ccyPairId;
    private Integer dealtCcyId;
    private Integer directionId;
    private BigDecimal price;
    private Boolean allowPartialFill = false;
    private Integer timeInForceId;
    private LocalDateTime expireTimestamp;
    private LocalDateTime fillTimestamp;
    private Integer expireTimezoneId;
    private Boolean callRequired = false;
    private BigDecimal dealtAmt;
    private BigDecimal contraAmt;
    private Integer createBy;
    private LocalDateTime createTimestamp;
    private Integer lastModifyBy;
    private LocalDateTime lastModifyTimestamp;
    private String longCustomerName;
    private BigDecimal watchPrice;
    private Integer dealtCcyIdOrig;
    private Integer directionIdOrig;
    private BigDecimal priceOrig;

    public TTrade(Integer tradeStatusId) {
        this.tradeStatusId = tradeStatusId;
    }

    public TTrade(Integer tradeStatusId, LocalDateTime fillTimestamp) {
        this.tradeStatusId = tradeStatusId;
        this.fillTimestamp = fillTimestamp;
    }
}
