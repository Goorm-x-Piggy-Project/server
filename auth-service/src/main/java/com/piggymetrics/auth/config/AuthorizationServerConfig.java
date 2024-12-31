package com.piggymetrics.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.piggymetrics.auth.service.security.MongoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthorizationServerConfig {

    private final Environment env;
    private final MongoUserDetailsService userDetailsService;

    // TODO: JdbcOAuth2AuthorizationService 사용
    @Bean // Access token의 저장 및 관리를 담당
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public OAuth2TokenGenerator<OAuth2Token> tokenGenerator() {
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean(name = "customAuthorizationServerSecurityFilterChain")
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults()) // OpenID Connect 1.0 사용
                .tokenEndpoint(oAuth2TokenEndpointConfigurer -> {
                    oAuth2TokenEndpointConfigurer
                            .accessTokenRequestConverter(new PasswordAuthenticationConverter()) // HttpServletRequest로부터 인증 정보를 추출하여 Authentication 객체로 변환하는 역할
                            .authenticationProvider(new PasswordAuthenticationProvider(authorizationService(), tokenGenerator(), userDetailsService, passwordEncoder())); // 인증 수행하는 AuthenticationProvider를 등록
                });
//                .tokenIntrospectionEndpoint(oAuth2TokenIntrospectionEndpointConfigurer -> {
//                    oAuth2TokenIntrospectionEndpointConfigurer
//                            .introspectionRequestConverter(new CustomIntrospectionAuthenticationConverter()) // HttpServletRequest로부터 인증 정보를 추출하여 Authentication 객체로 변환하는 역할
//                            .authenticationProvider(new CustomIntrospectionAuthenticationProvider(authorizationService(), registeredClientRepository())); // 인증 수행하는 AuthenticationProvider를 등록
//                });


        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
        );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/oauth2/token", "/oauth2/authorize", "/.well-known/openid-configuration").permitAll()
//                        .anyRequest().authenticated() // 모든 요청 인증 필요
                            .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient browserClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("browser")
                .clientSecret(passwordEncoder().encode(env.getProperty("BROWSER_CLIENT_PASSWORD")))
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
                .clientSecret(passwordEncoder().encode(env.getProperty("ACCOUNT_SERVICE_PASSWORD")))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("server"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT 사용
                        .build())
                .build();

        RegisteredClient statisticsServiceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("statistics-service")
                .clientSecret(passwordEncoder().encode(env.getProperty("STATISTICS_SERVICE_PASSWORD")))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("server"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT 사용
                        .build())
                .build();

        RegisteredClient notificationServiceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("notification-service")
                .clientSecret(passwordEncoder().encode(env.getProperty("NOTIFICATION_SERVICE_PASSWORD")))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(scopes -> scopes.add("server"))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT 사용
                        .build())
                .build();

        // TODO: JDBCRegisteredClientRepository 로 변경
        return new InMemoryRegisteredClientRepository(browserClient, accountServiceClient, statisticsServiceClient, notificationServiceClient);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT의 서명을 위한 개인키와 공개키를 생성하고 JWKSet을 생성하여 반환
     */

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://auth-service:5000/uaa") // Replace with your authorization server URL
                .build();
    }

    // JWT payload에 custom claim(field-value pair) 추가해야 하는 경우 사용
//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
//        return context -> {
//            if (context.getPrincipal() != null) {
//                context.getClaims().claim("custom-claim", "custom-value");
//            }
//        };
//    }
}