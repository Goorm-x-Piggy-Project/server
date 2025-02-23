package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationSettings;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;

import java.util.List;

/**
 * RecipientService 인터페이스는 수신자 관리와 알림 발송 준비 상태 처리 정의.
 */
public interface RecipientService {

	/**
	 * 계정 이름으로 수신자 정보 검색.
	 *
	 * @param accountName 계정 이름
	 * @return 검색된 Recipient 객체
	 */
	Recipient findByAccountName(String accountName);

	/**
	 * 현재 알림 발송 준비가 된 수신자 목록 검색.
	 *
	 * @param type 알림 유형
	 * @return 알림 발송 준비가 된 Recipient 목록
	 */
	List<Recipient> findReadyToNotify(NotificationType type);

	/**
	 * 특정 알림 유형의 설정을 업데이트한 수신자 정보를 저장.
	 *
	 * @param accountName 계정 이름
	 * @param type 알림 유형
	 * @param settings 업데이트할 NotificationSettings 객체
	 * @return 업데이트된 Recipient 객체
	 */
	Recipient updateNotificationSettings(String accountName, NotificationType type, NotificationSettings settings);

	/**
	 * 특정 알림 유형의 마지막 발송 시간을 현재 시간으로 기록.
	 *
	 * @param type 알림 유형
	 * @param recipient 발송 시간을 업데이트할 Recipient 객체
	 */
	void markNotified(NotificationType type, Recipient recipient);
}
