package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.mail.MessagingException; // javax → jakarta로 변경
import jakarta.mail.internet.MimeMessage; // javax → jakarta로 변경

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Service
@RefreshScope
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	private final JavaMailSender mailSender;
	private final Environment env;

	public EmailServiceImpl(JavaMailSender mailSender, Environment env) {
		this.mailSender = mailSender;
		this.env = env;
	}

	@Override
	public void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException {
		String subject = getProperty(type.getSubject(), "Default Subject");
		String text = MessageFormat.format(getProperty(type.getText(), "Default Text"), recipient.getAccountName());

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

		helper.setTo(recipient.getEmail());
		helper.setSubject(subject);
		helper.setText(text);

		if (StringUtils.hasText(attachment)) {
			String attachmentName = getProperty(type.getAttachment(), "attachment.txt");
			helper.addAttachment(attachmentName, new ByteArrayResource(attachment.getBytes(StandardCharsets.UTF_8)));
		}

		mailSender.send(message);
		log.info("{} email notification has been sent to {}", type, recipient.getEmail());
	}

	/**
	 * 환경 변수에서 값을 가져오고, 기본값을 제공하는 메서드
	 */
	private String getProperty(String key, String defaultValue) {
		return env.getProperty(key, defaultValue);
	}
}
