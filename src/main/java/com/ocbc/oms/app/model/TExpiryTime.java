package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class TExpiryTime extends Model<TExpiryTime> {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderTypeId;
    private Integer days;
    private Integer hours;
    private Integer minutes;
    private Date createTimestamp;
}
