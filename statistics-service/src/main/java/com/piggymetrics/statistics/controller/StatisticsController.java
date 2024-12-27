package com.piggymetrics.statistics.controller;

import com.piggymetrics.statistics.domain.Account;
import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;

	@GetMapping("/test")
	public String testController() {
		return "test";
	}

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public List<DataPoint> getCurrentAccountStatistics(Principal principal) {
		return statisticsService.findByAccountName(principal.getName());
	}

	@PreAuthorize("#oauth2.hasScope('server') or #accountName.equals('demo')")
	@RequestMapping(value = "/{accountName}", method = RequestMethod.GET)
	public List<DataPoint> getStatisticsByAccountName(@PathVariable String accountName) {
		return statisticsService.findByAccountName(accountName);
	}

	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(value = "/{accountName}", method = RequestMethod.PUT)
	public void saveAccountStatistics(@PathVariable String accountName, @Valid @RequestBody Account account) {
		statisticsService.save(accountName, account);
	}
}
