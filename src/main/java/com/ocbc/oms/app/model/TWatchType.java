package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TWatchType {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String code;
}
