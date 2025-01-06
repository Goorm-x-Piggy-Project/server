package com.piggymetrics.account.domain;

public enum Currency {

	USD, EUR, KRW, JPY;

	public static Currency getDefault() {
		return KRW;
	}
}
