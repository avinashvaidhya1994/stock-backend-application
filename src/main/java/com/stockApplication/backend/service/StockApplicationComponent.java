package com.stockApplication.backend.service;

import java.sql.Connection;

import org.springframework.stereotype.Component;

import com.stockApplication.backend.dbAccess.DBConnection;

@Component
public class StockApplicationComponent {

	private Connection conn;
		
	public StockApplicationComponent() {
		this.conn = DBConnection.createNewDBconnection();
	}

	public Connection getConnection() {
		return conn;
	}
	
}
