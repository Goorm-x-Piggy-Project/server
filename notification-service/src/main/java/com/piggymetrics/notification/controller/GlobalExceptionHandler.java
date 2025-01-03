package com.piggymetrics.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 잘못된 입력 값에 대한 예외를 처리하는 전역 예외 처리 클래스.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * MethodArgumentTypeMismatchException을 처리하여 잘못된 경로 변수 값에 대해 400 Bad Request 반환.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleInvalidEnum(MethodArgumentTypeMismatchException ex) {
        // 예외 메시지에서 발생한 경로 변수와 타입을 추출하여 더 세부적인 메시지를 제공
        String errorMessage = String.format("[ERROR] 잘못된 알림 유형입니다. 유효한 값이 아닙니다: %s", ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
}
