package com.ocbc.oms.app.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class TExpiryTimeVo {
    private Integer id;
    private Integer orderTypeId;
    private String orderTypeName;
    private Integer days;
    private Integer hours;
    private Integer minutes;
    private Date createTimestamp;
}
