package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class TUser {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    private Boolean enable;
    private String email;
    private Boolean emailNotification;
    private Boolean smsNotification;
    private String mobile;
    private Date createTimestamp;
    private Integer createBy;
    private Date lastModifyTimestamp;
    private Integer lastModifyBy;
    private Long mxOrderownerId;
    private Long mxRiskownerId;
    private Integer entityId;
    private Boolean skipsales;
    private Boolean skipecom;
    private String saleId;
    private String murexName;
    private String murexGroup;
    private String ecomid;
    private Long ecomentity;
}
