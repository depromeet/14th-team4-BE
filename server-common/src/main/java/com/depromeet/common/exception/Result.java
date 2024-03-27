package com.depromeet.common.exception;

import lombok.Getter;

@Getter
public enum Result {

    OK(200, "성공"),
    CREATED(201, "생성"),
    LOGOUT_OK(200, "로그아웃 성공"),
    DELETE_OK(200, "회원 탈퇴 성공"),
    FAIL(400, "실패"),
    BAD_REQUEST(400,"잘못된 요청"),
    SOCIAL_LOGIN_FAIL(401, "소셜로그인 실패"),
    UNAUTHORIZED_USER(403, "권한 없는 사용자"),
    DELETED_USER(401, "탈퇴 유저"),
    TOKEN_EXPIRED(401, "토큰 유효 기간 만료"),
    TOKEN_NOTSUPPORTED(401, "지원되지 않는 토큰 형식"),
    TOKEN_INVALID(401, "잘못된 토큰 형식"),
    CANNOT_MODIFY_INFORMATION_ANOTHER_ACCOUNT(400, "다른 계정의 정보를 수정할 수 없습니다."),
    CANNOT_FOLLOW_YOURSELF(400, "자기 자신을 팔로우할 수 없습니다."),
    DUPLICATED_NICKNAME(400, "이미 존재하는 닉네임입니다."),
    DUPLICATED_STORE(400, "이미 존재하는 가게입니다."),
    NOT_FOUND_COOKIE(401, "존재하지 않는 쿠키"),
    NOT_FOUND_USER(401, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_BOOKMARK(404, "북마크를 찾을 수 없습니다."),
    NOT_FOUND_STORE(404, "가게를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY(404, "카테고리를 찾을 수 없습니다." ),
    NOT_FOUND_REVIEW(404, "리뷰를 찾을 수 없습니다."),
    NOT_FOUND_FEED(404, "피드를 찾을 수 없습니다."), NOT_FOUND_COMMENT(404, "댓글을 찾을 수 없습니다."),;




    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result resolve(int code) {
        for (Result result : values()) {
            if (result.getCode() == code) {
                return result;
            }
        }
        return null;
    }

}
