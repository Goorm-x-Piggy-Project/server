//클라이언트 요청을 처리하는 REST Controller (/recipients 경로와 관련된 HTTP 요청 처리)
package com.piggymetrics.notification.controller;

import com.piggymetrics.notification.domain.NotificationSettings;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.service.RecipientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * RecipientController 클래스는 REST API를 통해 수신자 관련 알림 설정을 관리합니다.
 * - 알림 설정 조회 및 저장을 제공합니다.
 */
@RestController
@RequestMapping("/recipients")
@RequiredArgsConstructor
public class RecipientController {

	private final RecipientService recipientService;

	/**
	 * 현재 사용자의 알림 설정을 조회합니다.
	 *
	 * @param principal 현재 인증된 사용자의 Principal 객체
	 * @return ResponseEntity로 감싼 Recipient 객체
	 */
	@GetMapping("/current")
	public ResponseEntity<Recipient> getCurrentNotificationsSettings(Principal principal) {
		String accountName = principal.getName();
		Recipient recipient = recipientService.findByAccountName(accountName);

		// 수신자 정보가 없을 경우 예외 처리
		if (recipient == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(recipient);
	}

	/**
	 * 현재 사용자의 특정 알림 유형의 설정을 저장합니다.
	 *
	 * @param principal 현재 인증된 사용자의 Principal 객체
	 * @param type 알림 유형
	 * @param settings 저장할 NotificationSettings 객체
	 * @return ResponseEntity로 감싼 업데이트된 Recipient 객체
	 */
	@PutMapping("/current/{type}")
	public ResponseEntity<Recipient> saveNotificationSettings(
			Principal principal,
			@PathVariable NotificationType type,
			@Valid @RequestBody NotificationSettings settings
	) {
		String accountName = principal.getName();
		Recipient updatedRecipient = recipientService.updateNotificationSettings(accountName, type, settings);
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedRecipient);
	}
}