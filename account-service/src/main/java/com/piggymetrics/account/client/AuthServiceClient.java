package com.piggymetrics.account.client;

import com.piggymetrics.account.dto.UserReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

	@RequestMapping(method = RequestMethod.POST, value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	void createUser(UserReqDto userReqDto);

}
