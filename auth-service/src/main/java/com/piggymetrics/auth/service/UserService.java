package com.piggymetrics.auth.service;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.domain.dto.request.UserCreateRequestDto;
import com.piggymetrics.auth.domain.dto.response.UserCreateResponseDto;
import com.piggymetrics.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public UserCreateResponseDto create(UserCreateRequestDto requestDto) {
		log.info("auth-service create 메소드 진입(서비스 계층)");
		Optional<User> existing = userRepository.findById(requestDto.getUsername());
		existing.ifPresent(it-> {throw new IllegalArgumentException("user already exists: " + it.getUsername());});

		String hash = passwordEncoder.encode(requestDto.getPassword());
		User user = User.of(requestDto.getUsername(), hash);

		userRepository.save(user);
		log.info("new user has been created: {}", user.getUsername());

		return UserCreateResponseDto.from(user);
	}
}
