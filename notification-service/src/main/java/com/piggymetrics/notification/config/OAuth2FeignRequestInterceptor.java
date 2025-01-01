package com.piggymetrics.notification.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final String clientRegistrationId;

    public OAuth2FeignRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager,
                                         String clientRegistrationId) {
        this.authorizedClientManager = authorizedClientManager;
        this.clientRegistrationId = clientRegistrationId;
    }

    @Override
    public void apply(RequestTemplate template) {
        log.info("feignClient 요청 전 인터셉터 호출");
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                .principal("anonymous") // client credentials 방식이므로 인증된 사용자가 아니기 때문에 anonymous로 설정
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            log.error("client {}에 대해 access token 발급 실패", clientRegistrationId);
            // 나중에 전역 예외 처리 필요
            throw new IllegalStateException("Unable to obtain access token for client: " + clientRegistrationId);
        }

        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        log.info("발급된 access token: {}", accessToken);
        template.header("Authorization", "Bearer " + accessToken);

        log.info("Bearer 토큰 확인: {}", template.headers().get("Authorization"));
    }
}
