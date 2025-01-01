// MongoDB 기반 Recipient 리포지토리

package com.piggymetrics.notification.repository;

import com.piggymetrics.notification.domain.Recipient;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * RecipientRepository는 MongoDB recipients 컬렉션과 상호작용하는 리포지토리.
 * - 계정 이름 검색
 * - 알림 준비 상태의 수신자 검색
 */
@Repository
public interface RecipientRepository extends MongoRepository<Recipient, String>, RecipientRepositoryCustom {

	/**
	 * 계정 이름으로 수신자 정보를 검색합니다.
	 *
	 * @param name 계정 이름
	 * @return Optional로 감싼 Recipient 객체
	 */
	Optional<Recipient> findByAccountName(String name);
}
