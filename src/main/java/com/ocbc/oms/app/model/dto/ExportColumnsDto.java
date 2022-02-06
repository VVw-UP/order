package com.ocbc.oms.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzy
 * @since 2021-12-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportColumnsDto {

    private String key;

    private String value;
}
