package com.ocbc.oms.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应dto
 *
 * @param <T>
 * @author hzy
 * @since 2021-06-08
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer status;

    private String message;

    private Boolean data;

}
