package com.piggymetrics.account.config;


import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {
    private final String CLIENT_ID = "account-service";
    /**
     * SecurityFilterChain을 통해 리소스 서버 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/account").permitAll() // 회원가입은 허용
                .requestMatchers("/", "/demo", "/actuator/**").permitAll() // actuator 경로 허용
                .anyRequest().authenticated()             // 나머지는 인증 필요
        );
        http.csrf(AbstractHttpConfigurer::disable); // 필요에 따라 CSRF 비활성화
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(withDefaults())
        );
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
        return new OAuth2FeignRequestInterceptor(authorizedClientManager, CLIENT_ID);
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
