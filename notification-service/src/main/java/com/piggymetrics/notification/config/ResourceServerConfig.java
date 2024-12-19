//리소스 서버와 관련된 보안 설정 정의 (OAuth2 리소스 서버 설정, Security를 통한 인증 및 권한 관리)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 9:48
수정 내용 :
- OAuth2RestTemplate 제거 -> webclient 추가(비동기, 성능 최적화)
- JWT 기반 인증 설정(추후에 보고 삭제)
- 요청 헤더에서 Bearer토큰 추출 필터 추가 (BearerTokenAuthenticationFilter)
- 주석 추가

여기는 잘 몰라서 아마 후에 다시 싹 고쳐봐야 될 듯 합니다.
*/

package com.piggymetrics.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * ResourceServerConfig 클래스는 OAuth2 리소스 서버를 구성.
 *
 * 주요 역할:
 * - JWT를 사용하여 클라이언트 인증 및 권한 부여 처리
 * - WebClient를 설정하여 외부 API 요청 처리
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * WebClient 빈 생성.
     * 외부 API 호출 시 비동기 방식과 성능을 고려하여 WebClient를 사용.
     *
     * @return WebClient.Builder 인스턴스
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * JWT 인증 컨버터 설정.
     * JWT의 클레임을 권한으로 변환.
     * 이 부분은 일단 임의로 했는데 잘 몰라서 추후에 다시 변경해야 할 듯...
     *
     * @return JwtAuthenticationConverter 인스턴스
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // 권한 변환 로직 추가 가능
        return converter;
    }

    /**
     * BearerTokenAuthenticationFilter 설정.
     * 요청 헤더에서 Bearer 토큰을 추출하여 인증 과정을 처리.
     * 여기도 잘 몰라서 일단은 해놓고 확인..
     *
     * @return BearerTokenAuthenticationFilter 인스턴스
     */
    @Bean
    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter() {
        return new BearerTokenAuthenticationFilter();
    }
}
