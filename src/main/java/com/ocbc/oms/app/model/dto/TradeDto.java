package com.ocbc.oms.app.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author huijian.zhang
 * @date 2021.11.18 14:13
 */
@Data
@ToString
public class TradeDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private BigDecimal price;
    private BigDecimal watchPrice;
    private Long orderId;
    private Integer orderTypeId;
    private Integer tradeStatusId;
    private Integer directionId;
    private String ccyPair;
}
