/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 14:49
수정 내용 : 주석 추가
*/


package com.piggymetrics.notification.service;

/**
 * NotificationService 인터페이스는 알림 발송 관련 기능 정의.
 * 주요 역할:
 * - 백업 알림 발송
 * - 리마인드 알림 발송
 */
public interface NotificationService {

	/**
	 * 백업 알림 발송.
	 */
	void sendBackupNotifications();

	/**
	 * 리마인드 알림 발송.
	 */
	void sendRemindNotifications();
}