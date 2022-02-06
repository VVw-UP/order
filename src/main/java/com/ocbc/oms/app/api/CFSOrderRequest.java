package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CFSOrderRequest extends BaseOrder{
    @Valid
    private CFSOrderDetails orderDetails;
    private String cifNumber;
    private String customerSeg;
    private String channelCode;
    private String productCode;
    private String cfsRefNumber;

    @Builder(builderMethodName = "cfsOrderRequestBuilder")
    public CFSOrderRequest(String productType, String orderType, String client, String bosSale, String channel,
                           CFSOrderDetails orderDetails, String cifNumber, String customerSeg, String channelCode,
                           String productCode) {
        super(productType, orderType, client, bosSale, channel);
        this.orderDetails = orderDetails;
        this.cifNumber = cifNumber;
        this.customerSeg = customerSeg;
        this.channelCode = channelCode;
        this.productCode = productCode;
    }

}
