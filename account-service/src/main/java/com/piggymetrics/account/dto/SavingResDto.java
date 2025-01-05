package com.piggymetrics.account.dto;

import com.piggymetrics.account.domain.Currency;
import com.piggymetrics.account.domain.Saving;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class SavingResDto {

//    private ObjectId id; 이거 그냥 값 타입이어서 id 필요 없을 듯

    private Long amount;

    private Currency currency;

    private Long interest;

    private Boolean deposit;

    private Boolean capitalization;

    @Builder
    private SavingResDto(Long amount, Currency currency, Long interest, Boolean deposit, Boolean capitalization) {
        this.amount = amount;
        this.currency = currency;
        this.interest = interest;
        this.deposit = deposit;
        this.capitalization = capitalization;
    }

    public static SavingResDto fromEntity(Saving saving) {
        return SavingResDto.builder()
            .amount(saving.getAmount())
            .currency(saving.getCurrency())
            .interest(saving.getInterest())
            .deposit(saving.getDeposit())
            .capitalization(saving.getCapitalization())
            .build();
    }
}
