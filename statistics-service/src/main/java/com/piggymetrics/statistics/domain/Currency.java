package com.piggymetrics.statistics.domain;

public enum Currency {

	KRW, USD, JPY, EUR;

	public static Currency getBase() {
		return KRW;
	}

	public static boolean isSupported(String currency) {
		try {
			Currency.valueOf(currency);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
