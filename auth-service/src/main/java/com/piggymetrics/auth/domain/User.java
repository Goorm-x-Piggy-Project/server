package com.piggymetrics.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Document(collection = "users")
@Getter
@NoArgsConstructor
public class User implements UserDetails {

	@Id
	private String username;

	private String password;

	@Override
	public List<GrantedAuthority> getAuthorities() {
		return null;
	}

	@Builder(access = AccessLevel.PRIVATE)
	private User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public static User of(String username, String password) {
		return User.builder()
				.username(username)
				.password(password)
				.build();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
