package com.ocbc.oms.app.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradeDateDto {

    private LocalDateTime tradeDate;

    private LocalDateTime valueDate;

    public TradeDateDto(LocalDateTime tradeDate, LocalDateTime valueDate) {
        this.tradeDate = tradeDate;
        this.valueDate = valueDate;
    }

    public TradeDateDto() {
    }
}
