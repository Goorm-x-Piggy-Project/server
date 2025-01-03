package com.piggymetrics.statistics.controller;

import com.piggymetrics.statistics.domain.Account;
import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.service.StatisticsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

	private final StatisticsService statisticsService;

	public StatisticsController(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@GetMapping("/hello")
	public String testStatisticsController() {
		return "statistics-controller";
	}

	@GetMapping("/current")
	public List<DataPoint> getCurrentAccountStatistics(Principal principal) {
		return statisticsService.findByAccountName(principal.getName());
	}

	@PreAuthorize("hasAuthority('SCOPE_server') or #accountName.equals('demo')")
	@GetMapping("/{accountName}")
	public List<DataPoint> getStatisticsByAccountName(@PathVariable String accountName) {
		return statisticsService.findByAccountName(accountName);
	}

	@PreAuthorize("hasAuthority('SCOPE_server')")
	@PutMapping("/{accountName}")
	public void saveAccountStatistics(@PathVariable String accountName, @Valid @RequestBody Account account) {
		statisticsService.save(accountName, account);
	}
}
