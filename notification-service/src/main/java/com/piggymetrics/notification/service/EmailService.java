//이메일 발송 기능 정의 : 알림 유형과 수신자 정보를 기반으로 이메일을 발송.
package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;

import jakarta.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

	/**
	 * 이메일 발송.
	 *
	 * @param type 알림 유형 (NotificationType)
	 * @param recipient 수신자 정보 (Recipient)
	 * @param attachment 첨부 파일 경로
	 * @throws MessagingException 이메일 발송 중 메일 관련 예외 발생 시
	 * @throws IOException 첨부 파일 처리 중 IO 예외 발생 시
	 */
	void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException;

}
