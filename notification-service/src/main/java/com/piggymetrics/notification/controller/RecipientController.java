//클라이언트 요청을 처리하는 REST Controller (/recipients 경로와 관련된 HTTP 요청 처리)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 10:30
수정 내용 : @Autowired 제거, 생성자 주입 사용, @GetMapping, @PutMapping 변경
의존성 업데이트(javax -> jakarta), 주석 추가
*/


package com.piggymetrics.notification.controller;

import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.service.RecipientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * RecipientController 클래스는 수신자 관련 알림 설정을 처리하는 REST API 제공.
 * 주요 역할:
 * - 현재 사용자의 알림 설정 조회
 * - 알림 설정 저장
 */
@RestController
@RequestMapping("/recipients")
public class RecipientController {

	private final RecipientService recipientService;

	/**
	 * RecipientController 생성자.
	 *
	 * @param recipientService RecipientService 인스턴스
	 */
	public RecipientController(RecipientService recipientService) {
		this.recipientService = recipientService;
	}

	/**
	 * 현재 사용자의 알림 설정 조회.
	 *
	 * @param principal 현재 인증된 사용자의 Principal 객체
	 * @return 사용자의 알림 설정
	 */
	@GetMapping("/current")
	public Object getCurrentNotificationsSettings(Principal principal) {
		return recipientService.findByAccountName(principal.getName());
	}

	/**
	 * 현재 사용자의 알림 설정 저장.
	 *
	 * @param principal 현재 인증된 사용자의 Principal 객체
	 * @param recipient 알림 설정 정보
	 * @return 저장된 알림 설정
	 */
	@PutMapping("/current")
	public Object saveCurrentNotificationsSettings(Principal principal, @Valid @RequestBody Recipient recipient) {
		return recipientService.save(principal.getName(), recipient);
	}
}

