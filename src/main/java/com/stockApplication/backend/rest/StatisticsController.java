package com.stockApplication.backend.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stockApplication.backend.service.StatisticsService;


@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/stock/statistics")
public class StatisticsController {
	
	@Autowired
	StatisticsService statisticsService;
	
	public StatisticsController(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}
	
	@GetMapping(value = "/{symbol}")
	ResponseEntity<Map> getStatisticsDetail(@PathVariable(name = "symbol",required = true) String symbol,
			@RequestParam(name = "days",required = true) int days,
			@RequestParam(name= "market",required = true) String market){
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getStatisticsData(symbol,days,market));
	}
	
}
