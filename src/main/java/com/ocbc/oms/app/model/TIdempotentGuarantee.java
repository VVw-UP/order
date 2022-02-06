package com.ocbc.oms.app.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author hzy
 * @since 2021-11-16
 */
@Data
@ToString
@NoArgsConstructor
public class TIdempotentGuarantee {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String componentName;
    private Integer idempotentValue;
    private LocalDateTime updateTime;

    public TIdempotentGuarantee(String componentName, Integer idempotentValue, LocalDateTime dateTime) {
        this.componentName = componentName;
        this.idempotentValue = idempotentValue;
        this.updateTime = dateTime;
    }
}
