package com.stockApplication.backend.dbAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CreateTablesAndSchema {
	
	public static void main(String[] args) {
		try(Connection conn = createNewDBconnection()){
			createSchemaAndTables(conn);
			System.out.println("Tables and schema created");
		}catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	private static void createSchemaAndTables(Connection connection) {
		try {
			connection.prepareStatement("DROP SCHEMA if exists stock").execute();
			PreparedStatement statement = connection.prepareStatement("CREATE DATABASE stock"); 
			statement.execute();
			connection.setSchema("stock");
			String createCompany = "CREATE TABLE stock.companies ("+ 
					"					symbol varchar(25),"+ 
					"					company_name varchar(150)," + 
					"					Constraint PK Primary Key(symbol)"+ 
					"					)";
			String createExchange = "CREATE TABLE stock.exchange (" + 
					"    company varchar(150)," + 
					"    symbol varchar(25)," + 
					"market varchar(50)" + 
					")";
			String createPrice = "CREATE TABLE stock.price (" + 
					"    symbol varchar(25)," + 
					"    volume int," + 
					"    price float," + 
					"    open float," + 
					"    low float," + 
					"    high float," + 
					"    date date" + 
					")";
			String createTimeframe = "CREATE TABLE stock.timeframe (" + 
					"    symbol varchar(25)," + 
					"    startdate date," + 
					"    enddate date" + 
					")";
			connection.prepareStatement(createCompany).execute();
			connection.prepareStatement(createExchange).execute();
			connection.prepareStatement(createPrice).execute();
			connection.prepareStatement(createTimeframe).execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String dbhost = "jdbc:mysql://localhost:3306";
	private static String username = "root";
//	private static String password = "Shimsha06$";
	private static String password = "root";
	private static Connection conn;
	
	@SuppressWarnings("finally")
	public static Connection createNewDBconnection() {
		try  {	
			conn = DriverManager.getConnection(
					dbhost, username, password);	
		} catch (SQLException e) {
			System.out.println("Cannot create database connection");
			e.printStackTrace();
		} finally {
			return conn;	
		}		
	}
}
