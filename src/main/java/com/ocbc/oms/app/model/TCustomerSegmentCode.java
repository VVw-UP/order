package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TCustomerSegmentCode {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private Integer entityId;
    private String segmentCode;
    private String segmentDescription;
    private String segmentType;
    private Integer codeMx;
    private Integer salesId;

}
