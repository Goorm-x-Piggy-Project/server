//MongoDB 기반
/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 14:40
수정 내용 : 쿼리 최적화($lt), 주석 추가
*/

package com.piggymetrics.notification.repository;

import com.piggymetrics.notification.domain.Recipient;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RecipientRepository는 MongoDB recipients 컬렉션과 상호작용하는 리포지토리.
 * 주요 역할:
 * - 계정 이름으로 수신자 검색
 * - 특정 알림 유형에 따라 수신자를 검색
 */
@Repository
public interface RecipientRepository extends CrudRepository<Recipient, String> {

	/**
	 * 계정 이름으로 수신자 정보 검색.
	 *
	 * @param name 계정 이름
	 * @return 계정 이름에 해당하는 Recipient 객체
	 */
	Recipient findByAccountName(String name);

	/**
	 * 백업 알림을 받을 준비가 된 수신자 검색.
	 *
	 * @return 백업 알림 준비가 된 Recipient 리스트
	 */
	@Query("{ 'scheduledNotifications.BACKUP.active': true, 'scheduledNotifications.BACKUP.lastNotified': { $lt: ?0 } }")
	List<Recipient> findReadyForBackup(String date);

	/**
	 * 리마인드 알림을 받을 준비가 된 수신자를 검색합니다.
	 *
	 * @return 리마인드 알림 준비가 된 Recipient 리스트
	 */
	@Query("{ 'scheduledNotifications.REMIND.active': true, 'scheduledNotifications.REMIND.lastNotified': { $lt: ?0 } }")
	List<Recipient> findReadyForRemind(String date);
}
