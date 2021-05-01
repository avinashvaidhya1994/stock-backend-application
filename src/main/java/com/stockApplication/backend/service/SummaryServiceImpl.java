package com.stockApplication.backend.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockApplication.backend.entity.CompanySummary;

@Service
public class SummaryServiceImpl implements SummaryService{

	private StockApplicationComponent stockApplicationComponent;
	
	@Autowired
	public SummaryServiceImpl(StockApplicationComponent stockApplicationComponent) {
		this.stockApplicationComponent = stockApplicationComponent;
	}

	@Override
	public Map<String,Object> getSummaryData(String symbol) {
		Map<String, Object> responseMap = new HashMap<>();
		String exchangeSelect = "Select * from exchange where symbol = ?";
		String timeframeSelet = "Select * from timeframe where symbol = ?";
		CompanySummary companySummary = new CompanySummary();
		try {
			PreparedStatement exchangeSelectStatement = stockApplicationComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			ResultSet result = exchangeSelectStatement.executeQuery();
			if(!result.next()) {
				responseMap.put("errorMessage", "Symbol: " + symbol + " , not found");
				return responseMap;
			}
			PreparedStatement timeframeSelectStatement = stockApplicationComponent.getConnection().prepareStatement(timeframeSelet);
			timeframeSelectStatement.setString(1, symbol);
			ResultSet result3 = timeframeSelectStatement.executeQuery();
			if(!result3.next()) {
				responseMap.put("errorMessage","Data for " + symbol + ", not found");
				return responseMap;
			}
			formHighPriceAndVolumeData(symbol,responseMap,companySummary);
			if(responseMap.containsKey("errorMessage")) {
				return responseMap;
			}
			companySummary.setSymbol(symbol);
			companySummary.setCompany(result.getString("company"));
			companySummary.setMarket(result.getString("market"));
			companySummary.setStartdate(result3.getDate("startdate"));
			companySummary.setEnddate(result3.getDate("enddate"));
			responseMap.put("dataForSummaryPage", companySummary);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return responseMap;
	}

	private void formHighPriceAndVolumeData(String symbol, Map<String, Object> resultMap, CompanySummary companySummary) {
		String highestPriceSelect = "Select * from price where symbol = ? and high = (select max(high) from price where symbol = ?)";
		String highestVolumeSelect = "Select * from price where symbol = ? and volume = (select max(volume) from price where symbol = ?)";
		try {
			PreparedStatement statement1 = stockApplicationComponent.getConnection().prepareStatement(highestPriceSelect);
			statement1.setString(1, symbol);
			statement1.setString(2, symbol);
			ResultSet result1 = statement1.executeQuery();
			if(!result1.next()) {
				resultMap.put("errorMessage","Data for " + symbol + ", not found");
				return;
			}
			PreparedStatement statement2 = stockApplicationComponent.getConnection().prepareStatement(highestVolumeSelect);
			statement2.setString(1, symbol);
			statement2.setString(2, symbol);
			ResultSet result2 = statement2.executeQuery();
			if(!result2.next()) {
				resultMap.put("errorMessage","Data for " + symbol + ", not found");
				return;
			}
			String highestPriceWithDate = String.valueOf(result1.getFloat("high")) + " ( " + String.valueOf(result1.getDate("date"))  + " )";
			String highestVolumeWithDate = String.valueOf(result2.getInt("volume")) + " ( " + String.valueOf(result2.getDate("date"))  + " )";
			companySummary.setHighestPriceWithDate(highestPriceWithDate);
			companySummary.setHighestVolumeWithDate(highestVolumeWithDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
			
}
