// 수신자 알림 설정 관련 비즈니스 로직 구현
package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationSettings;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.repository.RecipientRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * RecipientServiceImpl 클래스는 RecipientService 인터페이스를 구현하며,
 * 수신자 관리와 알림 준비 상태 처리 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class RecipientServiceImpl implements RecipientService {

	private static final Logger log = LoggerFactory.getLogger(RecipientServiceImpl.class);

	private final RecipientRepository repository;

	/**
	 * 계정 이름으로 수신자 정보를 검색합니다.
	 *
	 * @param accountName 계정 이름
	 * @return 검색된 Recipient 객체
	 * @throws NoSuchElementException 수신자가 존재하지 않을 경우
	 */
	@Override
	public Recipient findByAccountName(String accountName) {
		Assert.hasLength(accountName, "계정 이름은 비어 있을 수 없습니다.");
		return repository.findByAccountName(accountName)
				.orElseThrow(() -> new NoSuchElementException("해당 계정 이름을 가진 수신자를 찾을 수 없습니다: " + accountName));
	}

//	/**
//	 * 수신자 정보를 생성하거나 업데이트합니다.
//	 *
//	 * @param accountName 계정 이름
//	 * @param recipient 업데이트할 Recipient 객체
//	 * @return 업데이트된 Recipient 객체
//	 */
//	@Override
//	public Recipient save(String accountName, Recipient recipient) {
//		Assert.hasLength(accountName, "계정 이름은 비어 있을 수 없습니다.");
//		Assert.notNull(recipient, "Recipient 객체는 null일 수 없습니다.");
//
//		Recipient updatedRecipient = recipient.updateAccountName(accountName);
//		Recipient savedRecipient = repository.save(updatedRecipient);
//		log.info("수신자 정보가 저장되었습니다: {}", accountName);
//		return savedRecipient;
//	}

	/**
	 * 특정 알림 유형의 설정을 업데이트한 수신자 정보를 저장합니다.
	 *
	 * @param accountName 계정 이름
	 * @param type 알림 유형
	 * @param settings 업데이트할 NotificationSettings 객체
	 * @return 업데이트된 Recipient 객체
	 */
	@Override
	public Recipient updateNotificationSettings(String accountName, NotificationType type, NotificationSettings settings) {
		Assert.hasLength(accountName, "계정 이름은 비어 있을 수 없습니다.");
		Assert.notNull(type, "알림 유형은 null일 수 없습니다.");
		Assert.notNull(settings, "NotificationSettings 객체는 null일 수 없습니다.");

		Recipient recipient = findByAccountName(accountName);

		Recipient updatedRecipient = recipient.updateNotificationSettings(type, settings);
		Recipient savedRecipient = repository.save(updatedRecipient);

		log.info("수신자의 알림 설정이 업데이트되었습니다: {} (알림 유형: {})", accountName, type);
		return savedRecipient;
	}

	/**
	 * 특정 알림 유형에 따라 알림 발송 준비가 된 수신자를 반환합니다.
	 *
	 * @param type 알림 유형
	 * @return 알림 발송 준비가 된 Recipient 목록
	 */
	@Override
	public List<Recipient> findReadyToNotify(NotificationType type) {
		Assert.notNull(type, "알림 유형은 null일 수 없습니다.");
		Date now = new Date();

		return switch (type) {
			case BACKUP -> repository.findReadyForBackup(now);
			case REMIND -> repository.findReadyForRemind(now);
			default -> throw new IllegalArgumentException("지원되지 않는 알림 유형: " + type);
		};
	}

	/**
	 * 특정 알림 유형의 마지막 발송 시간을 현재 시간으로 기록합니다.
	 *
	 * @param type 알림 유형
	 * @param recipient 발송 시간을 업데이트할 Recipient 객체
	 */
	@Override
	public void markNotified(NotificationType type, Recipient recipient) {
		Assert.notNull(type, "알림 유형은 null일 수 없습니다.");
		Assert.notNull(recipient, "Recipient 객체는 null일 수 없습니다.");

		// 기존 설정을 업데이트한 새 NotificationSettings 객체 생성
		NotificationSettings updatedSettings = recipient.getScheduledNotifications()
				.get(type)
				.updateLastNotified(new Date());

		// 기존 Recipient 객체의 scheduledNotifications을 새로 업데이트된 설정으로 덮어씌움
		Map<NotificationType, NotificationSettings> updatedNotifications = new HashMap<>(recipient.getScheduledNotifications());
		updatedNotifications.put(type, updatedSettings);

		// 변경된 값으로 새로운 Recipient 객체 생성
		Recipient updatedRecipient = recipient.toBuilder()
				.scheduledNotifications(updatedNotifications)
				.build();

		// 변경된 값으로 repository에 저장
		repository.save(updatedRecipient);

		log.info("{} 알림의 발송 시간이 {}로 업데이트되었습니다.", type, recipient.getAccountName());
	}
}
