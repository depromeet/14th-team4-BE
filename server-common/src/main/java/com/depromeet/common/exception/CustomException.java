package com.depromeet.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private Result result;
    private String debug;

    public CustomException(Result result) {
        super(result.getMessage()); // 부모 클래스의 생성자 호출
        this.result = result;
        this.debug = result.getMessage();
    }
}