package com.ocbc.oms.app.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author hzy
 * @since 2021-12-21
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketResponse<T> {

    private int statusCode;
    private String reasonPhrase;
    private T data;
    private String eventType;

    public WebSocketResponse(T data,String eventType){
        this.statusCode = 200;
        this.reasonPhrase = "OK";
        this.data = data;
        this.eventType = eventType;
    }
}
