package com.ocbc.oms.app.model;

import java.math.BigDecimal;
import java.util.Date;

public class TTradeDateHistory {
    private BigDecimal id;

    private BigDecimal currencyId;

    private Date currenctTradeDate;

    private Date lastModifyTimestamp;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public void setCurrencyId(BigDecimal currencyId) {
        this.currencyId = currencyId;
    }

    public void setCurrenctTradeDate(Date currenctTradeDate) {
        this.currenctTradeDate = currenctTradeDate;
    }

    public Date getLastModifyTimestamp() {
        return lastModifyTimestamp;
    }

    public void setLastModifyTimestamp(Date lastModifyTimestamp) {
        this.lastModifyTimestamp = lastModifyTimestamp;
    }
}