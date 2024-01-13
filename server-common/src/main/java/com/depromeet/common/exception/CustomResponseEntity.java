package com.depromeet.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomResponseEntity<T> {

    private int code;
    private String message;
    private T data;

    public static <T> CustomResponseEntity<T> success(T data) {
        return CustomResponseEntity.<T>builder()
                .code(Result.OK.getCode())
                .message(Result.OK.getMessage())
                .data(data)
                .build();
    }

    public static <T> CustomResponseEntity<T> success() {
        return CustomResponseEntity.<T>builder()
                .code(Result.OK.getCode())
                .message(Result.OK.getMessage())
                .build();
    }

    public static <T> CustomResponseEntity<T> fail() {
        return CustomResponseEntity.<T>builder()
                .code(Result.FAIL.getCode())
                .message(Result.FAIL.getMessage())
                .build();
    }

    public static <T> CustomResponseEntity<T> fail(Result result) {
        return CustomResponseEntity.<T>builder()
                .code(result.getCode())
                .message(result.getMessage())
                .build();
    }

    public static <T> CustomResponseEntity<T> created(T data) {
        return CustomResponseEntity.<T>builder()
            .code(Result.CREATED.getCode())
            .message(Result.CREATED.getMessage())
            .data(data)
            .build();
    }

    @Builder
    public CustomResponseEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
