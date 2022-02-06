package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class TCounterparty {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String code;
    private String fullName;
    private String mxLabel;
    private String mxName;
    private String mxCif;
    private String mxStatus;
    private String mxClsM;
    private String comments;
    private Long counterpartyTypeId;
    private Long counterpartyCategoryId;
    private Integer entityId;
    private Date createTimestamp;
    private Long createBy;
    private Date lastModifyTimestamp;
    private Long lastModifyBy;
    private String enable;
    private String skipsales;
    private String skipecom;
    private String ecomid;
    private Long ecomentity;
}
