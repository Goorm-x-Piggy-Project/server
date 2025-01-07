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
import org.springframework.security.access.prepost.PreAuthorize;
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
	@PreAuthorize("hasAuthority('SCOPE_server') or #principal.name == 'demo'")
	@GetMapping("/current")
	public ResponseEntity<?> getCurrentNotificationsSettings(Principal principal) {
		// 현재 사용자의 계정 이름 가져오기
		if (principal == null || principal.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse("Principal is missing"));
		}

		// 계정 이름으로 Recipient 검색
		Recipient recipient = recipientService.findByAccountName(principal.getName());

		// 수신자 정보가 없을 경우 404 Not Found 반환
		if (recipient == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Recipient not found"));
		}

		// 수신자 정보 반환
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
	public ResponseEntity<?> saveNotificationSettings(
			Principal principal,
			@PathVariable("type") NotificationType type,
			@Valid @RequestBody NotificationSettings settings
	) {
		// Principal이 null이거나 이름이 null일 경우 400 반환
		if (principal == null || principal.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Principal is missing"));
		}

		// NotificationType이 잘못된 값인 경우 400 반환
		if (type == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid notification type"));
		}

		// 현재 사용자의 계정 이름 가져오기
		String accountName = principal.getName();

		// 알림 설정 업데이트
		Recipient updatedRecipient = recipientService.updateNotificationSettings(accountName, type, settings);

		if (updatedRecipient == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Recipient not found"));
		}

		// 업데이트된 수신자 정보 반환
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedRecipient);
	}
}