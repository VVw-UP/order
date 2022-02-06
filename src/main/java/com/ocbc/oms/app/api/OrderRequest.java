package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderRequest extends BaseOrder {
    private OrderDetails orderDetails;

    @Builder(builderMethodName = "orderBuilder")
    public OrderRequest(String productType, String orderType, String client, String bosSale, String channel, OrderDetails orderDetails) {
        super(productType, orderType, client, bosSale, channel);
        this.orderDetails = orderDetails;
    }
}
