package com.stockApplication.backend.service;

import java.util.Map;

public interface SummaryService {

	Map<String,Object> getSummaryData(String symbol);
	
}
