package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class CFSOrderEnquiryResponse {
    private List<SearchResponse> searchResultList;
}
