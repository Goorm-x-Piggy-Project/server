// OAuth2 인증을 처리하기 위한 리소스 서버 설정

package com.piggymetrics.notification.config;

import static org.springframework.security.config.Customizer.withDefaults;

import feign.RequestInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.reactive.function.client.ClientRequest;
//import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.WebClient;

/**
 * ResourceServerConfig 클래스는 OAuth2를 활용한 인증을 처리하기 위한 설정.
 * WebClient를 활용하여 비동기 요청 처리와 Access Token 추가를 지원.
 */
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
                .requestMatchers("/", "/demo").permitAll() // "/" 및 "/demo" 경로는 인증 불필요
                .anyRequest().authenticated()             // 나머지는 인증 필요
        );
//        http.csrf(AbstractHttpConfigurer::disable); // 필요에 따라 CSRF 비활성화
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
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
}
