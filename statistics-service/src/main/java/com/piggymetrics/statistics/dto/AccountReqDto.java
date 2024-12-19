package com.piggymetrics.statistics.dto;

import com.piggymetrics.statistics.domain.Item;
import java.util.List;
import lombok.Getter;

@Getter
public class AccountReqDto {

    private String name;
    private List<Item> incomes;
    private List<Item> expenses;

}
