package com.piggymetrics.account.service;

import com.piggymetrics.account.client.AuthServiceClient;
import com.piggymetrics.account.client.StatisticsServiceClient;
import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.Currency;
import com.piggymetrics.account.domain.Saving;
import com.piggymetrics.account.dto.AccountReqDto;
import com.piggymetrics.account.dto.AccountResDto;
import com.piggymetrics.account.dto.UserReqDto;
import com.piggymetrics.account.exception.AccountAlreadyExistsException;
import com.piggymetrics.account.exception.AccountNotFoundException;
import com.piggymetrics.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

	private final StatisticsServiceClient statisticsClient;

	private final AuthServiceClient authClient;

	private final AccountRepository repository;

	@Transactional(readOnly = true)
	public AccountResDto findByName(String accountName) {
		Account account =  checkIfAccountNotExists(accountName);
		return AccountResDto.fromEntity(account);
	}

	@Transactional
	public void create(UserReqDto userReqDto) {

		checkIfAccountExists(userReqDto.getUsername());
		log.debug("Feign 요청 시작");
		authClient.createUser(userReqDto); // account 만들면서 auth service에도 user 생성
		log.debug("Feign 요청 완료");

		Saving saving = new Saving(0L, Currency.getDefault(), 0L, false,false);
		Account account = new Account(userReqDto, saving);
		repository.save(account);

		log.info("new account has been created: " + account.getName());
	}

	@Transactional
	public void saveChanges(String name, AccountReqDto update) {

		log.info("username is {}", name);
		Account account = checkIfAccountNotExists(name);

		account.updateAccount(update);

		log.info("account {} changes has been saved", name);
		log.info("Received account: {}", account);
		repository.save(account);

		statisticsClient.updateStatistics(name, account);
	}

	private void checkIfAccountExists(String name) {
		Optional<Account> existing = repository.findByName(name);
		if(existing.isPresent()) {
			throw new AccountAlreadyExistsException("account already exists: " + name);
		}
	}

	private Account checkIfAccountNotExists(String name) {
		return repository.findByName(name).orElseThrow(() -> new AccountNotFoundException("Account not found: " + name));
	}
}
