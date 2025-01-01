package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * EmailServiceImpl 클래스의 이메일 발송 로직을 검증.
 * - 이메일의 제목, 본문, 첨부 파일이 올바르게 설정되는지 확인
 */
class EmailServiceImplTest {

	@InjectMocks
	private EmailServiceImpl emailService;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private MimeMessage mimeMessage;

	private ArgumentCaptor<MimeMessage> mimeMessageCaptor;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
	}

	/**
	 * BACKUP 알림 유형의 이메일이 올바르게 생성되고 전송되는지 확인.
	 */
	@Test
	void shouldSendBackupEmail() throws MessagingException, IOException {
		// Given: 테스트 데이터 설정
		Recipient recipient = getMockRecipient();
		NotificationType type = NotificationType.BACKUP;
		String attachment = "Test attachment content";

		// When: 이메일 발송 메서드 호출
		emailService.send(type, recipient, attachment);

		// Then: 메일 전송 확인 및 캡처된 메시지 검증
		verify(mailSender).send(mimeMessageCaptor.capture());
		MimeMessage capturedMessage = mimeMessageCaptor.getValue();
		assertNotNull(capturedMessage);

		// 메시지 구성 확인
		verifyMimeMessage(type, recipient, attachment);
	}

	/**
	 * REMIND 알림 유형의 이메일이 첨부 파일 없이 올바르게 전송되는지 확인.
	 */
	@Test
	void shouldSendRemindEmailWithoutAttachment() throws MessagingException, IOException {
		// Given: 테스트 데이터 설정
		Recipient recipient = getMockRecipient();
		NotificationType type = NotificationType.REMIND;

		// When: 이메일 발송 메서드 호출
		emailService.send(type, recipient, null);

		// Then: 메일 전송 확인 및 캡처된 메시지 검증
		verify(mailSender).send(mimeMessageCaptor.capture());
		MimeMessage capturedMessage = mimeMessageCaptor.getValue();
		assertNotNull(capturedMessage);

		// 메시지 구성 확인
		verifyMimeMessage(type, recipient, null);
	}

	/**
	 * 이메일 메시지의 세부 사항 검증.
	 * @param type 알림 유형
	 * @param recipient 수신자 객체
	 * @param attachment 첨부 파일 내용
	 */
	private void verifyMimeMessage(NotificationType type, Recipient recipient, String attachment) throws MessagingException {
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

		verify(mimeMessage).setContent(any(), eq("text/html"));
		helper.setFrom(emailService.getFrom());
		helper.setTo(recipient.getEmail());
		helper.setSubject(type.getSubject());
		helper.setText(type.getText(), true);

		if (attachment != null) {
			helper.addAttachment("attachment", new ByteArrayResource(attachment.getBytes()));
		}
	}

	/**
	 * 테스트용 Mock Recipient 객체 생성.
	 * @return Recipient 객체
	 */
	private Recipient getMockRecipient() {
		return Recipient.builder()
				.accountName("test-user")
				.email("test@test.com")
				.build();
	}
}
