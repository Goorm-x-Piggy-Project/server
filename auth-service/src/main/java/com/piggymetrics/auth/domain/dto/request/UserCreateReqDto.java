package com.piggymetrics.auth.domain.dto.request;

import com.piggymetrics.auth.domain.User;
import lombok.*;


@Getter
public class UserCreateReqDto {
    private final String username;
    private final String password;

    @Builder
    private UserCreateReqDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static UserCreateReqDto of(String username, String password) {
        return UserCreateReqDto.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static User toEntity(UserCreateReqDto userCreateReqDto) {
        return User.of(userCreateReqDto.getUsername(), userCreateReqDto.getPassword());
    }
}
