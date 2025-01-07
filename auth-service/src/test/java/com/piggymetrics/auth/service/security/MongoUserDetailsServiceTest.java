package com.piggymetrics.auth.service.security;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MongoUserDetailsServiceTest {

	@Mock
	private UserRepository repository;
	@InjectMocks
	private MongoUserDetailsService service;

	@Test
	@DisplayName("사용자 이름으로 사용자 정보를 로드한다.")
	public void shouldLoadByUsernameWhenUserExists() {
		// given
		final User user = User.of("user", "password");
		when(repository.findById(any())).thenReturn(Optional.of(user));

		// when
		UserDetails loaded = service.loadUserByUsername("user");

		// then
		assertEquals(user, loaded);
	}

	@Test
	@DisplayName("사용자 이름으로 사용자 정보를 로드할 때 사용자가 존재하지 않으면 예외가 발생한다.")
	public void shouldFailToLoadByUsernameWhenUserNotExists() {
		Assertions.assertThatThrownBy(() ->
				service.loadUserByUsername("name")).isInstanceOf(UsernameNotFoundException.class);
	}
}