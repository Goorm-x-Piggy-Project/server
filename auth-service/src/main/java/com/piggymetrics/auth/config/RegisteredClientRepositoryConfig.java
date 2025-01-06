package com.piggymetrics.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class RegisteredClientRepositoryConfig {

    private final Environment env;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient browserClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("browser")
                .clientSecret(passwordEncoder.encode(env.getProperty("BROWSER_CLIENT_PASSWORD")))
                // 원래는 public client라 ClientAuthenticationMethod.CLIENT_SECRET_BASIC이 아니라 NONE으로 하고 PKCE 방식으로 해야함
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(new AuthorizationGrantType("password")) // 서드파티가 아닌 같은 서비스이므로 password 사용
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scopes(scopes -> scopes.add("ui")) // 클라이언트가 요청할 수 있는 권한의 범위
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT 사용
                        .build())
                .build();

        RegisteredClient accountServiceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("account-service")
                .clientSecret(passwordEncoder.encode(env.getProperty("ACCOUNT_SERVICE_PASSWORD")))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("server"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT 사용
                        .build())
                .build();

        RegisteredClient statisticsServiceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("statistics-service")
                .clientSecret(passwordEncoder.encode(env.getProperty("STATISTICS_SERVICE_PASSWORD")))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("server"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT 사용
                        .build())
                .build();

        RegisteredClient notificationServiceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("notification-service")
                .clientSecret(passwordEncoder.encode(env.getProperty("NOTIFICATION_SERVICE_PASSWORD")))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("server"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT 사용
                        .build())
                .build();

        // TODO: JDBCRegisteredClientRepository 로 변경
        return new InMemoryRegisteredClientRepository(browserClient, accountServiceClient, statisticsServiceClient, notificationServiceClient);
    }
}
