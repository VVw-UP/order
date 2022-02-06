package com.ocbc.oms.app.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 报价类
 * @Author zhenMing.pan
 * @Date 2021/11/18 13:44
 * @Version V1.0.0
 **/
@Data
public class FxDataPrice {

    private String id;
    private String ccyPair;
    private String tenor;
    private BigDecimal bidRate;
    private BigDecimal askRate;
    private String mdEntryId;
    // bid volume
    private BigDecimal mdEntrySizeBid;
    // ask volume
    private BigDecimal mdEntrySizeAsk;
    // bid price date
    private BigDecimal mdEntryDateBid;
    // ask price date
    private BigDecimal mdEntryDateAsk;
    // source id
    private Integer rateSourceId;
}
