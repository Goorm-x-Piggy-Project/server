package com.piggymetrics.account.dto;

import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.Item;
import com.piggymetrics.account.domain.Saving;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class AccountResDto {

    private String name;
    private LocalDate lastSeen;
    private List<Item> incomes;
    private List<Item> expenses;
    private Saving saving;
    private String note;

    @Builder
    private AccountResDto(String name, LocalDate lastSeen, List<Item> incomes, List<Item> expenses, Saving saving, String note) {
        this.name = name;
        this.lastSeen = lastSeen;
        this.incomes = incomes;
        this.expenses = expenses;
        this.saving = saving;
        this.note = note;
    }

    public static AccountResDto fromEntity(Account account) {
        return AccountResDto.builder()
                .name(account.getName())
                .lastSeen(account.getLastSeen())
                .incomes(account.getIncomes())
                .expenses(account.getExpenses())
                .saving(account.getSaving())
                .note(account.getNote())
                .build();
    }
}
