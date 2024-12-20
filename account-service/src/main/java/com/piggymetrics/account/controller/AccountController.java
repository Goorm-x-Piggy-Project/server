package com.piggymetrics.account.controller;

import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.dto.AccountReqDto;
import com.piggymetrics.account.dto.UserReqDto;
import com.piggymetrics.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

	private final AccountService accountService;

	@PreAuthorize("#oauth2.hasScope('server') or #name.equals('demo')")
	@GetMapping("/{name}")
	public ResponseEntity<String> getAccountByName(@PathVariable String name) {

		return ResponseEntity.ok(accountService.findByName(name));
	}

	@GetMapping("/current")
	public ResponseEntity<String> getCurrentAccount(Principal principal) {
		return ResponseEntity.ok(accountService.findByName(principal.getName()));
	}

	@PutMapping("/current")
	public void saveCurrentAccount(Principal principal, @Valid @RequestBody AccountReqDto accountReqDto) {
		accountService.saveChanges(principal.getName(), accountReqDto);
	}

	@PostMapping
	public ResponseEntity<String> createNewAccount(@Valid @RequestBody UserReqDto userReqDto) {
		accountService.create(userReqDto);
		return ResponseEntity.ok("account Created!");
	}
}
