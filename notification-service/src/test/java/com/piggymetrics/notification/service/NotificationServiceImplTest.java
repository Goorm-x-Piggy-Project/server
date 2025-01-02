package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * NotificationServiceImpl의 알림 발송 로직을 검증.
 * - 백업 및 리마인드 알림 발송 메서드 테스트
 */
class NotificationServiceImplTest {

	@InjectMocks
	private NotificationServiceImpl notificationService;

	@Mock
	private RecipientService recipientService;

	@Mock
	private EmailService emailService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * 백업 알림 발송 메서드가 수신자 목록에 대해 올바르게 동작하는지 확인.
	 */
	@Test
	void shouldSendBackupNotifications() throws Exception {
		// Given: 테스트용 수신자 목록 생성
		Recipient recipient1 = getMockRecipient("user1");
		Recipient recipient2 = getMockRecipient("user2");
		List<Recipient> recipients = Arrays.asList(recipient1, recipient2);

		when(recipientService.findReadyToNotify(NotificationType.BACKUP)).thenReturn(recipients);

		// When: 백업 알림 발송 메서드 호출
		notificationService.sendBackupNotifications();

		// Then: 각 수신자에 대해 이메일 발송 및 상태 업데이트 검증
		verify(emailService, times(1)).send(NotificationType.BACKUP, recipient1, null);
		verify(emailService, times(1)).send(NotificationType.BACKUP, recipient2, null);
		verify(recipientService, times(1)).markNotified(NotificationType.BACKUP, recipient1);
		verify(recipientService, times(1)).markNotified(NotificationType.BACKUP, recipient2);
	}

	/**
	 * 리마인드 알림 발송 메서드가 수신자 목록에 대해 올바르게 동작하는지 확인.
	 */
	@Test
	void shouldSendRemindNotifications() throws Exception {
		// Given: 테스트용 수신자 목록 생성
		Recipient recipient1 = getMockRecipient("user1");
		Recipient recipient2 = getMockRecipient("user2");
		List<Recipient> recipients = Arrays.asList(recipient1, recipient2);

		// When: findReadyToNotify 메소드가 호출될 때, 수신자 목록 반환
		when(recipientService.findReadyToNotify(NotificationType.REMIND)).thenReturn(recipients);

		// 리마인드 알림 발송 메서드 호출
		notificationService.sendRemindNotifications();

		// Then: 각 수신자에 대해 이메일 발송 및 상태 업데이트 검증
		verify(emailService, times(1)).send(NotificationType.REMIND, recipient1, null);
		verify(emailService, times(1)).send(NotificationType.REMIND, recipient2, null);
		verify(recipientService, times(1)).markNotified(NotificationType.REMIND, recipient1);
		verify(recipientService, times(1)).markNotified(NotificationType.REMIND, recipient2);
	}

	/**
	 * 테스트용 Mock Recipient 객체 생성.
	 * @param accountName 계정 이름
	 * @return Recipient 객체
	 */
	private Recipient getMockRecipient(String accountName) {
		return Recipient.builder()
				.accountName(accountName)
				.email(accountName + "@test.com")
				.build();
	}
}
