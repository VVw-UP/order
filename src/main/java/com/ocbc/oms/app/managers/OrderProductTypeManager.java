package com.ocbc.oms.app.managers;

import com.ocbc.oms.app.dbservice.OrderService;
import com.ocbc.oms.app.dbservice.OrderStatusService;
import com.ocbc.oms.app.dbservice.OrderTypeService;
import com.ocbc.oms.app.dbservice.ProductTypeService;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.error.api.ExpiryTimeException;
import com.ocbc.oms.app.error.application.ApplicationErrorConstant;
import com.ocbc.oms.app.error.application.InvalidInputMapException;
import com.ocbc.oms.app.model.PageEntity;
import com.ocbc.oms.app.model.TExpiryTime;
import com.ocbc.oms.app.model.TOrder;
import com.ocbc.oms.app.model.TOrderStatus;
import com.ocbc.oms.app.model.TOrderType;
import com.ocbc.oms.app.model.TProductType;
import com.ocbc.oms.app.repository.TExpiryTimeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@DependsOn("flywayInitializer")
public class OrderProductTypeManager {

    private final OrderService orderService;

    private Map<String, TOrderStatus> orderStatusMap;
    public Map<Integer, TOrderStatus> orderStatusById;
    private final OrderStatusService orderStatusService;

    private final OrderTypeService orderTypeService;
    private Map<String, TOrderType> orderTypeMap;
    public Map<Integer, TOrderType> orderTypeById;


    private final ProductTypeService productTypeService;
    private Map<String, TProductType> productTypeMap;
    public Map<Integer, TProductType> productTypeById;

    public TExpiryTimeMapper tExpiryTimeMapper;

    public OrderProductTypeManager(OrderService orderService, OrderStatusService orderStatusService,
                                   OrderTypeService orderTypeService, ProductTypeService productTypeService,
                                   TExpiryTimeMapper tExpiryTimeMapper) {
        this.orderService = orderService;
        this.orderStatusService = orderStatusService;
        this.orderTypeService = orderTypeService;
        this.productTypeService = productTypeService;
        this.tExpiryTimeMapper = tExpiryTimeMapper;
    }

    public int addOrder(TOrder order) {
        return orderService.addOrder(order);
    }

    public List<TOrder> getOrderByUniqId(String id) {
        return orderService.getOrderByUniqId(id);
    }

    public List<TOrder> getOrderByOrderId(Long id) {
        return orderService.getOrderById(id);
    }

    public Integer getOrderStatusId(String code) {
        if (StringUtils.isNotBlank(code) && orderStatusMap.containsKey(code)) {
            return orderStatusMap.get(code).getId();
        }
        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
            .append(" -> orderStatusMap")
            .toString(),
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public String getOrderStatusCodeById(Integer id) {
        if (id != null) {
            List<TOrderStatus> list = orderStatusMap.values().stream().filter(tOrderStatus -> id.equals(tOrderStatus.getId())).collect(Collectors.toList());
            if (!list.isEmpty()) {
                return list.get(0).getCode();
            }
        }
        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
            .append(" -> orderStatusMap")
            .toString(),
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public Integer getOrderTypeId(String code) {
        if (StringUtils.isNotBlank(code) && orderTypeMap.containsKey(code)) {
            return orderTypeMap.get(code).getId();
        }
        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
            .append(" -> orderTypeMap")
            .toString(),
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public String getOrderTypeCodeById(Integer id) {
        if (id != null) {
            List<TOrderType> list = orderTypeMap.values().stream().filter(tOrderType -> id.equals(tOrderType.getId())).collect(Collectors.toList());
            if (!list.isEmpty()) {
                return list.get(0).getCode();
            }
        }
        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
            .append(" -> orderTypeMap")
            .toString(),
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public Integer getProductTypeIdFromMap(String code) {
        if (StringUtils.isNotBlank(code) && productTypeMap.containsKey(code)) {
            return productTypeMap.get(code).getId();
        }
        throw new InvalidInputMapException(new StringBuffer(ApplicationErrorConstant.InvalidTypeExceptionMessage)
            .append(" -> productTypeMap")
            .toString(),
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public String getProductTypeCodeFromId(Integer id) {
        if (id != null) {
            List<TProductType> list = productTypeMap.values().stream().filter(tProductType -> id.equals(tProductType.getId())).collect(Collectors.toList());
            if (!list.isEmpty()) {
                return list.get(0).getCode();
            }
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> productTypeMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public List<TOrder> queryOrders(PageEntity pageEntity) {
        return orderService.getAllOrders(pageEntity);
    }

    public int queryOrderCount() {
        return orderService.queryOrderCount();
    }

    public void validExpiryTime(String orderType, LocalDateTime expireTimestamp) {
        if (orderType == null || expireTimestamp == null) {
            return;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(expireTimestamp)) {
            throw new ExpiryTimeException(APIErrorConstant.ExpiryTimeInvalidExceptionMessage, APIErrorConstant.ExpiryTimeOverException);
        }
        //校验当前订单是否大于当前订单过期时间
        Integer orderTypeId = getOrderTypeId(orderType.toUpperCase());
        TExpiryTime tExpiryTime = tExpiryTimeMapper.selectById(orderTypeId);
        if (tExpiryTime == null || (tExpiryTime.getDays() == 0
            && tExpiryTime.getHours() == 0 && tExpiryTime.getMinutes() == 0)) {
            return;
        }
        currentTime = currentTime.plusDays(tExpiryTime.getDays());
        currentTime = currentTime.plusHours(tExpiryTime.getHours());
        currentTime = currentTime.plusMinutes(tExpiryTime.getMinutes());
        if (currentTime.isBefore(expireTimestamp)) {
            throw new ExpiryTimeException(APIErrorConstant.ExpiryTimeOverExceptionMessage, APIErrorConstant.ExpiryTimeOverException);
        }
    }

    private void initializeOrderStatusMap() {
        orderStatusMap = new HashMap<>();
        orderStatusById = new HashMap<>();
        List<TOrderStatus> fromDatabase = orderStatusService.getAllOrderStatus();
        fromDatabase.forEach(orderStatus -> {
            log.debug("Adding orderStatus:{}", orderStatus);
            orderStatusMap.put(orderStatus.getCode(), orderStatus);
            orderStatusById.put(orderStatus.getId(), orderStatus);
        });
    }

    private void initializeOrderTypeMap() {
        orderTypeMap = new HashMap<>();
        orderTypeById = new HashMap<>();
        List<TOrderType> fromDatabase = orderTypeService.getAllOrderTypes();
        fromDatabase.forEach(orderType -> {
            log.debug("Adding orderType:{}", orderType);
            orderTypeMap.put(orderType.getCode(), orderType);
            orderTypeById.put(orderType.getId(), orderType);
        });
    }

    private void initializeProductTypeMap() {
        productTypeMap = new HashMap<>();
        productTypeById = new HashMap<>();
        List<TProductType> fromDatabase = productTypeService.getAllProductTypes();
        fromDatabase.forEach(productType -> {
            log.debug("Adding productType:{}", productType);
            productTypeMap.put(productType.getCode(), productType);
            productTypeById.put(productType.getId(), productType);
        });
    }

    @PostConstruct
    private void initialize() {
        initializeOrderStatusMap();
        initializeOrderTypeMap();
        initializeProductTypeMap();
    }
}
