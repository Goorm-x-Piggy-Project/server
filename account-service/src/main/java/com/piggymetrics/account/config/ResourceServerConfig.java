package com.piggymetrics.account.config;


import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ResourceServerConfig {
    @Value("${spring.security.oauth2.client.registration.account-service.client-id}")
    private String clientId;
    /**
     * SecurityFilterChain을 통해 리소스 서버 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AuthServerScopeConverter());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/account").permitAll() // 회원가입은 허용
                .requestMatchers("/", "/demo").permitAll() // "/" 및 "/demo" 경로는 인증 불필요
                .anyRequest().authenticated()             // 나머지는 인증 필요
        );
        http.csrf(csrf -> csrf.disable()); // 필요에 따라 CSRF 비활성화
        http.oauth2ResourceServer(rsc -> rsc.jwt(jwtConfigurer ->
                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        http.oauth2Client(withDefaults());

        return http.build();
    }

    /**
     * Feign 클라이언트를 위한 OAuth2AuthorizedClientManager 설정
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials() // Client Credentials Grant 설정
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    /**
     * Feign 요청 시 Access Token을 자동으로 추가하는 Interceptor 설정
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        return new OAuth2FeignRequestInterceptor(authorizedClientManager, clientId);
    }

    /**
     * RestTemplate을 통한 요청 시 Access Token 설정
     */
//    @Bean
//    public OAuth2RestTemplate oAuth2RestTemplate(ClientRegistrationRepository clientRegistrationRepository) {
//        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("default");
//        OAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest> accessTokenResponseClient =
//                new DefaultClientCredentialsTokenResponseClient();
//
//        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(clientRegistration, accessTokenResponseClient);
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//        return restTemplate;
//    }
}
