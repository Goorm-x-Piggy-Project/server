package com.piggymetrics.account.service;

import com.piggymetrics.account.client.AuthServiceClient;
import com.piggymetrics.account.client.StatisticsServiceClient;
import com.piggymetrics.account.domain.*;
import com.piggymetrics.account.dto.AccountResDto;
import com.piggymetrics.account.dto.UserReqDto;
import com.piggymetrics.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountServiceTest {

	@InjectMocks
	private AccountService accountService;

	@Mock
	private StatisticsServiceClient statisticsClient;

	@Mock
	private AuthServiceClient authClient;

	@Mock
	private AccountRepository repository;

    private List<Item> incomes = new ArrayList<>();
    private List<Item> expenses = new ArrayList<>();

	@BeforeEach
	public void setup() {
		initMocks(this);
	}

	@Test
	public void shouldFindByName() {

        Saving saving = new Saving(0L, Currency.getDefault(), 0L, false,false);
        Account account = new Account("name", incomes, expenses, saving);

		when(accountService.findByName(account.getName())).thenReturn(AccountResDto.fromEntity(account));
        AccountResDto dto = accountService.findByName(account.getName());

        assertEquals(account, dto);
	}

	@Test
	public void shouldCreateAccountWithGivenUser() {

		UserReqDto user = new UserReqDto();

		accountService.create(user);

		verify(authClient, times(1)).createUser(user);
	}

//	@Test
//	public void shouldSaveChangesWhenUpdatedAccountGiven() {
//
//		Item grocery = new Item();
//		grocery.setTitle("Grocery");
//		grocery.setAmount(new BigDecimal(10));
//		grocery.setCurrency(Currency.USD);
//		grocery.setPeriod(TimePeriod.DAY);
//		grocery.setIcon("meal");
//
//		Item salary = new Item();
//		salary.setTitle("Salary");
//		salary.setAmount(new BigDecimal(9100));
//		salary.setCurrency(Currency.USD);
//		salary.setPeriod(TimePeriod.MONTH);
//		salary.setIcon("wallet");
//
//		Saving saving = new Saving();
//		saving.setAmount(new BigDecimal(1500));
//		saving.setCurrency(Currency.USD);
//		saving.setInterest(new BigDecimal("3.32"));
//		saving.setDeposit(true);
//		saving.setCapitalization(false);
//
//		final Account update = new Account();
//		update.setName("test");
//		update.setNote("test note");
//		update.setIncomes(Arrays.asList(salary));
//		update.setExpenses(Arrays.asList(grocery));
//		update.setSaving(saving);
//
//		final Account account = new Account();
//
//		when(accountService.findByName("test")).thenReturn(account);
//		accountService.saveChanges("test", update);
//
//		assertEquals(update.getNote(), account.getNote());
//		assertNotNull(account.getLastSeen());
//
//		assertEquals(update.getSaving().getAmount(), account.getSaving().getAmount());
//		assertEquals(update.getSaving().getCurrency(), account.getSaving().getCurrency());
//		assertEquals(update.getSaving().getInterest(), account.getSaving().getInterest());
//		assertEquals(update.getSaving().getDeposit(), account.getSaving().getDeposit());
//		assertEquals(update.getSaving().getCapitalization(), account.getSaving().getCapitalization());
//
//		assertEquals(update.getExpenses().size(), account.getExpenses().size());
//		assertEquals(update.getIncomes().size(), account.getIncomes().size());
//
//		assertEquals(update.getExpenses().get(0).getTitle(), account.getExpenses().get(0).getTitle());
//		assertEquals(0, update.getExpenses().get(0).getAmount().compareTo(account.getExpenses().get(0).getAmount()));
//		assertEquals(update.getExpenses().get(0).getCurrency(), account.getExpenses().get(0).getCurrency());
//		assertEquals(update.getExpenses().get(0).getPeriod(), account.getExpenses().get(0).getPeriod());
//		assertEquals(update.getExpenses().get(0).getIcon(), account.getExpenses().get(0).getIcon());
//
//		assertEquals(update.getIncomes().get(0).getTitle(), account.getIncomes().get(0).getTitle());
//		assertEquals(0, update.getIncomes().get(0).getAmount().compareTo(account.getIncomes().get(0).getAmount()));
//		assertEquals(update.getIncomes().get(0).getCurrency(), account.getIncomes().get(0).getCurrency());
//		assertEquals(update.getIncomes().get(0).getPeriod(), account.getIncomes().get(0).getPeriod());
//		assertEquals(update.getIncomes().get(0).getIcon(), account.getIncomes().get(0).getIcon());
//
//		verify(repository, times(1)).save(account);
//		verify(statisticsClient, times(1)).updateStatistics("test", account);
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void shouldFailWhenNoAccountsExistedWithGivenName() {
//		final Account update = new Account();
//		update.setIncomes(Arrays.asList(new Item()));
//		update.setExpenses(Arrays.asList(new Item()));
//
//		when(accountService.findByName("test")).thenReturn(null);
//		accountService.saveChanges("test", update);
//	}
}
