package com.piggymetrics.auth.domain.dto.request;

import com.piggymetrics.auth.domain.User;
import lombok.*;


@Getter
@Builder
@RequiredArgsConstructor
public class UserCreateRequestDto {
    private final String username;
    private final String password;

    public static UserCreateRequestDto of(String username, String password) {
        return UserCreateRequestDto.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static User toEntity(UserCreateRequestDto userCreateRequestDto) {
        return User.of(userCreateRequestDto.getUsername(), userCreateRequestDto.getPassword());
    }
}
