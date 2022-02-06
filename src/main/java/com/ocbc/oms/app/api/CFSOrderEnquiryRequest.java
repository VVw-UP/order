package com.ocbc.oms.app.api;

import lombok.Data;

import java.util.List;

@Data
public class CFSOrderEnquiryRequest {
    private List<SearchParam> params;
}
