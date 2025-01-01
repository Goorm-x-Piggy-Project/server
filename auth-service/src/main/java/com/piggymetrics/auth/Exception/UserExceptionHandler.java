package com.piggymetrics.auth.Exception;

import com.piggymetrics.auth.domain.dto.response.UserErrorResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler extends RuntimeException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserErrorResDto> handleUserException(UserException e) {
        UserErrorResDto userErrorResDto = UserErrorResDto.of(
                e.getErrorCode(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userErrorResDto);
    }
}
