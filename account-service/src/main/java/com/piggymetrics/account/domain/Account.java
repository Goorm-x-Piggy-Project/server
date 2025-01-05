package com.piggymetrics.account.domain;

import com.piggymetrics.account.dto.AccountReqDto;
import com.piggymetrics.account.dto.UserReqDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "accounts")
public class Account {

	@Id
	private ObjectId id;

	@NotNull
	@Size(min = 1, max = 50)
	private String name;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate lastSeen;

	@Valid
	private List<Item> incomes;

	@Valid
	private List<Item> expenses;

	@NotNull
	private Saving saving;

	@Size(min = 0, max = 20_000)
	private String note;

	@Builder
	public Account(UserReqDto req, Saving saving) {
		this.name = req.getUsername();
		this.lastSeen = LocalDate.now();
		this.incomes = new ArrayList<>();
		this.expenses = new ArrayList<>();
		this.saving = saving;
	}

	public void updateAccount(AccountReqDto dto) {
		this.lastSeen = LocalDate.now();
		this.incomes = dto.getIncomes();
		this.expenses = dto.getExpenses();
		this.saving = dto.getSaving();
		this.note = dto.getNote();
	}
}
