package com.piggymetrics.notification.controller;

import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.service.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // javax → jakarta로 변경
import java.security.Principal;

@RestController
@RequestMapping("/recipients")
public class RecipientController {

	private final RecipientService recipientService;

	// 생성자 주입 방식
	@Autowired
	public RecipientController(RecipientService recipientService) {
		this.recipientService = recipientService;
	}

	// GET 요청
	@GetMapping("/current")
	public Object getCurrentNotificationsSettings(Principal principal) {
		return recipientService.findByAccountName(principal.getName());
	}

	// PUT 요청
	@PutMapping("/current")
	public Object saveCurrentNotificationsSettings(Principal principal,
												   @Valid @RequestBody Recipient recipient) {
		return recipientService.save(principal.getName(), recipient);
	}
}
