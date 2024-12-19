package com.piggymetrics.statistics.domain;

public enum Currency {

	KRW, USD, JPY, EUR;

	public static Currency getBase() {
		return KRW;
	}
}
