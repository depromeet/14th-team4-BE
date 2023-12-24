package com.depromeet.domains.test.service;

import com.depromeet.domains.test.dto.request.TestRequest;
import com.depromeet.domains.test.dto.response.TestResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    public TestResponse create(TestRequest testRequest) {
        return TestResponse.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();
    }

    public List<TestResponse> findAll() {
        return List.of(
                TestResponse.builder()
                        .id(1L)
                        .title("title")
                        .content("content")
                        .build(),
                TestResponse.builder()
                        .id(2L)
                        .title("title")
                        .content("content")
                        .build()
        );
    }

    public TestResponse findById(Long testId) {
        return TestResponse.builder()
                .id(testId)
                .title("title")
                .content("content")
                .build();
    }

    public void update(Long testId, TestRequest testRequest) {

    }

    public void delete(Long testId) {

    }
}
