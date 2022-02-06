package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class TCurrency {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String code;
    private String name;
    private Integer spotDays;
    private Date createTimestamp;
    private Integer createBy;
    private Date lastModifyTimestamp;
    private Integer lastModifyBy;
    private Boolean enable=true;
    private String rollTime;
    private String timeZone;
    private Integer minorUnits;
}
