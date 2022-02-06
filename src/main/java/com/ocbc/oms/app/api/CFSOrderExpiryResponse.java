package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CFSOrderExpiryResponse {
    private List<CFSOrderExpiry> results;
}