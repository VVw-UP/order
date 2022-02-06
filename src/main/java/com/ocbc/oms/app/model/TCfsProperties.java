package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@Data
public class TCfsProperties {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long orderId;
    private String cifNumber;
    private Integer customerSegId;
    private String channelCode;
    private String productCode;
    private BigDecimal allInPrice;
    private Date fillTimestamp;
}
