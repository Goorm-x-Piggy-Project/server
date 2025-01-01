package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.Frequency;
import com.piggymetrics.notification.domain.NotificationSettings;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.repository.RecipientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RecipientServiceImpl의 메서드들을 검증.
 * - 수신자 검색, 업데이트, 상태 변경 등을 테스트.
 */
class RecipientServiceImplTest {

	@InjectMocks
	private RecipientServiceImpl recipientService;

	@Mock
	private RecipientRepository recipientRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * 계정 이름으로 수신자를 검색하는 메서드가 올바르게 동작하는지 확인.
	 */
	@Test
	void shouldFindByAccountName() {
		// Given: 테스트용 수신자 생성
		Recipient recipient = getMockRecipient("test-user");
		when(recipientRepository.findByAccountName("test-user")).thenReturn(Optional.of(recipient));

		// When: 수신자 검색 메서드 호출
		Recipient result = recipientService.findByAccountName("test-user");

		// Then: 결과 검증
		assertNotNull(result);
		assertEquals("test-user", result.getAccountName());
		verify(recipientRepository, times(1)).findByAccountName("test-user");
	}

	/**
	 * 알림 설정을 업데이트하는 메서드가 올바르게 동작하는지 확인.
	 */
	@Test
	void shouldUpdateNotificationSettings() {
		// Given: Mock 데이터 생성
		Recipient recipient = getMockRecipient("test-user");
		NotificationSettings newSettings = NotificationSettings.builder()
				.active(true)
				.frequency(Frequency.WEEKLY)
				.build();

		when(recipientRepository.findByAccountName("test-user")).thenReturn(Optional.of(recipient));
		when(recipientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// When: 알림 설정 업데이트 메서드 호출
		Recipient updatedRecipient = recipientService.updateNotificationSettings("test-user", NotificationType.REMIND, newSettings);

		// Then: 결과 검증
		assertNotNull(updatedRecipient);
		assertEquals(true, updatedRecipient.getScheduledNotifications().get(NotificationType.REMIND).getActive());
		verify(recipientRepository, times(1)).save(any());
	}

	/**
	 * 특정 알림 유형의 마지막 발송 시간을 업데이트하는 메서드가 올바르게 동작하는지 확인.
	 */
	@Test
	void shouldMarkNotified() {
		// Given: Mock 데이터 생성
		Recipient recipient = getMockRecipient("test-user");
		Date now = new Date();

		when(recipientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// When: 상태 업데이트 메서드 호출
		recipientService.markNotified(NotificationType.REMIND, recipient);

		// Then: 결과 검증
		NotificationSettings settings = recipient.getScheduledNotifications().get(NotificationType.REMIND);
		assertNotNull(settings.getLastNotified());
		verify(recipientRepository, times(1)).save(any());
	}

	/**
	 * 테스트용 Mock Recipient 객체 생성.
	 * @param accountName 계정 이름
	 * @return Recipient 객체
	 */
	private Recipient getMockRecipient(String accountName) {
		NotificationSettings remindSettings = NotificationSettings.builder()
				.active(true)
				.frequency(Frequency.WEEKLY)
				.lastNotified(null)
				.build();

		NotificationSettings backupSettings = NotificationSettings.builder()
				.active(false)
				.frequency(Frequency.MONTHLY)
				.lastNotified(null)
				.build();

		return Recipient.builder()
				.accountName(accountName)
				.email(accountName + "@test.com")
				.scheduledNotifications(Map.of(
						NotificationType.REMIND, remindSettings,
						NotificationType.BACKUP, backupSettings
				))
				.build();
	}
}
