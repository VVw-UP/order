package com.ocbc.oms.app.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Hzy
 * @since 2021-10-20
 */
@Data
public class CustomerSegmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String customerName;
    private Integer entityId;
    private String segmentCode;
    private String segmentDescription;
    private Integer segmentTypeId;
    private String salesId;
    private String codeMx;
    private Integer userId;

}
