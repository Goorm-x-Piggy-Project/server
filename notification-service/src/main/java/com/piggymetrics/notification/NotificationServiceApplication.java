/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 9:36
수정 내용 : 코드 정리, 의존성 업데이트(javax -> jakarta), 주석 처리
*/

package com.piggymetrics.notification;

import com.piggymetrics.notification.repository.converter.FrequencyReaderConverter;
import com.piggymetrics.notification.repository.converter.FrequencyWriterConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

/**
 * NotificationServiceApplication 클래스는 Spring Boot 애플리케이션의 진입점.
 * 주요 역할:
 * - Spring Boot 애플리케이션 실행
 * - 스케줄링 활성화
 * - MongoDB 커스텀 변환기 등록
 * - 클라우드 클라이언트 및 Feign 클라이언트 설정
 */
@SpringBootApplication
@EnableDiscoveryClient // 서비스 디스커버리를 활성화
@EnableOAuth2Client // OAuth2 클라이언트 지원 활성화
@EnableFeignClients // Feign 클라이언트를 통한 외부 API 호출 지원
@EnableScheduling // 스케줄링 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메서드 수준의 보안 활성화
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	/**
	 * MongoDB 커스텀 변환기를 등록.
	 * - FrequencyReaderConverter: MongoDB에서 데이터를 읽을 때 변환.
	 * - FrequencyWriterConverter: 데이터를 MongoDB로 저장할 때 변환.
	 *
	 * @return 커스텀 변환기 리스트를 포함한 CustomConversions 객체
	 */
	@Bean
	public CustomConversions customConversions() {
		return new CustomConversions(Arrays.asList(
				new FrequencyReaderConverter(),
				new FrequencyWriterConverter()
		));
	}
}
