package com.piggymetrics.auth.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserErrorResDto {
    private String errorCode;
    private String message;

    @Builder
    private UserErrorResDto(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static UserErrorResDto of(String errorCode, String message) {
        return UserErrorResDto.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}
