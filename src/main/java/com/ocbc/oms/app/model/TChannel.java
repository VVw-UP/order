package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class TChannel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String code;
    private String name;
    private String enable;
    private Date createTimestamp;
    private Long createBy;
    private Date lastModifyTimestamp;
    private Long lastModifyBy;
}
