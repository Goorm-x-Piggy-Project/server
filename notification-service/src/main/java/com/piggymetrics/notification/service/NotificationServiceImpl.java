// 알림 비즈니스 로직 처리 (수신자와 알림 설정 기반으로 이메일 전송)
package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * NotificationServiceImpl 클래스는 NotificationService 인터페이스를 구현하며,
 * 백업 및 리마인드 알림 발송 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

	private final RecipientService recipientService;
	private final EmailService emailService;

	/**
	 * 백업 알림 발송.
	 * - 주기적으로 데이터 백업을 알림.
	 */
	@Override
	@Scheduled(cron = "${backup.cron}")
	public void sendBackupNotifications() {
		sendNotifications(NotificationType.BACKUP);
	}

	/**
	 * 리마인드 알림 발송.
	 * - 특정 이벤트를 사용자에게 상기시키기 위한 알림.
	 */
	@Override
	@Scheduled(cron = "${remind.cron}")
	public void sendRemindNotifications() {
		sendNotifications(NotificationType.REMIND);
	}

	/**
	 * 알림 발송 로직을 처리합니다.
	 *
	 * @param type 알림 유형
	 */
	private void sendNotifications(NotificationType type) {
		List<Recipient> recipients = recipientService.findReadyToNotify(type);

		recipients.forEach(recipient -> CompletableFuture.runAsync(() -> {
			try {
				emailService.send(type, recipient, null);
				recipientService.markNotified(type, recipient);
				log.info("{} 알림이 {}에게 성공적으로 발송되었습니다.", type, recipient.getAccountName());
			} catch (Exception e) {
				log.error("{} 알림 발송 중 오류 발생: {}", type, recipient.getAccountName(), e);
			}
		}));
	}
}
