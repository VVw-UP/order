package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("trader_spread")
public class TraderSpread extends Model<TraderSpread> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("ccy_par")
    private String ccyPar;

    @TableField("spread")
    private BigDecimal spread;

    @TableField("murex_booking_type")
    private Integer murexBookingType;

    @TableField("create_timestamp")
    private LocalDateTime createTimestamp;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
