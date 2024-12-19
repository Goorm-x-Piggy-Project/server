//Account service 와의 통신을 위한 HTTP 클라이언트 역할.
//OpenFeign이나 RestTemplate 등으로 외부 서비스와 데이터 주고받음.

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 9:45
수정 내용 : @GetMapping 변경, MediaType 업데이트, 주석 추가
*/

package com.piggymetrics.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * AccountServiceClient 인터페이스는 account-service와 통신하기 위한 Feign 클라이언트.
 * 주요 역할:
 * - account-service로부터 특정 계정 정보를 가져옴.
 *
 * @FeignClient: Feign 클라이언트를 정의하며, account-service와 연결.
 */
@FeignClient(name = "account-service")
public interface AccountServiceClient {

	/**
	 * 특정 계정의 정보를 가져오는 메서드.
	 *
	 * @param accountName 계정 이름
	 * @return 계정 정보 JSON 문자열
	 */
	@GetMapping(value = "/accounts/{accountName}", consumes = MediaType.APPLICATION_JSON_VALUE)
	String getAccount(@PathVariable("accountName") String accountName);

}

