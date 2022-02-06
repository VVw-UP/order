package com.ocbc.oms.app.api;


import com.ocbc.oms.app.dbservice.TraderSpreadService;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.model.TraderSpread;
import com.ocbc.oms.app.model.dto.ResponseDto;
import com.ocbc.oms.app.model.dto.TraderSpreadDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Hzy
 * @since 2021-10-20
 */
@RestController
@RequestMapping("/traderSpread")
@AllArgsConstructor
public class TraderSpreadController {

    private final TraderSpreadService traderSpreadService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TraderSpread> findTraderSpread(@RequestParam("userId") Integer userId) {
        return traderSpreadService.find(userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto insertTraderSpread(TraderSpreadDto traderSpreadDto) {
        if (traderSpreadService.selectCcy(traderSpreadDto.getCcyPar(), traderSpreadDto.getUserId(), traderSpreadDto.getId())) {
            return new ResponseDto(200, APIErrorConstant.DuplicateCurrencyPairExceptionMessage, false);
        }
        return new ResponseDto(200, "", traderSpreadService.insert(traderSpreadDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto updateTraderSpread(TraderSpreadDto traderSpreadDto) {
        if (traderSpreadService.selectCcy(traderSpreadDto.getCcyPar(), traderSpreadDto.getUserId(), traderSpreadDto.getId())) {
            return new ResponseDto(200, APIErrorConstant.DuplicateCurrencyPairExceptionMessage, false);
        }
        return new ResponseDto(200, "", traderSpreadService.updateSpread(traderSpreadDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/delete/{id}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto deleteTraderSpread(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return new ResponseDto(200, "", traderSpreadService.deleteSpread(id));
    }
}

