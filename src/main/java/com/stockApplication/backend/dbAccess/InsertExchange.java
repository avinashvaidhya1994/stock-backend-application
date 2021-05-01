package com.stockApplication.backend.dbAccess;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class InsertExchange {
		private static Statement stmt;
		private static ResultSet results;
		
		public static void main(String[] args) throws IOException {
			
			String sql_select = "INSERT INTO exchange (symbol,market,company) VALUES (?,?,?)";
//			String csvFilePath = "C:\\Users\\skone\\OneDrive\\Desktop\\DB\\Data\\exchange.csv";
			String amexExchangePath = "G:\\Data\\AMEX.txt";
			String nasdaqExchangePath = "G:\\Data\\NASDAQ.txt";
			String nyseEchangePath = "G:\\Data\\NYSE.txt";
			List<String> pathList = new ArrayList<>();
			pathList.add(amexExchangePath);
			pathList.add(nasdaqExchangePath);
			pathList.add(nyseEchangePath);
			int batchSize = 20;
			 
			
			try(Connection conn = DBConnection.createNewDBconnection()){
				
				stmt = conn.createStatement();
				PreparedStatement statement = conn.prepareStatement(sql_select);
				for(int i = 0; i < pathList.size(); i++) {
					String path = pathList.get(i);
					String fileNameWithOutExt = FilenameUtils.removeExtension(new File(path).getName());
					BufferedReader lineReader = new BufferedReader(new FileReader(path));
					String lineText = null;
					int count = 0;
					lineReader.readLine(); 
					
					while ((lineText = lineReader.readLine()) != null) {
						String[] data = lineText.split(",");
						String company = data[0];
						String symbol = data[1];
						String market = fileNameWithOutExt;
						
						statement.setString(1, symbol);
						statement.setString(2, market);
						statement.setString(3, company.replace("\"", ""));
						System.out.println("Exchange : " + market + ", Symbol : " + symbol);
						statement.execute();
//						statement.addBatch();
//						
//						if (count % batchSize == 0) {
//							statement.executeBatch();
//						}
					}
					
					lineReader.close();
//					statement.executeBatch();
					
				}
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

	}


