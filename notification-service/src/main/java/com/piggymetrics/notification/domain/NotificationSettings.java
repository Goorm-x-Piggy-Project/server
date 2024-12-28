// 특정 알림의 설정 정보를 저장

package com.piggymetrics.notification.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/**
 * NotificationSettings 클래스는 알림 설정 정보를 관리합니다.
 * - 활성화 상태, 발송 빈도, 마지막 발송 시간을 포함합니다.
 */
@Getter
@Builder(toBuilder = true) // 기존 객체 복사 및 변경 가능
public class NotificationSettings {

	@NotNull
	private final Boolean active; // 알림 활성화 여부

	@NotNull
	private final Frequency frequency; // 알림 발송 빈도

	private final Date lastNotified; // 마지막 알림 발송 날짜 (nullable)

//	/**
//	 * 알림이 활성화되어 있는지 확인합니다.
//	 *
//	 * @return 활성화 여부
//	 */
//	public boolean isActive() {
//		return Boolean.TRUE.equals(active);
//	}
//
//	/**
//	 * 알림 설정이 마지막 발송 시간 이전인지 확인합니다.
//	 *
//	 * @param date 기준 날짜
//	 * @return true면 기준 날짜 이전, false면 이후
//	 */
//	public boolean isBeforeLastNotified(Date date) {
//		return lastNotified == null || lastNotified.before(date);
//	}

	/**
	 * 마지막 알림 발송 시간을 업데이트한 새로운 설정 객체를 반환합니다.
	 *
	 * @param date 새 발송 시간
	 * @return 업데이트된 NotificationSettings 객체
	 */
	public NotificationSettings updateLastNotified(Date date) {
		return this.toBuilder().lastNotified(date).build();
	}
}
