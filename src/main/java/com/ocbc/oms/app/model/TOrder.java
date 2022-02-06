package com.ocbc.oms.app.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class TOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String uniqId;
    private Integer productTypeId;
    private Integer orderStatusId;
    private Integer orderTypeId;
    private Integer counterpartyId;
    private Integer clientId;
    //ID of user who created the order
    private Integer createBy;
    private LocalDateTime createTimestamp;
    private Integer lastModifyBy;
    private LocalDateTime lastModifyTimestamp;
    private Integer onBehalfOf;
    private Integer bosSaleId;
    private Integer channelId;
    private String cfsRefNumber;
}
