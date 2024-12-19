//수신자 정보.

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 14:36
수정 내용 : 의존성 업데이트(javax -> jakarta), getter setter 정리, Builder 추가, 주석 추가,
*/

package com.piggymetrics.notification.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
@Document(collection = "recipients")
public class Recipient {

	@Id
	private String accountName; // 계정 이름 (MongoDB 식별자)

	@NotNull
	@Email
	private String email; // 이메일 주소

	@Valid
	private Map<NotificationType, NotificationSettings> scheduledNotifications; // 알림 유형과 설정의 매핑

	/**
	 * 계정 이름 반환.
	 *
	 * @return 계정 이름
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * 계정 이름 설정.
	 *
	 * @param accountName 계정 이름
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * 이메일 주소 반환.
	 *
	 * @return 이메일 주소
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 이메일 주소 설정.
	 *
	 * @param email 이메일 주소
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 알림 유형과 설정의 매핑을 반환.
	 *
	 * @return 알림 유형과 설정의 매핑
	 */
	public Map<NotificationType, NotificationSettings> getScheduledNotifications() {
		return scheduledNotifications;
	}

	/**
	 * 알림 유형과 설정의 매핑을 설정.
	 *
	 * @param scheduledNotifications 알림 유형과 설정의 매핑
	 */
	public void setScheduledNotifications(Map<NotificationType, NotificationSettings> scheduledNotifications) {
		this.scheduledNotifications = scheduledNotifications;
	}

	/**
	 * Recipient 객체 생성을 돕는 Builder를 반환.
	 *
	 * @return RecipientBuilder 인스턴스
	 */
	public static RecipientBuilder builder() {
		return new RecipientBuilder();
	}

	/**
	 * Recipient 객체 생성을 위한 Builder 클래스.
	 */
	public static class RecipientBuilder {

		private String accountName;
		private String email;
		private Map<NotificationType, NotificationSettings> scheduledNotifications;

		public RecipientBuilder accountName(String accountName) {
			this.accountName = accountName;
			return this;
		}

		public RecipientBuilder email(String email) {
			this.email = email;
			return this;
		}

		public RecipientBuilder scheduledNotifications(Map<NotificationType, NotificationSettings> scheduledNotifications) {
			this.scheduledNotifications = scheduledNotifications;
			return this;
		}

		public Recipient build() {
			Recipient recipient = new Recipient();
			recipient.setAccountName(this.accountName);
			recipient.setEmail(this.email);
			recipient.setScheduledNotifications(this.scheduledNotifications);
			return recipient;
		}
	}
}