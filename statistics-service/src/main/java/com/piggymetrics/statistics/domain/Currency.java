package com.piggymetrics.statistics.domain;

public enum Currency {

	USD, EUR, RUB, KRW;

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
