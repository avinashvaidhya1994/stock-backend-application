package com.stockApplication.backend.entity;

import java.sql.Date;

public class CompanySummary {
	
	private String company;
	private String symbol;
	private String market;
	private String highestPriceWithDate;
	private String highestVolumeWithDate;
	private Date startdate;
	private Date enddate;
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
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getHighestPriceWithDate() {
		return highestPriceWithDate;
	}
	public void setHighestPriceWithDate(String highestPriceWithDate) {
		this.highestPriceWithDate = highestPriceWithDate;
	}
	public String getHighestVolumeWithDate() {
		return highestVolumeWithDate;
	}
	public void setHighestVolumeWithDate(String highestVolumeWithDate) {
		this.highestVolumeWithDate = highestVolumeWithDate;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	
}
