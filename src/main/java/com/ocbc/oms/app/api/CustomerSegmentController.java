package com.ocbc.oms.app.api;


import com.ocbc.oms.app.dbservice.CustomerSegmentService;
import com.ocbc.oms.app.dbservice.EntityService;
import com.ocbc.oms.app.model.CustomerSegment;
import com.ocbc.oms.app.model.dto.CustomerSegmentDto;
import com.ocbc.oms.app.model.dto.EntityDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/customerSegment")
@AllArgsConstructor
public class CustomerSegmentController {

    private final CustomerSegmentService segmentService;

    private final EntityService entityService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerSegment> findCustomerSegment(@RequestParam("userId") Integer userId) {
        return segmentService.find(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/insert")
    public Boolean insertCustomerSegment(CustomerSegmentDto customerSegmentDto) {
        return segmentService.insert(customerSegmentDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/update")
    public Boolean updateCustomerSegment(CustomerSegmentDto customerSegmentDto) {
        return segmentService.updateCustomerSegment(customerSegmentDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/delete/{id}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteCustomerSegment(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return segmentService.deleteCustomerSegment(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/findEntity", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EntityDto> findEntity() {
        return entityService.findEntity();
    }
}

