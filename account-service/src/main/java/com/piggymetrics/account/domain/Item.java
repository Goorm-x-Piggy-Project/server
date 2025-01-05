package com.piggymetrics.account.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

//	@Id
//	private ObjectId id; // Item이 컬렉션이 아니라 그냥 값인거 같음

	@NotNull
	@Size(min = 1, max = 20)
	private String title;

	@NotNull
	private Long amount;

	@NotNull
	private Currency currency;

	@NotNull
	private TimePeriod period;

	@NotNull
	private String icon;


}
