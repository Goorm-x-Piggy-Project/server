package com.piggymetrics.auth.service;

import com.piggymetrics.auth.Exception.UserException;
import com.piggymetrics.auth.Exception.UserException.UserAlreadyExistsException;
import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.domain.dto.request.UserCreateReqDto;
import com.piggymetrics.auth.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
	@Mock
	private PasswordEncoder passwordEncoder;
    @InjectMocks
	private UserService userService;

	@Test
	@DisplayName("사용자 생성 시 repository.save() 메소드가 호출된다.")
	public void shouldCreateUser() {
		// given
        UserCreateReqDto userCreateReqDto = UserCreateReqDto.builder()
                .username("name")
                .password("password")
                .build();

		// when
		userService.create(userCreateReqDto);

		// then
		verify(repository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("사용자 생성 시 이미 존재하는 사용자가 있으면 UserAlreadyExistsException이 발생한다.")
	public void shouldFailWhenUserAlreadyExists() {
		// given
		User user = User.of("name", "password");
		UserCreateReqDto userCreateReqDto = UserCreateReqDto.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.build();

		// when
		when(repository.findById(user.getUsername())).thenReturn(Optional.of(user));

		// then
		assertThatThrownBy(() ->
				userService.create(userCreateReqDto)).isInstanceOf(UserAlreadyExistsException.class);
	}
}
