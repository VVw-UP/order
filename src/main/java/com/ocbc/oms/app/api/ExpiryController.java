package com.ocbc.oms.app.api;

import com.ocbc.oms.app.dbservice.TExpiryTimeService;
import com.ocbc.oms.app.model.dto.ResponseDto;
import com.ocbc.oms.app.model.dto.TExpiryTimeVo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/expiry")
public class ExpiryController {

    private final TExpiryTimeService expiryTimeService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/findExpiryTimes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TExpiryTimeVo> findExpiryTimes() {
        return expiryTimeService.findExpiryTimes();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/insertExpiryTime", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto insertExpiryTime(@RequestBody TExpiryTimeVo tExpiryTimeVo) {
        return new ResponseDto(200, "", expiryTimeService.insertExpiryTime(tExpiryTimeVo));
    }

}
