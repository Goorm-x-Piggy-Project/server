package com.piggymetrics.notification.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

	// GET 요청
	@GetMapping(value = "/accounts/{accountName}", consumes = MediaType.APPLICATION_JSON_VALUE)
	String getAccount(@PathVariable("accountName") String accountName);

}
