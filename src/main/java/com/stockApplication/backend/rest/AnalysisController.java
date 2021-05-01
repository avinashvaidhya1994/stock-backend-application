package com.stockApplication.backend.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stockApplication.backend.service.AnalysisService;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/stockapp/analysis")
public class AnalysisController {
	
	@Autowired
	AnalysisService analysisService;
	
	public AnalysisController(AnalysisService analysisService) {
		this.analysisService = analysisService;
	}
	
	@GetMapping(value = "/currrentQuarter")
	ResponseEntity<Map> getCurrentQuarter(@RequestParam(name = "symbol",required = true) String symbol){
		return ResponseEntity.status(HttpStatus.OK).body(analysisService.getQuarterReport(symbol));
	}
	
	@GetMapping(value = "/compare")
	ResponseEntity<Map> getComparisonDetail(@RequestParam(name = "symbol1",required = true) String symbol1,
			@RequestParam(name = "symbol2",required = true) String symbol2){
		return ResponseEntity.status(HttpStatus.OK).body(analysisService.getComparisonData(symbol1,symbol2));
	}
	
	@GetMapping(value = "/movingAverage")
	ResponseEntity<Map> getMMovingAverageData(@RequestParam(name = "symbol",required = true) String symbol,
			@RequestParam(name = "years",required = true) int years){
		return ResponseEntity.status(HttpStatus.OK).body(analysisService.getMovingAverage(symbol,years));
	}
	
	
}
