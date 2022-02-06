package com.ocbc.oms.app.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EntityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer value;
    private String label;
}
