package com.piggymetrics.auth.controller;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.domain.dto.request.UserCreateRequestDto;
import com.piggymetrics.auth.domain.dto.response.UserCreateResponseDto;
import com.piggymetrics.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService userService;

	@GetMapping("/current")
	public Principal getUser(Principal principal) {
		return principal;
	}

	@PreAuthorize("#oauth2.hasScope('server')")
	@PostMapping
	public ResponseEntity<UserCreateResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
		UserCreateResponseDto userCreateResponseDto = userService.create(requestDto);
		return ResponseEntity.ok(userCreateResponseDto);
	}
}
