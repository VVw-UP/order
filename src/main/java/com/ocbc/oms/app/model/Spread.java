package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hzy
 * @since 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("spread")
public class Spread extends Model<Spread> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("ccy_par")
    private String ccyPar;

    @TableField("spread_from")
    private Integer spreadFrom;

    @TableField("spread_to")
    private Integer spreadTo;

    @TableField("spread_point")
    private BigDecimal spreadPoint;

    @TableField("user_id")
    private Integer userId;

    @TableField("create_timestamp")
    private LocalDateTime createTimestamp;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
