//리소스 서버와 관련된 보안 설정 정의 (OAuth2 리소스 서버 설정, Security를 통한 인증 및 권한 관리)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 15:19
수정 내용 :
- OAuth2RestTemplate 제거 -> webclient 추가(비동기, 성능 최적화)
- JWT,  Bearer 관련 삭제.
- 주석 추가
*/

package com.piggymetrics.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * ResourceServerConfig 클래스는 WebClient를 활용하여 비동기 방식으로 OAuth2 인증을 처리.
 */
@Configuration
public class ResourceServerConfig {

    /**
     * OAuth2 클라이언트 자격 증명을 관리하는 빈 생성.
     *
     * @return ClientCredentialsResourceDetails 인스턴스
     */
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    /**
     * WebClient.Builder 빈 생성.
     * - 요청 필터를 통해 Access Token을 자동으로 추가.
     *
     * @param clientCredentialsResourceDetails OAuth2 클라이언트 자격 증명
     * @return WebClient.Builder 인스턴스
     */
    @Bean
    public WebClient.Builder webClientBuilder(ClientCredentialsResourceDetails clientCredentialsResourceDetails) {
        return WebClient.builder()
                .filter(oauth2Filter(clientCredentialsResourceDetails));
    }

    /**
     * WebClient 요청 필터로 Access Token 추가.
     *
     * @param clientCredentialsResourceDetails OAuth2 클라이언트 자격 증명
     * @return ExchangeFilterFunction 인스턴스
     */
    private ExchangeFilterFunction oauth2Filter(ClientCredentialsResourceDetails clientCredentialsResourceDetails) {
        return (request, next) -> {
            // Access Token 발급
            String accessToken = new DefaultOAuth2ClientContext()
                    .getAccessTokenRequest()
                    .getExistingToken()
                    .getValue(); // Access Token 발급 로직

            // 요청 헤더에 Authorization 추가
            return next.exchange(
                    ClientRequest.from(request)
                            .header("Authorization", "Bearer " + accessToken)
                            .build()
            );
        };
    }
}
