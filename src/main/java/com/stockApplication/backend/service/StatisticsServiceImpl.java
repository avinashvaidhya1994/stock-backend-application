package com.stockApplication.backend.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockApplication.backend.entity.CompanyPrice;

@Service
public class StatisticsServiceImpl implements StatisticsService{

	private StockApplicationComponent stockApplicationComponent;
	
	@Autowired
	public StatisticsServiceImpl(StockApplicationComponent stockApplicationComponent) {
		this.stockApplicationComponent = stockApplicationComponent;
	}

	@Override
	public Map<String, Object> getStatisticsData(String symbol, int days, String exchange) {
		Map<String, Object> responseMap = new HashMap<>();
		String select = "Select * from exchange where symbol = ? and market = ?";
		try {
			PreparedStatement statement1 = stockApplicationComponent.getConnection().prepareStatement(select);
			statement1.setString(1, symbol);
			statement1.setString(2, exchange);
			ResultSet rs = statement1.executeQuery();
			if(!rs.next()) {
				responseMap.put("errorMessage", "Symbol: " + symbol + " , not found in " + exchange);
				return responseMap;
			}
			String companyName = rs.getString("company");
			List<CompanyPrice> companyPriceList = formData(symbol, companyName, days);
			if(companyPriceList.isEmpty()) {
				responseMap.put("errorMessage","Data for " + symbol + ", not found");
				return responseMap;
			}
			responseMap.put("companyStatisticData", companyPriceList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseMap;
	}
	
	private List<CompanyPrice>  formData(String symbol,String company, int days) {
		List<CompanyPrice> companyPriceList = new ArrayList<>();
		String priceSelect = "Select * from price where symbol = ? order by date desc";
		try {
			PreparedStatement priceSelectStatement = stockApplicationComponent.getConnection().prepareStatement(priceSelect);
			priceSelectStatement.setString(1, symbol);
			ResultSet rs2 = priceSelectStatement.executeQuery();
			int i = 0;
			while(rs2.next() && i < days) {
				i++;
				CompanyPrice companyPriceData = new CompanyPrice();
				companyPriceData.setCompany(company);
				companyPriceData.setSymbol(symbol);
				companyPriceData.setOpen(rs2.getFloat("open"));
				companyPriceData.setClose(rs2.getFloat("price"));
				companyPriceData.setHigh(rs2.getFloat("high"));
				companyPriceData.setLow(rs2.getFloat("low"));
				companyPriceData.setVolumeTraded(rs2.getInt("volume"));
				companyPriceData.setDate(rs2.getDate("date"));
				companyPriceList.add(companyPriceData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companyPriceList;
	}
}
