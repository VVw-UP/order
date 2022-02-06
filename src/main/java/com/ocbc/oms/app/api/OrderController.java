package com.ocbc.oms.app.api;

import com.ocbc.oms.app.config.cache.TradeCache;
import com.ocbc.oms.app.managers.DbOMSManager;
import com.ocbc.oms.app.model.PageEntity;
import com.ocbc.oms.app.model.TUiViewLabel;
import com.ocbc.oms.app.model.dto.RealTimePriceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@RestController
@Slf4j
@RequestMapping("api/order")
public class OrderController {
    private final DbOMSManager dbOMSManager;

    public OrderController(DbOMSManager dbOMSManager) {
        this.dbOMSManager = dbOMSManager;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/spot", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse saveCFSSpotOrder(@RequestHeader("authenticatedId") String user,
                                          @RequestBody @Valid CFSOrderRequest spotOrder) {
        return dbOMSManager.createCFSOrder(spotOrder, user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/enquire", produces = MediaType.APPLICATION_JSON_VALUE)
    public CFSOrderEnquiryResponse enquireCFSSpotOrder(@RequestHeader("authenticatedId") String user, @RequestBody CFSOrderEnquiryRequest orderEnquiryRequest) {
        return dbOMSManager.processCFSEnquiry(user, orderEnquiryRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/enquireOrig", produces = MediaType.APPLICATION_JSON_VALUE)
    public CFSOrderEnquiryResponse enquireOrigCFSSpotOrder(@RequestHeader("authenticatedId") String user, @RequestBody CFSOrderEnquiryRequest orderEnquiryRequest) {
        CFSOrderEnquiryResponse cfsOrderEnquiryResponse = dbOMSManager.processCFSEnquiry(user, orderEnquiryRequest);
        return dbOMSManager.exChangeOrig(cfsOrderEnquiryResponse.getSearchResultList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/cancel/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CancelOrder cancelCFSSpotOrder(@RequestHeader("authenticatedId") String user, @PathVariable("orderId") Long orderId) {
        return dbOMSManager.cancelCFSOrder(user, orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/amend", produces = MediaType.APPLICATION_JSON_VALUE)
    public AmendOrderResponse amendCFSSpotOrder(@RequestHeader("authenticatedId") String user,
                                                @Valid @RequestBody AmendOrderRequest amendOrderRequest) {
        //修改时，校验过期时间
        return dbOMSManager.amendCFSSpotOrder(user, amendOrderRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/expiry/enquire")
    public CFSOrderExpiryResponse expiryCFSSpotOrder(@RequestHeader("authenticatedId") String user) {
        return dbOMSManager.expiryCFSSpotOrder(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/query")
    public CFSOrderResponse queryCFSSpotOrder(@RequestHeader("authenticatedId") String user, @RequestBody PageEntity pageEntity) {
        return dbOMSManager.queryCFSSpotOrder(user, pageEntity);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/bulKeDit/{orderId}/{orderStatus}")
    public List<BulKeDitResponse> bulKeDitCFSSpotOrder(@RequestHeader("authenticatedId") String user, @PathVariable("orderStatus") Long orderStatus, @PathVariable("orderId") Long orderId) {
        return dbOMSManager.bulKeDitCFSSpotOrder(user, orderStatus, orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/mergeUiViewData")
    public List<TUiViewLabel> mergeUiViewData(@RequestHeader("authenticatedId") String user) {
        return dbOMSManager.mergeUiViewData(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/bulKeDit")
    public List<Object> bulKeDit(@RequestHeader("authenticatedId") String user) {
        return new ArrayList<>();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/batchCancel")
    public void batchCancel(@RequestHeader("authenticatedId") String user, @RequestBody BatchCancelRequest batchCancelRequest) {
        dbOMSManager.batchCancel(user, batchCancelRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/allPrice")
    public Map<String, RealTimePriceVo> batchCancel() {
        return TradeCache.getAllRealTimePrice();
    }
}
