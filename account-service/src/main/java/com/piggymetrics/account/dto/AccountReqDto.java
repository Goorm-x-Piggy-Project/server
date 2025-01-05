package com.piggymetrics.account.dto;

import com.piggymetrics.account.domain.Item;
import com.piggymetrics.account.domain.Saving;
import lombok.Getter;

import java.util.List;

@Getter
public class AccountReqDto {

    private String note;
    private List<Item> incomes;
    private List<Item> expenses;
    private Saving saving;

}
