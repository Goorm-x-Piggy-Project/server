package com.piggymetrics.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserReqDto {

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 3, max = 10)
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 15)
    private String password;
}
