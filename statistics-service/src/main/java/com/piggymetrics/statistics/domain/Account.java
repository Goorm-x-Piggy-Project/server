package com.piggymetrics.statistics.domain;

import static com.piggymetrics.statistics.exception.ErrorMessages.ACCOUNT_NAME_BLANK;

import com.piggymetrics.statistics.dto.AccountReqDto;
import com.piggymetrics.statistics.dto.UserReqDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "accounts")
public class Account {

	@Id
	private ObjectId id;

	@NotBlank(message = ACCOUNT_NAME_BLANK)
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
	}
}

