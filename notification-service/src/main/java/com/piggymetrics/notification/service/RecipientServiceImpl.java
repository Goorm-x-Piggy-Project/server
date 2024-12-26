//Recipient 관련 비즈니스 로직 처리.

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-20, 금, 13:31
수정 내용 : 코드 수정
*/

package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationSettings;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.repository.RecipientRepository;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * RecipientServiceImpl 클래스는 RecipientService 인터페이스를 구현하며,
 * 수신자 관리 및 알림 준비 상태 처리를 제공.
 */
@Service
public class RecipientServiceImpl implements RecipientService {

	private static final Logger log = LoggerFactory.getLogger(RecipientServiceImpl.class);

	private final RecipientRepository repository;

	/**
	 * RecipientServiceImpl 생성자.
	 *
	 * @param repository RecipientRepository 인스턴스
	 */
	public RecipientServiceImpl(RecipientRepository repository) {
		this.repository = repository;
	}

	/**
	 * 계정 이름으로 수신자 검색.
	 *
	 * @param accountName 계정 이름
	 * @return 검색된 Recipient 객체
	 */
	@Override
	public Recipient findByAccountName(String accountName) {
		Assert.hasLength(accountName, "계정 이름은 비어 있을 수 없습니다.");
		return repository.findByAccountName(accountName);
	}

	/**
	 * 수신자 정보를 생성하거나 업데이트.
	 *
	 * @param accountName 계정 이름
	 * @param recipient 업데이트할 Recipient 객체
	 * @return 업데이트된 Recipient 객체
	 */
	@Override
	public Recipient save(String accountName, Recipient recipient) {
		Assert.hasLength(accountName, "계정 이름은 비어 있을 수 없습니다.");
		Assert.notNull(recipient, "Recipient 객체는 null일 수 없습니다.");

		// accountName을 업데이트한 새 Recipient 객체 생성
		Recipient updatedRecipient = recipient.updateAccountName(accountName);
		return repository.save(updatedRecipient);
	}

	/**
	 * 특정 알림 유형에 따라 알림 발송 준비가 된 수신자를 반환.
	 *
	 * @param type 알림 유형
	 * @return 알림 발송 준비가 된 Recipient 목록
	 */
	@Override
	public List<Recipient> findReadyToNotify(NotificationType type) {
		Assert.notNull(type, "알림 유형은 null일 수 없습니다.");
        return switch (type) {
            case BACKUP -> repository.findReadyForBackup(new Date().toString());
            case REMIND -> repository.findReadyForRemind(new Date().toString());
            default -> throw new IllegalArgumentException("지원되지 않는 알림 유형: " + type);
        };
	}

	/**
	 * 특정 알림 유형의 마지막 발송 시간을 현재 시간으로 기록.
	 *
	 * @param type 알림 유형
	 * @param recipient 발송 시간을 업데이트할 Recipient 객체
	 */
	@Override
	public void markNotified(NotificationType type, Recipient recipient) {
		Assert.notNull(type, "알림 유형은 null일 수 없습니다.");
		Assert.notNull(recipient, "Recipient 객체는 null일 수 없습니다.");

		// 기존 알림 설정 업데이트
		NotificationSettings settings = recipient.getScheduledNotifications().get(type)
				.updateLastNotified(new Date());

		// updatedRecipient 생성
		Recipient updatedRecipient = recipient.toBuilder()
				.scheduledNotifications(
						Map.of(type, settings) // 기존 알림 설정에서 업데이트된 값만 변경
				)
				.build();

		repository.save(updatedRecipient);
		log.info("{} 알림의 발송 시간이 {}로 업데이트되었습니다.", type, recipient.getAccountName());
	}
}