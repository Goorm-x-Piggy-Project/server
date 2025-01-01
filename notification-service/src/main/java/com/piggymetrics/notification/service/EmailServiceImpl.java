// 이메일 전송 로직 (SMTP 또는 외부 API를 통해 알림 이메일 전송)
package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * EmailServiceImpl 클래스는 EmailService 인터페이스를 구현하며,
 * 이메일 전송 로직을 제공합니다.
 */
@Service
@Getter
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	private final JavaMailSender mailSender;

	@Value("${email.from}")
	private String from;

	/**
	 * 이메일 발송.
	 *
	 * @param type 알림 유형
	 * @param recipient 수신자 정보
	 * @param attachment 첨부 파일 경로
	 * @throws MessagingException 이메일 발송 중 발생한 예외
	 * @throws IOException 첨부 파일 처리 중 발생한 예외
	 */
	@Override
	public void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException {
		validateRecipient(recipient);

		MimeMessage message = createMimeMessage(type, recipient, attachment);

		mailSender.send(message);
		log.info("{} 이메일이 {}에게 성공적으로 발송되었습니다.", type, recipient.getEmail());
	}

	/**
	 * 수신자 유효성을 검증합니다.
	 *
	 * @param recipient 수신자 객체
	 */
	private void validateRecipient(Recipient recipient) {
		Assert.notNull(recipient, "Recipient 객체는 null일 수 없습니다.");
		Assert.hasLength(recipient.getEmail(), "수신자의 이메일 주소는 비어 있을 수 없습니다.");
	}

	/**
	 * 이메일 메시지를 생성합니다.
	 *
	 * @param type 알림 유형
	 * @param recipient 수신자 정보
	 * @param attachment 첨부 파일 경로
	 * @return 생성된 MimeMessage 객체
	 * @throws MessagingException 이메일 생성 중 발생한 예외
	 * @throws IOException 첨부 파일 처리 중 발생한 예외
	 */
	private MimeMessage createMimeMessage(NotificationType type, Recipient recipient, String attachment)
			throws MessagingException, IOException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(from);
		helper.setTo(recipient.getEmail());
		helper.setSubject(type.getSubject());
		helper.setText(MessageFormat.format(type.getText(), recipient.getAccountName()), true);

		if (attachment != null && !attachment.isEmpty()) {
			helper.addAttachment("attachment", new ByteArrayResource(attachment.getBytes()));
		}

		return message;
	}
}
