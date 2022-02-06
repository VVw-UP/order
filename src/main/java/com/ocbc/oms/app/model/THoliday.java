package com.ocbc.oms.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

import static com.ocbc.oms.app.consts.CommonConstants.GMT_8_TIME_ZONE;
import static com.ocbc.oms.app.consts.CommonConstants.YYYY_MM_DD_HH_MM_SS;


public class THoliday {
    private BigDecimal id;

    private BigDecimal ccyId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYY_MM_DD_HH_MM_SS, timezone = GMT_8_TIME_ZONE)
    private Date holidayDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYY_MM_DD_HH_MM_SS, timezone = GMT_8_TIME_ZONE)
    private Date createTimestamp;

    private BigDecimal createBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYY_MM_DD_HH_MM_SS, timezone = GMT_8_TIME_ZONE)
    private Date lastModifyTimestamp;

    private BigDecimal lastModifyBy;

    private String enable;

    private String ccy;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getCcyId() {
        return ccyId;
    }

    public void setCcyId(BigDecimal ccyId) {
        this.ccyId = ccyId;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public BigDecimal getCreateBy() {
        return createBy;
    }

    public void setCreateBy(BigDecimal createBy) {
        this.createBy = createBy;
    }

    public Date getLastModifyTimestamp() {
        return lastModifyTimestamp;
    }

    public void setLastModifyTimestamp(Date lastModifyTimestamp) {
        this.lastModifyTimestamp = lastModifyTimestamp;
    }

    public BigDecimal getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(BigDecimal lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }

    public String getCcy() {
        return ccy;
    }
}