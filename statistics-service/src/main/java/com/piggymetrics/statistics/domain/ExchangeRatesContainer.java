package com.piggymetrics.statistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRatesContainer {

	private List<ExchangeRate> rates; // 통화별 환율 정보 리스트

	@Data
	public static class ExchangeRate {

		@JsonProperty("cur_unit")
		private String currencyUnit; // 통화 단위 (e.g., USD, EUR)

		@JsonProperty("deal_bas_r")
		private BigDecimal dealBaseRate; // 매매기준율 (환율 값)

		@JsonProperty("cur_nm")
		private String currencyName; // 통화명 (e.g., 미국 달러, 유로)
	}
}

