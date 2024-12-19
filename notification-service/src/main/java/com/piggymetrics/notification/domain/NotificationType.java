//알림의 유형을 나타냄. (BACKUP, REMIND..)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 21:45
수정 내용 : 롬복 적용
*/

package com.piggymetrics.notification.domain;

/**
 * NotificationType 열거형은 다양한 알림 유형을 정의.
 * 각 유형은 이메일의 제목, 본문 텍스트, 첨부 파일 정보 포함.
 */
public enum NotificationType {

	/**
	 * BACKUP 알림 유형:
	 * - 이메일 제목: "backup.email.subject"
	 * - 이메일 본문: "backup.email.text"
	 * - 첨부 파일: "backup.email.attachment"
	 */
	BACKUP("backup.email.subject", "backup.email.text", "backup.email.attachment"),

	/**
	 * REMIND 알림 유형:
	 * - 이메일 제목: "remind.email.subject"
	 * - 이메일 본문: "remind.email.text"
	 * - 첨부 파일: 없음 (null)
	 */
	REMIND("remind.email.subject", "remind.email.text", null);

	private final String subject; // 이메일 제목
	private final String text; // 이메일 본문 텍스트
	private final String attachment; // 첨부 파일 이름 (nullable)

	/**
	 * NotificationType 생성자.
	 *
	 * @param subject 이메일 제목
	 * @param text 이메일 본문 텍스트
	 * @param attachment 첨부 파일 이름
	 */
	NotificationType(String subject, String text, String attachment) {
		this.subject = subject;
		this.text = text;
		this.attachment = attachment;
	}

	/**
	 * 이메일 제목 반환.
	 *
	 * @return 이메일 제목
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 이메일 본문 텍스트 반환.
	 *
	 * @return 이메일 본문 텍스트
	 */
	public String getText() {
		return text;
	}

	/**
	 * 첨부 파일 이름 반환.
	 *
	 * @return 첨부 파일 이름 (없으면 null 반환)
	 */
	public String getAttachment() {
		return attachment;
	}
}

