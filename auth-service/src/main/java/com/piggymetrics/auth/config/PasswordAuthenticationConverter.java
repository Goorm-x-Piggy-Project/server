package com.piggymetrics.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Slf4j
public class PasswordAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        log.debug("PasswordAuthenticationConverter.convert 호출");
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        if (!grantType.equals("password")
                && !grantType.equals(AuthorizationGrantType.REFRESH_TOKEN.getValue())) {
            return null;
        }
        MultiValueMap<String, String> parameters = getParameters(request);

        // 그냥 여기서 authentication 객체를 만들어서 반환하자
        // TODO: 앞의 filter에서 Authentication 객체 만들어서 사용하도록 수정
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> parameters.getFirst(OAuth2ParameterNames.SCOPE));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                parameters.getFirst(OAuth2ParameterNames.USERNAME),
                parameters.getFirst(OAuth2ParameterNames.PASSWORD),
                authorities);

        HashMap<String, Object> additionalParameters = new HashMap<>(); // scope, username, password 등 추가 파라미터
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_SECRET) // Authentication 객체에 포함되는 정보들은 제외
            ) {
                additionalParameters.put(key, value.get(0));
            }
        });

        return new PasswordGrantAuthenticationToken(new AuthorizationGrantType(grantType), authentication, additionalParameters);
    }

    private MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }

        });
        return parameters;
    }
}
