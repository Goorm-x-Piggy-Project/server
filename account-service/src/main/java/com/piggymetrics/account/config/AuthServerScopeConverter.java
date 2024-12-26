package com.piggymetrics.account.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class AuthServerScopeConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        ArrayList<String> roles = (ArrayList<String>) source.getClaims().get("scope");
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }

        // Spring Security가 권한을 처리할 수 있도록 사용자 역할을 권한으로 변환할 때 ROLE_ 접두사 추가
        Collection<GrantedAuthority> returnValue = roles.stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return returnValue;
    }
}
