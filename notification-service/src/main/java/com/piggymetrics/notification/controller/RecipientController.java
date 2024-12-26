//클라이언트 요청을 처리하는 REST Controller (/recipients 경로와 관련된 HTTP 요청 처리)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-20, 금, 08:45
수정 내용 : 생성자 삭제
*/

package com.piggymetrics.notification.controller;

import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.service.RecipientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

/**
 * RecipientController 클래스는 수신자 관련 알림 설정을 처리하는 REST API 제공.
 * 주요 역할:
 * - 현재 사용자의 알림 설정 조회
 * - 알림 설정 저장
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/recipients")
public class RecipientController {

	private final RecipientService recipientService;

	/**
	 * 현재 사용자의 알림 설정 조회.
	 *
	 * @param principal 현재 인증된 사용자의 Principal 객체
	 * @return 사용자의 알림 설정
	 */
	@GetMapping("/current")
	public ResponseEntity<Recipient> getCurrentNotificationsSettings(Principal principal) {
		Recipient recipient = recipientService.findByAccountName(principal.getName());
		return ResponseEntity.ok(recipient);
	}

	/**
	 * 현재 사용자의 알림 설정 저장.
	 *
	 * @param principal 현재 인증된 사용자의 Principal 객체
	 * @param recipient 알림 설정 정보
	 * @return 저장된 알림 설정
	 */
	@PutMapping("/current")
	public ResponseEntity<String> saveCurrentNotificationsSettings(Principal principal, @Valid @RequestBody Recipient recipient) {
		Recipient savedRecipient = recipientService.save(principal.getName(), recipient);
		return ResponseEntity.status(HttpStatus.CREATED).body("알림 설정이 저장되었습니다.");
	}
}

