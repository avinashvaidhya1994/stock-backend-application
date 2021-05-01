package com.stockApplication.backend.entity;

import java.sql.Date;

public class CompanyMovingAverage {
	
	private String company;
	private String symbol;
	private Date date;
	private float price;
	private float fiftyDayAvg;
	private float twoHundredDayAvg;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getFiftyDayAvg() {
		return fiftyDayAvg;
	}
	public void setFiftyDayAvg(float fiftyDayAvg) {
		this.fiftyDayAvg = fiftyDayAvg;
	}
	public float getTwoHundredDayAvg() {
		return twoHundredDayAvg;
	}
	public void setTwoHundredDayAvg(float twoHundredDayAvg) {
		this.twoHundredDayAvg = twoHundredDayAvg;
	}
		
	
}
