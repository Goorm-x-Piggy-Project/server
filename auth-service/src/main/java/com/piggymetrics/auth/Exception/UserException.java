package com.piggymetrics.auth.Exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private String errorCode;

    public UserException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public static class UserAlreadyExistsException extends UserException {
        public UserAlreadyExistsException(String userId) {
            super(userId + "에 해당하는 유저가 이미 존재합니다.", "USER_ALREADY_EXISTS");
        }
    }
}
