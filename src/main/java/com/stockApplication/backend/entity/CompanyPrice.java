package com.stockApplication.backend.entity;

import java.sql.Date;

public class CompanyPrice {

	private String company;
	private String symbol;
	private int volumeTraded;
	private float open;
	private float close;
	private float high;
	private float low;
	private Date date;
	private float changeFromFirstDate;
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
	public int getVolumeTraded() {
		return volumeTraded;
	}
	public void setVolumeTraded(int volumeTraded) {
		this.volumeTraded = volumeTraded;
	}
	public float getOpen() {
		return open;
	}
	public void setOpen(float open) {
		this.open = open;
	}
	public float getClose() {
		return close;
	}
	public void setClose(float close) {
		this.close = close;
	}
	public float getHigh() {
		return high;
	}
	public void setHigh(float high) {
		this.high = high;
	}
	public float getLow() {
		return low;
	}
	public void setLow(float low) {
		this.low = low;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getChangeFromFirstDate() {
		return changeFromFirstDate;
	}
	public void setChangeFromFirstDate(float changeFromFirstDate) {
		this.changeFromFirstDate = changeFromFirstDate;
	}
	
	
}
