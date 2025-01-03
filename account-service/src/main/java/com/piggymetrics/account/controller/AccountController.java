package com.piggymetrics.account.controller;

import com.piggymetrics.account.dto.AccountReqDto;
import com.piggymetrics.account.dto.AccountResDto;
import com.piggymetrics.account.dto.UserReqDto;
import com.piggymetrics.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
@Slf4j
public class AccountController {

	private final AccountService accountService;

	@GetMapping
	public String hello() {
		return "hello";
	}

	@PreAuthorize("hasAuthority('SCOPE_server') or #name.equals('demo')")
	@GetMapping("/{name}")
	public ResponseEntity<AccountResDto> getAccountByName(@PathVariable String name) {

		return ResponseEntity.ok(accountService.findByName(name));
	}

	@GetMapping("/current")
	public ResponseEntity<AccountResDto> getCurrentAccount(Principal principal) {
		return ResponseEntity.ok(accountService.findByName(principal.getName()));
	}

	@PutMapping("/current")
	public void saveCurrentAccount(Principal principal, @Valid @RequestBody AccountReqDto accountReqDto) {
		accountService.saveChanges(principal.getName(), accountReqDto);
	}

	@PostMapping
	public ResponseEntity<String> createNewAccount(@Valid @RequestBody UserReqDto userReqDto) {
		log.info("AccountController createNewAccount 호출");
		accountService.create(userReqDto);
		return ResponseEntity.ok("account Created!");
	}
}
