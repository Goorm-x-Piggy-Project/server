package com.piggymetrics.auth.domain.dto.response;

import com.piggymetrics.auth.domain.User;
import lombok.*;

@Getter
public class UserCreateResDto {
    private final String username;

    @Builder
    private UserCreateResDto(String username) {
        this.username = username;
    }

    public static UserCreateResDto of(String username) {
        return UserCreateResDto.builder()
                .username(username)
                .build();
    }

    public static UserCreateResDto from(User user) {
        return UserCreateResDto.builder()
                .username(user.getUsername())
                .build();
    }
}
