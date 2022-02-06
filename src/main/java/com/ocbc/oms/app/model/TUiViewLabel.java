package com.ocbc.oms.app.model;

import java.math.BigDecimal;

public class TUiViewLabel {
    private BigDecimal uiViewId;

    private String uiLabelCode;

    private Long position;

    private Short groupable;

    private String pinned;

    private String headerName;

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public BigDecimal getUiViewId() {
        return uiViewId;
    }

    public void setUiViewId(BigDecimal uiViewId) {
        this.uiViewId = uiViewId;
    }

    public String getUiLabelCode() {
        return uiLabelCode;
    }

    public void setUiLabelCode(String uiLabelCode) {
        this.uiLabelCode = uiLabelCode == null ? null : uiLabelCode.trim();
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Short getGroupable() {
        return groupable;
    }

    public void setGroupable(Short groupable) {
        this.groupable = groupable;
    }

    public String getPinned() {
        return pinned;
    }

    public void setPinned(String pinned) {
        this.pinned = pinned == null ? null : pinned.trim();
    }
}