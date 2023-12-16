package com.depromeet.common.exception;

import lombok.Getter;

@Getter
public enum Result {

    OK(200, "성공"),
    LOGOUT_OK(200, "로그아웃 성공"),
    DELETE_OK(200, "회원 탈퇴 성공"),
    FAIL(400, "실패"),
    BAD_REQUEST(400,"잘못된 요청");




    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result resolve(int code) {
        for (Result result : values()) {
            if (result.getCode() == code) {
                return result;
            }
        }
        return null;
    }

}
