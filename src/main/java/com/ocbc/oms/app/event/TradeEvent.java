package com.ocbc.oms.app.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocbc.oms.app.consts.DataConstant;
import com.ocbc.oms.app.model.CFSOrder;
import com.ocbc.oms.app.websocket.TradeSocket;

import javax.websocket.Session;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: trade event
 * @Author zhenMing.pan
 * @Date 2021/11/19 15:28
 * @Version V1.0.0
 **/
public class TradeEvent implements Event {
    private CFSOrder cfsOrder;
    private String eventType;

    public TradeEvent(JSONObject message) {
        this.cfsOrder = message.getObject(DataConstant.DATA, CFSOrder.class);
        this.eventType = message.getString(DataConstant.EVENT_TYPE);

    }

    @Override
    public void execute() {
        Collection<Session> values = TradeSocket.getSession().values();
        Map<String, Object> map = new HashMap<>();
        map.put(DataConstant.EVENT_TYPE, eventType);
        values.forEach(session -> {
            if (session != null && session.isOpen()) {
                map.put(DataConstant.DATA, cfsOrder);
                session.getAsyncRemote().sendText(JSON.toJSONString(map));
            }
        });
    }
}
