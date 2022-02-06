package com.ocbc.oms.app.api;

import com.ocbc.oms.app.managers.CurrencyManager;
import com.ocbc.oms.app.model.TCurrencyPair;
import com.ocbc.oms.app.model.dto.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("currencyPair")
@AllArgsConstructor
public class CurrencyPairController {

    private final CurrencyManager currencyManager;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/updateTCurrencyPair",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto updateTCurrencyPair(@RequestBody TCurrencyPair tCurrencyPair) {
        return new ResponseDto(200, "success",currencyManager.updateTCurrencyPair(tCurrencyPair));
    }
}
