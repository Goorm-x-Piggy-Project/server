package com.piggymetrics.auth.domain.dto.response;

import com.piggymetrics.auth.domain.User;
import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
public class UserCreateResponseDto {
    private final String username;

    public static UserCreateResponseDto of(String username) {
        return UserCreateResponseDto.builder()
                .username(username)
                .build();
    }

    public static UserCreateResponseDto from(User user) {
        return UserCreateResponseDto.builder()
                .username(user.getUsername())
                .build();
    }
}
