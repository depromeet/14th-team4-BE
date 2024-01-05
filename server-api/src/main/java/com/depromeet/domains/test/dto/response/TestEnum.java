package com.depromeet.domains.test.dto.response;

import com.depromeet.test.TestEnumType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestEnum {
    private Long id;
    private String title;
    private String content;
    private TestEnumType testEnumType;
}
