package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class TCurrencyPair {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String ccyPair;
    private Integer baseCcyId;
    private Integer termCcyId;
    private BigDecimal spotPips;
    private BigDecimal forwardPips;
    private Integer rateStaleTime;
    private Integer noFlyZonePips;
    private Integer tempPipsVeryHot;
    private Integer tempPipsHot;
    private Integer tempPipsWarm;
    private Integer tempPipsCold;
    private Integer tempPipsInactive;
    private Date createTimestamp;
    private Integer createBy;
    private Date lastModifyTimestamp;
    private Integer lastModifyBy;
    private Boolean enable;
}
