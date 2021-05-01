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

import com.stockApplication.backend.service.FinancialService;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/stock/financial")
public class FinancialController {
	
	@Autowired
	FinancialService financialService;
	
	public FinancialController(FinancialService financialService) {
		this.financialService = financialService;
	}
	
	@GetMapping(value = "/range/{symbol}")
	ResponseEntity<Map> getFinancialReport(@PathVariable(name = "symbol",required = true) String symbol,
			@RequestParam(name = "fromdate",required = true) String fromdate,
			@RequestParam(name = "todate",required = true) String todate){
		return ResponseEntity.status(HttpStatus.OK).body(financialService.getFinancialDataForRange(symbol,fromdate,todate));
	}
	
	@GetMapping(value = "/monthly/{symbol}")
	ResponseEntity<Map> getMonthlyFinancialReport(@PathVariable(name = "symbol",required = true) String symbol,
			@RequestParam(name = "fromdate",required = true) String fromdate,
			@RequestParam(name = "todate",required = true) String todate,
			@RequestParam(name = "allocatedLotSize",required = true) int allocatedLotSize){
		return ResponseEntity.status(HttpStatus.OK).body(financialService.getMonthlyData(symbol,fromdate,todate,allocatedLotSize));
	}
	
}
