package com.ocbc.oms.app.event.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocbc.oms.app.consts.DataConstant;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.error.api.MissingInvalidEventTypeException;
import com.ocbc.oms.app.event.Event;
import com.ocbc.oms.app.event.TradeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: Event factory
 * @Author zhenMing.pan
 * @Date 2021/11/19 13:37
 * @Version V1.0.0
 **/
@Slf4j
public class EventFactory {

    public static Event create(String message){
        JSONObject jsonObject = JSON.parseObject(message);
        String eventType = jsonObject.getString(DataConstant.EVENT_TYPE);
        if (StringUtils.isEmpty(eventType)){
             throw new MissingInvalidEventTypeException(APIErrorConstant.MissInvalidEventTypeExceptionMessage,APIErrorConstant.MissInvalidEventTypeCode);
        }
        switch (eventType) {
            case DataConstant.ADD:
            case DataConstant.UPDATE:
                return new TradeEvent(jsonObject);

            default:
                throw new MissingInvalidEventTypeException(APIErrorConstant.MissInvalidEventTypeExceptionMessage,APIErrorConstant.MissInvalidEventTypeCode);
        }
    }
}
