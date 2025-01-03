package com.piggymetrics.account.dto;

import com.piggymetrics.account.domain.Currency;
import com.piggymetrics.account.domain.Item;
import com.piggymetrics.account.domain.TimePeriod;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class ItemResDto {

    private ObjectId id;
    private String title;
    private Long amount;
    private Currency currency;
    private TimePeriod period;
    private String icon;

    @Builder
    private ItemResDto(ObjectId id, String title, Long amount, Currency currency, TimePeriod period, String icon) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.currency = currency;
        this.period = period;
        this.icon = icon;
    }

    public static ItemResDto fromEntity(Item item) {
        return ItemResDto.builder()
            .id(item.getId())
            .title(item.getTitle())
            .amount(item.getAmount())
            .currency(item.getCurrency())
            .period(item.getPeriod())
            .icon(item.getIcon())
            .build();
    }

}
