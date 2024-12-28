// Spring Boot 애플리케이션의 진입점
package com.piggymetrics.notification;

import com.piggymetrics.notification.repository.converter.FrequencyReaderConverter;
import com.piggymetrics.notification.repository.converter.FrequencyWriterConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

/**
 * NotificationServiceApplication 클래스는 Spring Boot 애플리케이션의 진입점입니다.
 * 주요 역할:
 * - Spring Boot 애플리케이션 실행
 * - 스케줄링 및 OAuth2 활성화
 * - MongoDB 커스텀 변환기 등록
 */
@SpringBootApplication
@EnableDiscoveryClient // 서비스 디스커버리 활성화
@EnableFeignClients // Feign 클라이언트를 통한 외부 API 호출 지원
@EnableScheduling // 스케줄링 활성화
@EnableMethodSecurity(prePostEnabled = true) // 메서드 수준 보안 활성화
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	/**
	 * WebClient.Builder 빈 등록.
	 * - 외부 API 호출 시 비동기 방식과 성능 최적화를 고려.
	 *
	 * @return WebClient.Builder 인스턴스
	 */
	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	/**
	 * MongoDB 커스텀 변환기를 등록합니다.
	 * - FrequencyReaderConverter: MongoDB에서 데이터를 읽을 때 변환.
	 * - FrequencyWriterConverter: 데이터를 MongoDB로 저장할 때 변환.
	 *
	 * @return 커스텀 변환기 리스트를 포함한 CustomConversions 객체
	 */
	@Bean
	public MongoCustomConversions customConversions() {
		return new MongoCustomConversions(Arrays.asList(
				new FrequencyReaderConverter(),
				new FrequencyWriterConverter()
		));
	}
}
