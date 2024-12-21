package com.piggymetrics.account.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	private String id;

	@NotNull
	@Size(min = 3, max = 20)
	private String username;

	@NotNull
	@Size(min = 6, max = 40)
	private String password;
}
