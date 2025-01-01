package com.piggymetrics.notification.repository;

import com.piggymetrics.notification.domain.Frequency;
import com.piggymetrics.notification.domain.NotificationSettings;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * MongoDB 쿼리를 사용하는 RecipientRepository의 커스텀 메서드를 검증.
 * - 백업 알림 및 리마인드 알림 준비 상태 확인
 */
class RecipientRepositoryTest {

	@InjectMocks
	private RecipientRepositoryImpl recipientRepository;

	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * 백업 알림 발송 준비 상태의 수신자를 올바르게 검색하는지 확인.
	 */
	@Test
	void shouldFindReadyForBackup() {
		// Given: Mock 데이터 생성
		Date currentDate = new Date();
		NotificationSettings backupSettings = NotificationSettings.builder()
				.active(true)
				.frequency(Frequency.MONTHLY)
				.lastNotified(new Date(currentDate.getTime() - (31L * 24 * 60 * 60 * 1000)))
				.build();

		Recipient recipient = Recipient.builder()
				.accountName("test-user")
				.email("test@test.com")
				.scheduledNotifications(Map.of(NotificationType.BACKUP, backupSettings))
				.build();

		Query query = Query.query(
				Criteria.where("scheduledNotifications.BACKUP.active").is(true)
						.and("scheduledNotifications.BACKUP.lastNotified").lt(currentDate)
		);

		// Mock 동작 정의
		when(mongoTemplate.query(Recipient.class).matching(query).all())
				.thenReturn(List.of(recipient));

		// When: 메서드 호출
		List<Recipient> recipients = recipientRepository.findReadyForBackup(currentDate);

		// Then: 결과 검증
		assertNotNull(recipients);
		assertEquals(1, recipients.size());
		assertEquals("test-user", recipients.get(0).getAccountName());
	}

	/**
	 * 리마인드 알림 발송 준비 상태의 수신자를 올바르게 검색하는지 확인.
	 */
	@Test
	void shouldFindReadyForRemind() {
		// Given: Mock 데이터 생성
		Date currentDate = new Date();
		NotificationSettings remindSettings = NotificationSettings.builder()
				.active(true)
				.frequency(Frequency.WEEKLY)
				.lastNotified(new Date(currentDate.getTime() - (8L * 24 * 60 * 60 * 1000)))
				.build();

		Recipient recipient = Recipient.builder()
				.accountName("test-user")
				.email("test@test.com")
				.scheduledNotifications(Map.of(NotificationType.REMIND, remindSettings))
				.build();

		Query query = Query.query(
				Criteria.where("scheduledNotifications.REMIND.active").is(true)
						.and("scheduledNotifications.REMIND.lastNotified").lt(currentDate)
		);

		// Mock 동작 정의
		when(mongoTemplate.query(Recipient.class).matching(query).all())
				.thenReturn(List.of(recipient));

		// When: 메서드 호출
		List<Recipient> recipients = recipientRepository.findReadyForRemind(currentDate);

		// Then: 결과 검증
		assertNotNull(recipients);
		assertEquals(1, recipients.size());
		assertEquals("test-user", recipients.get(0).getAccountName());
	}
}
