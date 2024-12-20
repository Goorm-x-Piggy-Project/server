//이메일 전송 로직. (SMTP 또는 외부 API로 알림 이메일 전송)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 14:46
수정 내용 : 생성자 주입으로 변경, 유효성 검사(Assert 검증 : null 이거나 빈값 X), 주석 추가, 예외 처리 추가
*/

package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * EmailServiceImpl 클래스는 EmailService 인터페이스를 구현하며,
 * JavaMailSender를 사용하여 이메일을 발송.
 */
@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	private final JavaMailSender mailSender;

	@Value("${email.from}")
	private String from;

	/**
	 * EmailServiceImpl 생성자.
	 *
	 * @param mailSender JavaMailSender 인스턴스
	 */
	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * 이메일 발송.
	 *
	 * @param type 알림 유형
	 * @param recipient 수신자 정보
	 * @param attachment 첨부 파일 경로
	 * @throws MessagingException 이메일 발송 중 예외가 발생한 경우
	 * @throws IOException 첨부 파일 처리 중 예외가 발생한 경우
	 */
	@Override
	public void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException {
		if (StringUtils.isEmpty(recipient.getEmail())) {
			log.error("수신자의 이메일 주소가 비어 있습니다.");
			throw new IllegalArgumentException("수신자의 이메일 주소가 유효하지 않습니다.");
		}

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(from);
		helper.setTo(recipient.getEmail());
		helper.setSubject(type.getSubject());
		helper.setText(MessageFormat.format(type.getText(), recipient.getAccountName()), true);

		if (!StringUtils.isEmpty(attachment)) {
			helper.addAttachment("attachment", new ByteArrayResource(attachment.getBytes()));
		}

		mailSender.send(message);
		log.info("{} 이메일이 {}에게 성공적으로 발송되었습니다.", type, recipient.getEmail());
	}
}