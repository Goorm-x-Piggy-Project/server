package com.piggymetrics.statistics.dto;

import com.piggymetrics.statistics.domain.Item;
import java.util.List;

import com.piggymetrics.statistics.domain.Saving;
import lombok.Getter;

@Getter
public class AccountReqDto {

    private String note;
    private List<Item> incomes;
    private List<Item> expenses;
    private Saving saving;

}
