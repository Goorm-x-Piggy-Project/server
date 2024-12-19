//알림 비즈니스 로직 처리. (수신자와 알림 설정 기반으로 이메일 전송)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 14:52
수정 내용 : @Autowired 제거, 생성자 주입 사용, 주석 추가
*/

package com.piggymetrics.notification.service;

import com.piggymetrics.notification.client.AccountServiceClient;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * NotificationServiceImpl 클래스는 NotificationService 인터페이스를 구현하며,
 * 백업 및 리마인드 알림 발송 로직을 제공.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

	private final AccountServiceClient client;
	private final RecipientService recipientService;
	private final EmailService emailService;

	/**
	 * NotificationServiceImpl 생성자.
	 *
	 * @param client AccountServiceClient 인스턴스
	 * @param recipientService RecipientService 인스턴스
	 * @param emailService EmailService 인스턴스
	 */
	public NotificationServiceImpl(AccountServiceClient client, RecipientService recipientService, EmailService emailService) {
		this.client = client;
		this.recipientService = recipientService;
		this.emailService = emailService;
	}

	/**
	 * 백업 알림 발송.
	 *
	 * - 주기적으로 데이터 백업을 알림.
	 */
	@Override
	@Scheduled(cron = "${backup.cron}")
	public void sendBackupNotifications() {
		final NotificationType type = NotificationType.BACKUP;
		List<Recipient> recipients = recipientService.findReadyToNotify(type);

		recipients.forEach(recipient -> CompletableFuture.runAsync(() -> {
			try {
				emailService.send(type, recipient, null);
				log.info("백업 알림이 {}에게 성공적으로 발송되었습니다.", recipient.getAccountName());
			} catch (Exception e) {
				log.error("백업 알림 발송 중 오류 발생: {}", recipient.getAccountName(), e);
			}
		}));
	}

	/**
	 * 리마인드 알림 발송.
	 *
	 * - 특정 이벤트를 사용자에게 상기시키기 위한 알림.
	 */
	@Override
	@Scheduled(cron = "${remind.cron}")
	public void sendRemindNotifications() {
		final NotificationType type = NotificationType.REMIND;
		List<Recipient> recipients = recipientService.findReadyToNotify(type);

		recipients.forEach(recipient -> CompletableFuture.runAsync(() -> {
			try {
				emailService.send(type, recipient, null);
				log.info("리마인드 알림이 {}에게 성공적으로 발송되었습니다.", recipient.getAccountName());
			} catch (Exception e) {
				log.error("리마인드 알림 발송 중 오류 발생: {}", recipient.getAccountName(), e);
			}
		}));
	}
}