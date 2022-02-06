package com.ocbc.oms.app.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpreadDelDto implements Serializable {

    private String ccy;
    private Integer userId;
}
