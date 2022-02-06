package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TTradeStatus {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
}
