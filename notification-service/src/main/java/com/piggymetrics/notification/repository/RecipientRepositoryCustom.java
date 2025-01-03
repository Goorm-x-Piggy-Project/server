package com.piggymetrics.notification.repository;

import com.piggymetrics.notification.domain.Recipient;

import java.util.Date;
import java.util.List;

/**
 * Querydsl을 사용하여 사용자 정의 쿼리를 처리하는 인터페이스.
 */
public interface RecipientRepositoryCustom {

    /**
     * 백업 알림 발송 준비가 된 수신자를 검색합니다.
     *
     * @param date 기준 날짜
     * @return 준비된 수신자 목록
     */
    List<Recipient> findReadyForBackup(Date date);

    /**
     * 리마인드 알림 발송 준비가 된 수신자를 검색합니다.
     *
     * @param date 기준 날짜
     * @return 준비된 수신자 목록
     */
    List<Recipient> findReadyForRemind(Date date);
}
