package com.piggymetrics.account.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .decoder(jwtDecoder())
//                        )
//                );
        return http.build();
    }

//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
//                .clientCredentials()
//                .build();
//
//        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }
//
//    @Bean
//    public WebClient webClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
//        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2FilterFunction =
//                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
//        oauth2FilterFunction.setDefaultOAuth2AuthorizedClient(true);
//
//        return WebClient.builder()
//                .filter(oauth2FilterFunction)
//                .build();
//    }
//
//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager authorizedClientManagers(
//            ReactiveClientRegistrationRepository clientRegistrationRepository,
//            ServerOAuth2AuthorizedClientRepository authorizedClientService) {
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
//                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                        .clientCredentials() // 클라이언트 자격 증명 플로우
//                        .build();
//
//        // DefaultReactiveOAuth2AuthorizedClientManager 생성
//        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultReactiveOAuth2AuthorizedClientManager(
//                        clientRegistrationRepository, authorizedClientService);
//
//        // AuthorizedClientProvider 설정
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return org.springframework.security.oauth2.jwt.NimbusJwtDecoder
                .withJwkSetUri("https://issuer-url/.well-known/jwks.json")
                .build();
    }


}
