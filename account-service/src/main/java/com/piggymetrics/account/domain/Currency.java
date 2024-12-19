package com.piggymetrics.account.domain;

public enum Currency {

	USD, EUR, RUB, WON;

	public static Currency getDefault() {
		return WON;
	}
}
