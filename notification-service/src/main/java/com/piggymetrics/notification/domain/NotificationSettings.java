//특정 알림의 설정 정보를 저장. (알림 유형(NotificationType)과 빈도(Frequency))

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-20, 금, 13:31
수정 내용 : toBuilder()추가
*/

package com.piggymetrics.notification.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Date;
import lombok.Getter;

/**
 * NotificationSettings 클래스는 알림 설정 정보 관리.
 * 주요 필드:
 * - active: 알림 활성화 여부
 * - frequency: 알림 발송 빈도
 * - lastNotified: 마지막 알림 발송 날짜
 */
@Getter
@Builder(toBuilder = true) // 기존 객체를 복사하면서 특정 필드만 변경 가능
public class NotificationSettings {

	@NotNull
	private Boolean active; // 알림 활성화 여부

	@NotNull
	private Frequency frequency; // 알림 발송 빈도

	private Date lastNotified; // 마지막 알림 발송 날짜

	// Builder를 통해 lastNotified를 변경하는 방식
	public NotificationSettings updateLastNotified(Date lastNotified) {
		return this.toBuilder().lastNotified(lastNotified).build();
	}
}