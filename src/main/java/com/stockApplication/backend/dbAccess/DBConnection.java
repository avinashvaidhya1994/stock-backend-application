package com.stockApplication.backend.dbAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
	
//	private static String dbhost = "jdbc:mysql://192.168.1.225:3306/stock";
	private static String dbhost = "jdbc:mysql://localhost:3306/stock";
	private static String username = "root";
//	private static String password = "password";
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
