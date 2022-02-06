package com.ocbc.oms.app.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 全局字典表
 * </p>
 *
 * @author Hzy
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("global_dict")
public class GlobalDict extends Model<GlobalDict> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("p_id")
    private Integer parentId;

    @TableField("full_path")
    private String fullPath;

    @TableField("create_timestamp")
    private LocalDateTime createTimestamp;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
