package com.stockApplication.backend.service;

import java.util.Map;

public interface StatisticsService {

	Map<String, Object> getStatisticsData(String symbol, int pastDaysCount, String exchange);
	
}
