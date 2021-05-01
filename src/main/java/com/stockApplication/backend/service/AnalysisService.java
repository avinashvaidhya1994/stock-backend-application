package com.stockApplication.backend.service;

import java.util.Map;

public interface AnalysisService {

	Map<String, Object> getQuarterReport(String symbol);

	Map<String, Object> getComparisonData(String symbol1, String symbol2);

	Map<String, Object> getMovingAverage(String symbol,int years);
	
}
