package com.ocbc.oms.app.api;


import com.ocbc.oms.app.dbservice.SpreadService;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.model.dto.ResponseDto;
import com.ocbc.oms.app.model.dto.SpreadDelDto;
import com.ocbc.oms.app.model.dto.SpreadVo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/spread")
@AllArgsConstructor
public class SpreadController {

    private final SpreadService spreadService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SpreadVo> findSpread(@RequestParam("userId") Integer userId) {
        return spreadService.find(userId);
        //return new ResponseDto<>(200, "", spreadService.find(userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto insertSpread(@RequestBody SpreadVo spreadVo) {
        if (spreadService.selectCcy(spreadVo)) {
            return new ResponseDto(200, APIErrorConstant.DuplicateCurrencyPairExceptionMessage, false);
        }
        return new ResponseDto(200, "", spreadService.insert(spreadVo));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto updateSpread(@RequestBody SpreadVo spreadVo) {
        if (spreadService.selectCcy(spreadVo)) {
            return new ResponseDto(200, APIErrorConstant.DuplicateCurrencyPairExceptionMessage, false);
        }
        return new ResponseDto(200, "", spreadService.updateSpread(spreadVo));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto deleteSpread(@RequestBody SpreadDelDto spreadDelDto) {
        return new ResponseDto(200, "", spreadService.deleteSpread(spreadDelDto));
    }
}

