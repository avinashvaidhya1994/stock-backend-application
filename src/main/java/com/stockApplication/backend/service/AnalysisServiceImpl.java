package com.stockApplication.backend.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockApplication.backend.entity.CompanyMovingAverage;
import com.stockApplication.backend.entity.CompanyPrice;

@Service
public class AnalysisServiceImpl implements AnalysisService{

	private StockApplicationComponent stockApplicationComponent;
	
	@Autowired
	public AnalysisServiceImpl(StockApplicationComponent stockApplicationComponent) {
		this.stockApplicationComponent = stockApplicationComponent;
	}

	@Override
	public Map<String, Object> getQuarterReport(String symbol) {
		Map<String, Object> responseMap = new HashMap<>();
		String select = "Select * from exchange where symbol = ?";
		try {
			PreparedStatement statement = stockApplicationComponent.getConnection().prepareStatement(select);
			statement.setString(1, symbol);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				responseMap.put("errorMessage",  symbol + " , not found ");
				return responseMap;
			}
			String company = rs.getString("company");
			List<CompanyPrice> priceList = getQuarterprice(symbol, company);
			if(priceList.isEmpty()) {
				responseMap.put("errorMessage","Data for " + symbol + ", not found");
				return responseMap;
			}
			priceList.sort(Comparator.comparing(CompanyPrice::getDate));
			responseMap.put("qnarterReport", priceList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseMap;
	}

	private List<CompanyPrice> getQuarterprice(String symbol,String company){
		String priceSelect = "Select * from price where symbol = ? order by date desc";
		List<CompanyPrice> companyPriceList = new ArrayList<>();
		try {
			PreparedStatement priceSelectStatement = stockApplicationComponent.getConnection().prepareStatement(priceSelect);
			priceSelectStatement.setString(1, symbol);
			ResultSet rs2 = priceSelectStatement.executeQuery();
			int i = 0;
			int firstMonth = 0;
			while(rs2.next()) {
				Date date = rs2.getDate("date");
				int month = date.toLocalDate().getMonthValue();
				if(i == 0)
					firstMonth = month;
				i++;				
				if(month != firstMonth && month % 3 == 0 )
					break;
				CompanyPrice companyPrice = new CompanyPrice();
				companyPrice.setCompany(company);
				companyPrice.setSymbol(symbol);
				companyPrice.setOpen(rs2.getFloat("open"));
				companyPrice.setClose(rs2.getFloat("price"));
				companyPrice.setHigh(rs2.getFloat("high"));
				companyPrice.setLow(rs2.getFloat("low"));
				companyPrice.setVolumeTraded(rs2.getInt("volume"));
				companyPrice.setDate(date);
				companyPriceList.add(companyPrice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companyPriceList;
	}
	
	@Override
	public Map<String, Object> getComparisonData(String symbol1, String symbol2) {
		Map<String, Object> responseMap = new HashMap<>();
		String company1 = getCompanyName(symbol1);
		if(company1.equals("errorMessage")) {
			responseMap.put(company1, "Symbol : " + symbol1 + " Not found");
			return responseMap;
		}
		String company2 = getCompanyName(symbol2);
		if(company2.equals("errorMessage")) {
			responseMap.put(company2, "Symbol : " + symbol2 + " Not found");
			return responseMap;
		}
		List<CompanyPrice> symbol1Data = getPriceDetails(responseMap,symbol1,company1);
		if(responseMap.containsKey("errorMessage"))
			return responseMap;
		if(symbol1Data.isEmpty()) {
			responseMap.put("errorMessage","Data for " + symbol1 + ", not found");
			return responseMap;
		}
		List<CompanyPrice> symbol2Data = getPriceDetails(responseMap, symbol2,company2);
		if(symbol2Data.isEmpty()) {
			responseMap.put("errorMessage","Data for " + symbol2 + ", not found");
			return responseMap;
		}
		responseMap.put("company1", symbol1Data);
		responseMap.put("company2", symbol2Data);
		return responseMap;
	}
	
	private String getCompanyName(String symbol) {
		String exchangeSelect = "Select * from exchange where symbol = ?";
		PreparedStatement statement;
		String company = "";
		try {
			statement = stockApplicationComponent.getConnection().prepareStatement(exchangeSelect);
			statement.setString(1, symbol);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				return "errorMessage";
			}
			 company = rs.getString("company");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return company;
	}
	
	private List<CompanyPrice> getPriceDetails(Map<String, Object> resultMap, String symbol,String company){
		String select = "Select * from price where symbol = ? order by date asc";
		List<CompanyPrice> priceList = new ArrayList<>();
		try {
			PreparedStatement statement = stockApplicationComponent.getConnection().prepareStatement(select);
			statement.setString(1, symbol);
			ResultSet rs2 = statement.executeQuery();
			int i = 0;
			float intital = 0f;
			float changeFromInitial = 0f;
			while(rs2.next()) {
				if(i < 1) {
					intital = rs2.getFloat("price");
					changeFromInitial = 0f;
				}else {
					changeFromInitial = (( rs2.getFloat("price") - intital)/intital) * 100;
				}
				i++;
				CompanyPrice companyPrice = new CompanyPrice();
				companyPrice.setCompany(company);
				companyPrice.setSymbol(symbol);
				companyPrice.setOpen(rs2.getFloat("open"));
				companyPrice.setClose(rs2.getFloat("price"));
				companyPrice.setHigh(rs2.getFloat("high"));
				companyPrice.setLow(rs2.getFloat("low"));
				companyPrice.setVolumeTraded(rs2.getInt("volume"));
				companyPrice.setDate(rs2.getDate("date"));
				companyPrice.setChangeFromFirstDate(changeFromInitial);
				priceList.add(companyPrice);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return priceList;
		
	}

	@Override
	public Map<String, Object> getMovingAverage(String symbol,int years) {
		Map<String, Object> responseMap = new HashMap<>();
		String exchangeSelect = "Select * from exchange where symbol = ?";
		try {
			PreparedStatement exchangeSelectStatement = stockApplicationComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			ResultSet rs = exchangeSelectStatement.executeQuery();
			if(!rs.next()) {
				responseMap.put("errorMessage", symbol + " , not found ");
				return responseMap;
			}
			String company = rs.getString("companyName");
			List<CompanyMovingAverage> movingAverageList = getCompanyData(symbol,company,years);
			if(movingAverageList.isEmpty()) {
				responseMap.put("errorMessage","Data for " + symbol + ", not found");
				return responseMap;
			}
			
			movingAverageList.sort(Comparator.comparing(CompanyMovingAverage::getDate));
			calculateMovingAverage(movingAverageList);
			responseMap.put("companyMovingAvgData", movingAverageList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseMap;
	}

	private List<CompanyMovingAverage> getCompanyData(String symbol,String company,int years){
		String select = "Select * from price where symbol = ? order by date desc";
		List<CompanyMovingAverage> movingAverageList = new ArrayList<>();
		try {
			PreparedStatement priceSelectStatement = stockApplicationComponent.getConnection().prepareStatement(select);
			priceSelectStatement.setString(1, symbol);
			ResultSet rs2 = priceSelectStatement.executeQuery();
			int count = 0;			
			while(rs2.next() && count < ((years*365))) {
				count++;
				Date tradeDate = rs2.getDate("date");
				CompanyMovingAverage companyMovingAverage = new CompanyMovingAverage();
				companyMovingAverage.setCompany(company);
				companyMovingAverage.setSymbol(symbol);
				companyMovingAverage.setPrice(rs2.getFloat("price"));
				companyMovingAverage.setDate(tradeDate);
				movingAverageList.add(companyMovingAverage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movingAverageList;
	}
	
	private void calculateMovingAverage(List<CompanyMovingAverage> movingAverageList) {
		float fiftyDaySum = 0f;
		float twoHundredDaySum = 0f;
		for (int i = 0; i < movingAverageList.size(); i++) {
			fiftyDaySum += movingAverageList.get(i).getPrice();
			if(i >= 50) 
				fiftyDaySum -= movingAverageList.get(i-50).getPrice();
			float fiftyDayAverage = i >= 50 ? fiftyDaySum / 50 : fiftyDaySum / (i+1);
			twoHundredDaySum += movingAverageList.get(i).getPrice();
			if(i >= 200) 
				twoHundredDaySum -= movingAverageList.get(i-200).getPrice();
			float twoHundredDayAverage = i >= 200 ? twoHundredDaySum / 200 : twoHundredDaySum / (i+1);
			movingAverageList.get(i).setFiftyDayAvg(fiftyDayAverage);
			movingAverageList.get(i).setTwoHundredDayAvg(twoHundredDayAverage);
		}
	}
}
