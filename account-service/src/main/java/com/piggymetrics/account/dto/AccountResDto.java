package com.piggymetrics.account.dto;

import com.piggymetrics.account.domain.Account;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class AccountResDto {

    private String name;
    private LocalDate lastSeen;
    private List<ItemResDto> incomes;
    private List<ItemResDto> expenses;
    private SavingResDto saving;
    private String note;

    @Builder
    private AccountResDto(String name, LocalDate lastSeen, List<ItemResDto> incomes, List<ItemResDto> expenses, SavingResDto saving, String note) {
        this.name = name;
        this.lastSeen = lastSeen;
        this.incomes = incomes;
        this.expenses = expenses;
        this.saving = saving;
        this.note = note;
    }

    public static AccountResDto fromEntity(Account account) {
        List<ItemResDto> incomes = account.getIncomes().stream().map(ItemResDto::fromEntity).toList();
        List<ItemResDto> expenses = account.getExpenses().stream().map(ItemResDto::fromEntity).toList();
        return AccountResDto.builder()
                .name(account.getName())
                .lastSeen(account.getLastSeen())
                .incomes(incomes)
                .expenses(expenses)
                .saving(SavingResDto.fromEntity(account.getSaving()))
                .note(account.getNote())
                .build();
    }
}
