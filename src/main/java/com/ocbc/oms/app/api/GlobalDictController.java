package com.ocbc.oms.app.api;


import com.ocbc.oms.app.dbservice.CurrencyPairService;
import com.ocbc.oms.app.dbservice.GlobalDictService;
import com.ocbc.oms.app.model.dto.GlobalDictDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 全局字典表 前端控制器
 * </p>
 *
 * @author Hzy
 * @since 2021-10-21
 */
@RestController
@RequestMapping("/globalDict")
@AllArgsConstructor
public class GlobalDictController {

    private final GlobalDictService globalDictService;

    private final CurrencyPairService currencyPairService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/findSegmentType", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GlobalDictDto> findSegmentType() {
        return globalDictService.findSegmentType();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/findMurexBook", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GlobalDictDto> findMurexBookingType() {
        return globalDictService.findMurexBookType();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/findCcyPair", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> findCcyPair() {
        return currencyPairService.findCcyPair();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/findCcyPairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GlobalDictDto> findCcyPairs() {
        return currencyPairService.findCcyPairs();
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/ccyPairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public AllCcyPairsResponse allCcyPairs() {
        return new AllCcyPairsResponse(currencyPairService.allPair());
    }

}

