package com.piggymetrics.auth.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.piggymetrics.auth.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
public class UserCreateReqDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private final String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private final String password;

    @Builder
    private UserCreateReqDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonCreator // ObjectMapper가 JSON을 UserCreateReqDto로 변환할 때 사용, dto를 record 클래스로 만든다면 이런 작업 해줄 필요 없을듯..?
    public static UserCreateReqDto of(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password) { // 생성자가 아닌 정적 팩토리 메소드 사용 시 @JsonProperty로 JSON 필드명을 명시해줘야 함
        return UserCreateReqDto.builder()
                .username(username)
                .password(password)
                .build();
    }
}
