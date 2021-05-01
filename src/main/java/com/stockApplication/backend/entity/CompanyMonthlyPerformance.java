package com.stockApplication.backend.entity;

public class CompanyMonthlyPerformance{

	private String company;
	private String symbol;
	private int allocatedLotSize;
	private float gainLossPerShare;
	private float gainLossPercentage;
	private float gainLossPerLot;
	private int tradeYear;
	private int tradeMonth;
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getAllocatedLotSize() {
		return allocatedLotSize;
	}
	public void setAllocatedLotSize(int allocatedLotSize) {
		this.allocatedLotSize = allocatedLotSize;
	}
	public float getGainLossPerShare() {
		return gainLossPerShare;
	}
	public void setGainLossPerShare(float gainLossPerShare) {
		this.gainLossPerShare = gainLossPerShare;
	}
	public float getGainLossPercentage() {
		return gainLossPercentage;
	}
	public void setGainLossPercentage(float gainLossPercentage) {
		this.gainLossPercentage = gainLossPercentage;
	}
	public float getGainLossPerLot() {
		return gainLossPerLot;
	}
	public void setGainLossPerLot(float gainLossPerLot) {
		this.gainLossPerLot = gainLossPerLot;
	}
	public int getTradeYear() {
		return tradeYear;
	}
	public void setTradeYear(int tradeYear) {
		this.tradeYear = tradeYear;
	}
	public int getTradeMonth() {
		return tradeMonth;
	}
	public void setTradeMonth(int tradeMonth) {
		this.tradeMonth = tradeMonth;
	}
	
}
