package com.stockApplication.backend.service;

import java.util.Map;

public interface FinancialService {

	Map<String, Object> getFinancialDataForRange(String symbol, String fromDate, String toDate);

	Map<String, Object> getMonthlyData(String symbol, String fromDate, String toDate, int lotSize);
	
}
