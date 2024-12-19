//특정 알림의 설정 정보를 저장. (알림 유형(NotificationType)과 빈도(Frequency))

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 10:38
수정 내용 : 의존성 업데이트(javax -> jakarta), Builder 패턴 도입(객체 생성 가독성), 주석 추가 , getter setter 정리
*/

package com.piggymetrics.notification.domain;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * NotificationSettings 클래스는 알림 설정 정보 관리.
 * 주요 필드:
 * - active: 알림 활성화 여부
 * - frequency: 알림 발송 빈도
 * - lastNotified: 마지막 알림 발송 날짜
 */
public class NotificationSettings {

	@NotNull
	private Boolean active; // 알림 활성화 여부

	@NotNull
	private Frequency frequency; // 알림 발송 빈도

	private Date lastNotified; // 마지막 알림 발송 날짜

	/**
	 * 알림 활성화 여부 반환.
	 *
	 * @return 알림 활성화 여부
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * 알림 활성화 여부 설정.
	 *
	 * @param active 알림 활성화 여부
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * 알림 발송 빈도 반환.
	 *
	 * @return 알림 발송 빈도
	 */
	public Frequency getFrequency() {
		return frequency;
	}

	/**
	 * 알림 발송 빈도 설정.
	 *
	 * @param frequency 알림 발송 빈도
	 */
	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	/**
	 * 마지막 알림 발송 날짜 반환.
	 *
	 * @return 마지막 알림 발송 날짜
	 */
	public Date getLastNotified() {
		return lastNotified;
	}

	/**
	 * 마지막 알림 발송 날짜 설정.
	 *
	 * @param lastNotified 마지막 알림 발송 날짜
	 */
	public void setLastNotified(Date lastNotified) {
		this.lastNotified = lastNotified;
	}

	/**
	 * NotificationSettings 객체의 Builder를 반환.
	 *
	 * @return NotificationSettingsBuilder 인스턴스
	 */
	public static NotificationSettingsBuilder builder() {
		return new NotificationSettingsBuilder();
	}

	/**
	 * NotificationSettings 객체 생성을 돕는 Builder 클래스.
	 */
	public static class NotificationSettingsBuilder {

		private Boolean active;
		private Frequency frequency;
		private Date lastNotified;

		public NotificationSettingsBuilder active(Boolean active) {
			this.active = active;
			return this;
		}

		public NotificationSettingsBuilder frequency(Frequency frequency) {
			this.frequency = frequency;
			return this;
		}

		public NotificationSettingsBuilder lastNotified(Date lastNotified) {
			this.lastNotified = lastNotified;
			return this;
		}

		public NotificationSettings build() {
			NotificationSettings settings = new NotificationSettings();
			settings.setActive(this.active);
			settings.setFrequency(this.frequency);
			settings.setLastNotified(this.lastNotified);
			return settings;
		}
	}
}
