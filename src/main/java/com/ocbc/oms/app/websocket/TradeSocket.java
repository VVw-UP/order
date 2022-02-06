package com.ocbc.oms.app.websocket;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ServerEndpoint(value = "/tradesocket")
@Component
@Slf4j
public class TradeSocket {

    /**
     * key--> sessionId  value-->session
     */
    private static ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<>();

    private Session session;


    /**
     * sessionId 获取 session
     *
     * @return
     */
    public static ConcurrentMap<String, Session> getSession() {
        return sessions;
    }


    @OnClose
    public void onClose() throws IOException {
        session.close();
        sessions.remove(session.getId());
    }

    /**
     * socket连接建立后，发送前端报价的panic状态
     *
     * @param session
     */
    @OnOpen
    public void onWebSocketConnect(Session session) {
        this.session = session;
        session.setMaxIdleTimeout(0);
        sessions.put(session.getId(), session);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable arg0) {
        if (session != null) {
            sessions.remove(session.getId());
        }
    }

    @OnMessage
    public void onWebSocketText(String msg, Session session) {
        if (!session.isOpen()) {
            return;
        }
        //心跳机制
        if (StringUtils.isEmpty(msg)) {
            return;
        }
    }

    /**
     * 通知前台
     *
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        if (sessions.isEmpty()) {
            return;
        }
        for (Session value : sessions.values()) {
            log.info("Trade send Message：{}", message);
            value.getAsyncRemote().sendText(message);
        }
    }
}
