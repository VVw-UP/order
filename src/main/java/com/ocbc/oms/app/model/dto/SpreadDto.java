package com.ocbc.oms.app.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Hzy
 * @since 2021-10-20
 */
@Data
public class SpreadDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String spreadCcyPar;
    private Integer spreadFrom;
    private Integer spreadTo;
    private BigDecimal spreadPoint;
    private Integer userId;

}
