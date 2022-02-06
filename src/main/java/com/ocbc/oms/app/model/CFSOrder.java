package com.ocbc.oms.app.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
public class CFSOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer rowNum;
    private Long orderId;
    private String uniqId;
    private Integer productTypeId;
    private String productTypeCode;
    private Integer orderStatusId;
    private String orderStatusCode;
    private Integer orderTypeId;
    private String orderTypeCode;
    private Integer counterpartyId;
    private Integer clientId;
    private Integer onBehalfOf;
    private Integer bosSaleId;
    private Integer channelId;

    private Long id;//tradeId
    private Integer tradeStatusId;
    private String tradeStatusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDateTime settlementDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDateTime tradeDate;
    private Integer watchTypeId;
    private Integer ccyPairId;
    private String ccyPair;
    private Integer dealtCcyId;
    private Integer directionId;
    private BigDecimal price;
    private Boolean allowPartialFill=false;
    private Integer timeInForceId;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTimestamp;
    private Integer expireTimezoneId;
    private Boolean callRequired=false;
    private BigDecimal dealtAmt;
    private String dealAmtStr;
    private BigDecimal contraAmt;
    private String contraAmtStr;
    private Integer createBy;
    private String createByName;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimestamp;
    private Integer lastModifyBy;
    private String lastModifyByName;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifyTimestamp;
    private String cfsRefNumber;
    /**tips for front*/
    private String message;
    private String longCustomerName;
    private String watchPrice;
    private String dealCurrency;
    private String dealCurrencyDirection;
    private String marketRate;

    public String getDealAmtStr() {
        //保留两位
        DecimalFormat df = new DecimalFormat("##,##0.00");
        if(this.dealtAmt != null){
            return df.format(dealtAmt);
        }else{
            return "0.00";
        }
    }

    public String getContraAmtStr() {
        //保留两位
        DecimalFormat df = new DecimalFormat("##,##0.00");
        if(this.contraAmt != null){
            return df.format(contraAmt);
        }else{
            return "0.00";
        }
    }
}
