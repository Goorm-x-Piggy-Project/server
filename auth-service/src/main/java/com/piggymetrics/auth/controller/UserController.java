package com.piggymetrics.auth.controller;

import com.piggymetrics.auth.domain.dto.request.UserCreateReqDto;
import com.piggymetrics.auth.domain.dto.response.UserCreateResDto;
import com.piggymetrics.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
	private final UserService userService;

	@GetMapping("/current")
	public Principal getUser(Principal principal) {
		return principal;
	}

	@PreAuthorize("hasAuthority('SCOPE_server')")
	@PostMapping
	public ResponseEntity<UserCreateResDto> createUser(@Valid @RequestBody UserCreateReqDto requestDto) {
		log.debug("auth-service createUser 컨트롤러 진입");
		UserCreateResDto userCreateResDto = userService.create(requestDto);
		return ResponseEntity.ok(userCreateResDto);
	}
}
