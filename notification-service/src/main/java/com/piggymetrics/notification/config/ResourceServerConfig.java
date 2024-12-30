// OAuth2 인증을 처리하기 위한 리소스 서버 설정

package com.piggymetrics.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * ResourceServerConfig 클래스는 OAuth2를 활용한 인증을 처리하기 위한 설정.
 * WebClient를 활용하여 비동기 요청 처리와 Access Token 추가를 지원.
 */
@Configuration
public class ResourceServerConfig {

    /**
     * OAuth2 클라이언트 자격 증명 설정.
     *
     * @return OAuth2 클라이언트 자격 증명 객체
     */
    //@ConfigurationProperties 사용 시, security.oauth2.client이 application.yml에 있어야 함.(수정하기)
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    /**
     * WebClient.Builder 빈 생성.
     * - Access Token을 요청에 자동으로 추가.
     *
     * @return WebClient.Builder 인스턴스
     */
    @Bean(name = "AccessWebClientBuilder")
    public WebClient.Builder webClientBuilder(ClientCredentialsResourceDetails clientDetails) {
        return WebClient.builder().filter(oauth2Filter(clientDetails));
    }

    /**
     * WebClient 요청 필터로 Access Token 추가.
     *
     * @param clientDetails OAuth2 클라이언트 자격 증명
     * @return 필터 함수
     */
    private ExchangeFilterFunction oauth2Filter(ClientCredentialsResourceDetails clientDetails) {
        return (request, next) -> {
            String accessToken = clientDetails.getAccessTokenUri(); // Access Token 로직 수정

            return next.exchange(
                    ClientRequest.from(request)
                            .header("Authorization", "Bearer " + accessToken)
                            .build()
            );
        };
    }
}
