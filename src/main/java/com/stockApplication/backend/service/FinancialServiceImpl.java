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

import com.stockApplication.backend.entity.CompanyMonthlyPerformance;
import com.stockApplication.backend.entity.CompanyPrice;

@Service
public class FinancialServiceImpl implements FinancialService{

	private StockApplicationComponent stockApplicationComponent;
	
	@Autowired
	public FinancialServiceImpl(StockApplicationComponent stockApplicationComponent) {
		this.stockApplicationComponent = stockApplicationComponent;
	}

	@Override
	public Map<String, Object> getFinancialDataForRange(String symbol, String fromDate, String toDate) {
		Map<String, Object> responseMap = new HashMap<>();
		String select = "Select * from exchange where symbol = ?";
		try {
			PreparedStatement statement = stockApplicationComponent.getConnection().prepareStatement(select);
			statement.setString(1, symbol);
			ResultSet rs = statement.executeQuery();
			if(!rs.next()) {
				responseMap.put("errorMessage", symbol + " , not found ");
				return responseMap;
			}
			String company = rs.getString("company");
			List<CompanyPrice> priceList = formFinancialData(symbol, company, fromDate, toDate);
			if(priceList.isEmpty()) {
				responseMap.put("errorMessage","Data for " + symbol + ", not found");
				return responseMap;
			}		
			responseMap.put("financialRangeData", priceList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseMap;
	}
	
	private List<CompanyPrice> formFinancialData(String symbol,String company, String fromDate,String toDate){
		String select = "Select * from price where symbol = ? and date between ? and ? ";
		List<CompanyPrice> comapnyPriceList = new ArrayList<>();	
		try {
			PreparedStatement statement = stockApplicationComponent.getConnection().prepareStatement(select);
			statement.setString(1, symbol);
			statement.setDate(2, Date.valueOf(fromDate));
			statement.setDate(3, Date.valueOf(toDate));
			ResultSet rs2 = statement.executeQuery();
			while(rs2.next()) {
				CompanyPrice companyPrice = new CompanyPrice();
				companyPrice.setCompany(company);
				companyPrice.setSymbol(symbol);
				companyPrice.setOpen(rs2.getFloat("open"));
				companyPrice.setClose(rs2.getFloat("price"));
				companyPrice.setHigh(rs2.getFloat("high"));
				companyPrice.setLow(rs2.getFloat("low"));
				companyPrice.setVolumeTraded(rs2.getInt("volume"));
				companyPrice.setDate(rs2.getDate("date"));
				comapnyPriceList.add(companyPrice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comapnyPriceList;
	}

	@Override
	public Map<String, Object> getMonthlyData(String symbol, String fromDate, String toDate, int lotSize) {
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
			String company = rs.getString("company");
			List<CompanyMonthlyPerformance> monthlyReportList = getMonthlyList(symbol,company, fromDate, toDate,lotSize ,responseMap);
			if(responseMap.containsKey("errorMessage")) {
				return responseMap;
			}
			responseMap.put("monthlyReportDetail", monthlyReportList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseMap;
	}
	
	private List<CompanyMonthlyPerformance> getMonthlyList(String symbol,String company,String fromDate,
			String toDate,int lotSize,Map<String,Object> responseMap){
		List<CompanyMonthlyPerformance> monthlyReportList = new ArrayList<>();
		String priceSelect = "Select * from price where symbol = ? and date between ? and ?";
		try {
			PreparedStatement statement = stockApplicationComponent.getConnection().prepareStatement(priceSelect);
			statement.setString(1, symbol);
			statement.setDate(2, Date.valueOf(fromDate));
			statement.setDate(3, Date.valueOf(toDate));
			ResultSet rs2 = statement.executeQuery();
			float intialPrice = 0;
			int month = 0;
			CompanyMonthlyPerformance companyMonthlyPerformance = null;
			while(rs2.next()) {
				Date tradeDate = rs2.getDate("date");
				if(month != tradeDate.toLocalDate().getMonthValue()) {
					month = tradeDate.toLocalDate().getMonthValue();
					intialPrice = rs2.getFloat("price");
					if(companyMonthlyPerformance != null)
						monthlyReportList.add(companyMonthlyPerformance);
					companyMonthlyPerformance = new CompanyMonthlyPerformance();
					continue;
				}
				float currentPrice = rs2.getFloat("price");
				float perShare = currentPrice - intialPrice;
				float percentage = (perShare/intialPrice) * 100;
				companyMonthlyPerformance.setSymbol(symbol);
				companyMonthlyPerformance.setCompany(company);
				companyMonthlyPerformance.setGainLossPercentage(percentage);
				companyMonthlyPerformance.setTradeYear(tradeDate.toLocalDate().getYear());
				companyMonthlyPerformance.setTradeMonth(month);
				companyMonthlyPerformance.setAllocatedLotSize(lotSize);
				companyMonthlyPerformance.setGainLossPerShare(perShare);
				companyMonthlyPerformance.setGainLossPerLot(perShare*lotSize);
			}
			if(monthlyReportList.isEmpty()) {
				responseMap.put("errorMessage","Data for " + symbol + ", not found");
				return monthlyReportList;
			}
			if(!monthlyReportList.contains(companyMonthlyPerformance) && companyMonthlyPerformance.getCompany() != null 
					&& !companyMonthlyPerformance.getCompany().isEmpty() )
					monthlyReportList.add(companyMonthlyPerformance);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return monthlyReportList;
	}
}
