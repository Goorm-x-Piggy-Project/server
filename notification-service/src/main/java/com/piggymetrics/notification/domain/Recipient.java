// 수신자 정보 관리 클래스

package com.piggymetrics.notification.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.Map;

/**
 * Recipient 클래스는 알림 수신자 정보를 관리하며,
 * MongoDB recipients 컬렉션에 매핑됩니다.
 */
@Getter
@Builder(toBuilder = true) // 기존 객체 복사 및 필드 업데이트 가능
@Document(collection = "recipients")
public class Recipient {

	@Id
	private final String accountName; // 계정 이름 (MongoDB 식별자)

	@NotNull
	@Email
	private final String email; // 이메일 주소

	@Valid
	private final Map<NotificationType, NotificationSettings> scheduledNotifications; // 알림 유형과 설정의 매핑

	/**
	 * 알림 설정을 안전하게 반환합니다.
	 *
	 * @return 읽기 전용 알림 설정 맵
	 */
	public Map<NotificationType, NotificationSettings> getScheduledNotifications() {
		return scheduledNotifications != null ? Collections.unmodifiableMap(scheduledNotifications) : Collections.emptyMap();
	}

	/**
	 * 특정 알림 유형의 설정을 업데이트한 새 Recipient 객체를 반환합니다.
	 *
	 * @param type 알림 유형
	 * @param settings 업데이트할 NotificationSettings 객체
	 * @return 업데이트된 Recipient 객체
	 */
	public Recipient updateNotificationSettings(NotificationType type, NotificationSettings settings) {
		Map<NotificationType, NotificationSettings> updatedSettings = new java.util.HashMap<>(this.scheduledNotifications);
		updatedSettings.put(type, settings);

		return this.toBuilder()
				.scheduledNotifications(Collections.unmodifiableMap(updatedSettings))
				.build();
	}
}
