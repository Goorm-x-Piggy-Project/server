package com.piggymetrics.account.service;

import com.piggymetrics.account.client.AuthServiceClient;
import com.piggymetrics.account.client.StatisticsServiceClient;
import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.Currency;
import com.piggymetrics.account.domain.Saving;
import com.piggymetrics.account.dto.AccountReqDto;
import com.piggymetrics.account.dto.UserReqDto;
import com.piggymetrics.account.exception.AccountNotFoundException;
import com.piggymetrics.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

	private final StatisticsServiceClient statisticsClient;

	private final AuthServiceClient authClient;

	private final AccountRepository repository;

	@Transactional(readOnly = true)
	public String findByName(String accountName) {
		Account account =  validateName(accountName);


		return account.getName();
	}

	public void create(UserReqDto userReqDto) {

		validateName(userReqDto.getUsername());
		authClient.createUser(userReqDto);

		Saving saving = new Saving(0L, Currency.getDefault(), 0L, false,false);
		Account account = new Account(userReqDto, saving);
		repository.save(account);

		log.info("new account has been created: " + account.getName());
	}

	public void saveChanges(String name, AccountReqDto update) {

		Account account = validateName(name);

		account.updateAccount(update);
		repository.save(account);

		log.debug("account {} changes has been saved", name);

		statisticsClient.updateStatistics(name, account);
	}

	private Account validateName(String name) {
		return repository.findByName(name)
				.orElseThrow(() -> new AccountNotFoundException("Not found: " + name));
	}
}
