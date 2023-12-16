package com.depromeet.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;



@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CommonRestExceptionHandler extends RuntimeException {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public CustomResponseEntity<String> handleExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("defaultExceptionHandler", e);
        return CustomResponseEntity.fail(Result.FAIL);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public CustomResponseEntity<String> handleCustomExceptionHandler(CustomException exception) {
        log.error("CustomExceptionHandler code : {}, message : {}",
                exception.getResult().getCode(), exception.getResult().getMessage());
        return CustomResponseEntity.fail(exception.getResult());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public CustomResponseEntity<Object> handleBadRequest(
            MethodArgumentNotValidException e, HttpServletRequest request
    ) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return CustomResponseEntity.builder()
                .code(400)
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
            MissingServletRequestParameterException.class
    )
    public CustomResponseEntity<Object> handleBadRequest(
            MissingServletRequestParameterException e, HttpServletRequest request
    ) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getParameterName() + " 값이 등록되지 않았습니다.");
        return CustomResponseEntity.builder()
                .code(-1)
                .message(e.getParameterName() + " 값이 등록되지 않았습니다.")
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
            MissingServletRequestPartException.class
    )
    public CustomResponseEntity<Object> handleBadRequest(
            MissingServletRequestPartException e, HttpServletRequest request
    ) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getRequestPartName() + " 값을 요청받지 못했습니다.");
        return CustomResponseEntity.builder()
                .code(-1)
                .message("{ " + e.getRequestPartName() + " }"+ " 값을 요청받지 못했습니다.")
                .build();
    }
}