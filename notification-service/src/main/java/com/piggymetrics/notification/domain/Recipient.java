//수신자 정보.

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-20, 금, 11:13
수정 내용 : @Data -> @Getter 변경
*/

package com.piggymetrics.notification.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Recipient 클래스는 알림 수신자 정보를 관리.
 * MongoDB recipients 컬렉션에 매핑.
 * 주요 필드:
 * - accountName: 계정 이름
 * - email: 이메일 주소
 * - scheduledNotifications: 알림 유형과 설정의 매핑
 */
@Getter
@Builder
@Document(collection = "recipients")
public class Recipient {

	@Id
	private String accountName; // 계정 이름 (MongoDB 식별자)

	@NotNull
	@Email
	private String email; // 이메일 주소

	@Valid
	private Map<NotificationType, NotificationSettings> scheduledNotifications; // 알림 유형과 설정의 매핑
}