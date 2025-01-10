package com.piggymetrics.statistics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableFeignClients(basePackages = "com.piggymetrics.statistics.client")
public class StatisticsServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

}
