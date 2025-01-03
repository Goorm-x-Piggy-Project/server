package com.piggymetrics.account.client;

import com.piggymetrics.account.domain.Account;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "statistics-service")
public interface StatisticsServiceClient {

	static final Logger LOGGER = LoggerFactory.getLogger(StatisticsServiceClient.class);

	@RequestMapping(method = RequestMethod.PUT, value = "/api/v1/statistics/{accountName}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@CircuitBreaker(name = "statisticsService", fallbackMethod= "fallbackUpdateStatistics")
	void updateStatistics(@PathVariable("accountName") String accountName, @RequestBody Account account);

	default void fallbackUpdateStatistics(String accountName, Account account, Throwable throwable) {
		LOGGER.error("Error during update statistics for account: {}", accountName);
	}

}
