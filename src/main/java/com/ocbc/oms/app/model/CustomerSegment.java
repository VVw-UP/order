package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
@TableName("customer_segment")
public class CustomerSegment extends Model<CustomerSegment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("customer_name")
    private String customerName;

    @TableField("entity_id")
    private Integer entityId;

    @TableField("segment_code")
    private String segmentCode;

    @TableField("segment_description")
    private String segmentDescription;

    @TableField("segment_type_id")
    private Integer segmentTypeId;

    @TableField("sales_id")
    private String salesId;

    @TableField("code_mx")
    private String codeMx;

    @TableField("user_id")
    private Integer userId;

    @TableField("create_timestamp")
    private LocalDateTime createTimestamp;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
