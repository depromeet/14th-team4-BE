package com.depromeet.domains.test.controller;

import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.test.dto.request.TestRequest;
import com.depromeet.domains.test.dto.response.TestResponse;
import com.depromeet.domains.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/test")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    @PostMapping
    public CustomResponseEntity<TestResponse> create(@RequestBody final TestRequest testRequest) {
        TestResponse testResponse = testService.create(testRequest);
        return CustomResponseEntity.success(testResponse);
    }

    @GetMapping
    public CustomResponseEntity<List<TestResponse>> findAll() {
        return CustomResponseEntity.success(testService.findAll());
    }

    @GetMapping("/{testId}")
    public CustomResponseEntity<TestResponse> findById(@PathVariable final Long testId) {
        return CustomResponseEntity.success(testService.findById(testId));
    }

    @PutMapping("/{testId}")
    public CustomResponseEntity update(@PathVariable final Long testId, @RequestBody TestRequest testRequest) {
        testService.update(testId, testRequest);
        return CustomResponseEntity.success();
    }

    @DeleteMapping("/{testId}")
    public CustomResponseEntity<Void> delete(@PathVariable final Long testId) {
        return CustomResponseEntity.success();
    }
}
