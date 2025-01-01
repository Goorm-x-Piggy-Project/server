package com.piggymetrics.notification.repository;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * RecipientRepositoryCustom 구현체.
 * - 알림 유형에 따른 동적 검색 로직 구현.
 */
@Repository
public class RecipientRepositoryImpl implements RecipientRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public RecipientRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 특정 알림 유형과 날짜 조건으로 수신자를 검색.
     *
     * @param type 알림 유형
     * @param date 기준 날짜
     * @return 준비 상태의 Recipient 목록
     */
    private List<Recipient> findReadyForType(NotificationType type, Date date) {
        return mongoTemplate.query(Recipient.class)
                .matching(Query.query(
                        Criteria.where("scheduledNotifications." + type.name() + ".active").is(true)
                                .and("scheduledNotifications." + type.name() + ".lastNotified").lt(date)
                )).all();
    }

    @Override
    public List<Recipient> findReadyForBackup(Date date) {
        return findReadyForType(NotificationType.BACKUP, date);
    }

    @Override
    public List<Recipient> findReadyForRemind(Date date) {
        return findReadyForType(NotificationType.REMIND, date);
    }
}
