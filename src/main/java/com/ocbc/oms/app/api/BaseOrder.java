package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseOrder {
    private String productType;
    private String orderType;
    private String client;
    private String bosSale;
    private String channel;

    @Builder(builderMethodName = "baseOrderBuilder")
    public BaseOrder(String productType, String orderType, String client, String bosSale, String channel) {
        this.productType = productType;
        this.orderType = orderType;
        this.client = client;
        this.bosSale = bosSale;
        this.channel = channel;
    }
}
