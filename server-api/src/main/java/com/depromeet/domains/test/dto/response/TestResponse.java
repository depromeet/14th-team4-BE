package com.depromeet.domains.test.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestResponse {

    private Long id;
    private String title;
    private String content;

}
