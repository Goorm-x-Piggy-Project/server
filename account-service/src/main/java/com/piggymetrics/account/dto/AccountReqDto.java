package com.piggymetrics.account.dto;

import com.piggymetrics.account.domain.Item;
import lombok.Getter;

import java.util.List;

@Getter
public class AccountReqDto {

    private String name;
    private List<Item> incomes;
    private List<Item> expenses;

}
