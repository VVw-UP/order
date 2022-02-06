package com.ocbc.oms.app.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TCurrencyDataDto {
    private String currency;
    private LocalDateTime tradeDate;
    private LocalDateTime updateTime;
    private String rollTime;
    private Long spotDays;
    private String timeZone;

    public TCurrencyDataDto(){

    }
    public TCurrencyDataDto(String currency, LocalDateTime tradeDate, LocalDateTime updateTime, String rollTime, Long spotDays, String timeZone) {
        this.currency = currency;
        this.tradeDate = tradeDate;
        this.updateTime = updateTime;
        this.rollTime = rollTime;
        this.spotDays = spotDays;
        this.timeZone = timeZone;
    }
}
