package com.ocbc.oms.app.api;

import lombok.Data;

@Data
public class BatchCancelRequest {
    private Long actionId;
    private String actionCode;
    private Long[] bulkEditValueKeys;
}
