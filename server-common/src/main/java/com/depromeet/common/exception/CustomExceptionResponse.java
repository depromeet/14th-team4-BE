package com.depromeet.common.exception;

import lombok.*;

@Getter
@Setter
public class CustomExceptionResponse extends RuntimeException {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class entryPointResponse<T>{
        private Integer code;
        private String message;
        private T data;

        public static CustomExceptionResponse.entryPointResponse response(CustomException customException){
            return entryPointResponse
                    .builder()
                    .code(customException.getResult().getCode())
                    .message(customException.getResult().getMessage())
                    .data(null)
                    .build();
        }
    }
}