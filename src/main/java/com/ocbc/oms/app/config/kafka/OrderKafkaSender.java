package com.ocbc.oms.app.config.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocbc.oms.app.api.CFSOrderEnquiryRequest;
import com.ocbc.oms.app.api.SearchParam;
import com.ocbc.oms.app.api.SearchResponse;
import com.ocbc.oms.app.managers.DbOMSManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mqt
 * @version 1.0
 * @date 2021/12/17 14:36
 */
@Slf4j
@Component
public class OrderKafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.sendOrderTopic:}")
    private String sendOrderTopic;

    @Autowired
    private DbOMSManager dbOMSManager;

    public void sendOrderMsg(Long orderId, String msgType) {
        if (orderId == null) {
            return;
        }
        CFSOrderEnquiryRequest request = new CFSOrderEnquiryRequest();
        ArrayList<SearchParam> params = new ArrayList<>();
        SearchParam param = new SearchParam();
        param.setSearchType("orderId");
        param.setValue(orderId + "");
        params.add(param);
        request.setParams(params);
        List<SearchResponse> searchParams = dbOMSManager.processSearchParams(request);
        if (searchParams == null || searchParams.isEmpty()) {
            return;
        }
        JSONObject msg = (JSONObject) JSON.toJSON(searchParams.get(0));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", msg);
        jsonObject.put("msgType", msgType);
        try {
            kafkaTemplate.send(sendOrderTopic, jsonObject.toJSONString());
        } catch (Exception e) {
            log.info("send msg : {}", jsonObject.toJSONString());
            log.error("kafka send order error", e);
        }
    }
}
