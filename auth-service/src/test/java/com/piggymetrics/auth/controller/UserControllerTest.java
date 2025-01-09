package com.piggymetrics.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.domain.dto.request.UserCreateReqDto;
import com.piggymetrics.auth.service.UserService;
import com.sun.security.auth.UserPrincipal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String urlPrefix = "/api/v1/users";

	@Mock
	private UserService userService;
	@InjectMocks
	private UserController userController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	@DisplayName("사용자를 생성한다.")
	public void shouldCreateNewUser() throws Exception {
		// given
		UserCreateReqDto user = UserCreateReqDto.of("test", "password");
		String json = mapper.writeValueAsString(user);

		// when, then
		mockMvc.perform(post(urlPrefix).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("사용자가 유효하지 않을 때 실패한다.")
	public void shouldFailWhenUserIsNotValid() throws Exception {
		// given
		UserCreateReqDto user = UserCreateReqDto.of("t", "p");
		String json = mapper.writeValueAsString(user);

		// when, then
		mockMvc.perform(post(urlPrefix).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("현재 사용자를 반환한다.")
	public void shouldReturnCurrentUser() throws Exception {
		// given, when, then
		mockMvc.perform(get(urlPrefix + "/current").principal(new UserPrincipal("test")))
				.andExpect(jsonPath("$.name").value("test"))
				.andExpect(status().isOk());
	}
}
