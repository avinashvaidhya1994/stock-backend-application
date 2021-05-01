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

import com.stockApplication.backend.service.SummaryService;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/stock/summary")
public class SummaryController {
	
	@Autowired
	SummaryService summaryService;
	
	public SummaryController(SummaryService summaryService) {
		this.summaryService = summaryService;
	}
	
	@GetMapping(value = "/{symbol}")
	ResponseEntity<Map> getSummaryDetails(@PathVariable(name = "symbol",required = true) String symbol){
		return ResponseEntity.status(HttpStatus.OK).body(summaryService.getSummaryData(symbol));
	}
	
}
