package com.piggymetrics.auth.config;


import com.piggymetrics.auth.service.security.MongoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@RequiredArgsConstructor
@Slf4j
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private final OAuth2AuthorizationService authorizationService; // 발급된 토큰과 OAuth2Authorization 객체를 매핑
    private final OAuth2TokenGenerator tokenGenerator;
    private final MongoUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("PasswordAuthenticationProvider.authenticate 호출");
        PasswordGrantAuthenticationToken authenticationToken = (PasswordGrantAuthenticationToken) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        if(!registeredClient.getAuthorizationGrantTypes().contains(authenticationToken.getGrantType())) { // 등록된 클라이언트가 지원하지 않는 인증 방식인 경우
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE);
        }

        Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();
        String scope = (String) additionalParameters.get(OAuth2ParameterNames.SCOPE);

        UserDetails userDetails = null;
        if(authenticationToken.getGrantType().getValue().equals("password")) {
            String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
            String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
            if(username == null || password == null) {
                throw new OAuth2AuthenticationException("username and password must be provided");
            }

            userDetails = userDetailsService.loadUserByUsername(username);
            if(!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new OAuth2AuthenticationException("password not matched");
            }
        } else if (authenticationToken.getGrantType().getValue().equals(AuthorizationGrantType.REFRESH_TOKEN.getValue())) { // refresh token
            String givenRefreshToken = (String) additionalParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);
            OAuth2Authorization foundToken = authorizationService.findByToken(givenRefreshToken, OAuth2TokenType.REFRESH_TOKEN);
            if(foundToken == null) {
                throw new OAuth2AuthenticationException("Refresh token not matched");
            }
        } else {
            throw new OAuth2AuthenticationException("Unsupported grant type");
        }

        DefaultOAuth2TokenContext accessTokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(clientPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(authenticationToken.getGrantType())
                .authorizationGrant(authenticationToken)
                .authorizedScopes(Set.of(scope))
                .build();

        DefaultOAuth2TokenContext refreshTokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(clientPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .authorizationGrantType(authenticationToken.getGrantType())
                .authorizationGrant(authenticationToken)
                .authorizedScopes(Set.of(scope))
                .build();

        OAuth2Token generatedAccessToken = tokenGenerator.generate(accessTokenContext);
        OAuth2Token generatedRefreshToken = tokenGenerator.generate(refreshTokenContext);

        if(generatedAccessToken == null || generatedRefreshToken == null) {
            throw new OAuth2AuthenticationException("Token generation failed");
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(), generatedAccessToken.getExpiresAt(), Set.of(scope));
        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(generatedRefreshToken.getTokenValue(), generatedRefreshToken.getIssuedAt(), generatedRefreshToken.getExpiresAt());

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizationGrantType(authenticationToken.getGrantType());
        if (generatedAccessToken instanceof ClaimAccessor) { // 메타데이터에 저장하는 코드인데..아직 그 용도를 정확히 모르겠음
            authorizationBuilder.token(accessToken, metadata -> {
                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims());
            }).refreshToken(refreshToken).attribute("refresh_expires_in", refreshToken.getExpiresAt());
        } else {
            authorizationBuilder.accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .attribute("refresh_expires_in", refreshToken.getExpiresAt());
        }
        OAuth2Authorization authorization = authorizationBuilder.build();
        authorizationService.save(authorization);

        Map<String, Object> attrib = new HashMap<>();

        // "refresh_expires_in" 설정
        if (refreshToken.getExpiresAt() != null) {
            Instant currentInstant = Instant.now();
            Duration duration = Duration.between(currentInstant, refreshToken.getExpiresAt());
            attrib.put("refresh_expires_in", duration.getSeconds());
        }

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,
                clientPrincipal,
                accessToken,
                refreshToken,
                attrib
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrinciple = null; // client 인증에 사용(ex. "browser" 클라이언트)
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
                authentication.getPrincipal().getClass())) {
            clientPrinciple = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
            if(clientPrinciple != null && clientPrinciple.isAuthenticated()) {
                return clientPrinciple;
            }
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

}
