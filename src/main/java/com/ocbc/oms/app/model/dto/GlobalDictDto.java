package com.ocbc.oms.app.model.dto;

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
public class GlobalDictDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer value;
    private String label;

}
