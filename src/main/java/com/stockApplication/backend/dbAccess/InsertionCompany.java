package com.stockApplication.backend.dbAccess;
import java.io.BufferedReader;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class InsertionCompany {
	private static Statement stmt;
	private static ResultSet results;
	
	public static void main(String[] args) throws IOException {
		
		String sql_select = "INSERT INTO companies (symbol,company_name) VALUES (?, ?)";
		String csvFilePath = "G:\\Data\\symbol.txt";
		int batchSize = 20;
		 
		
		try(Connection conn = DBConnection.createNewDBconnection()){
			
			stmt = conn.createStatement();
			PreparedStatement statement = conn.prepareStatement(sql_select);
			 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); 
            
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String symbol = data[0];
                String name = data[1];
                statement.setString(1, symbol);
                statement.setString(2, name);
                statement.addBatch();
                
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
 
            lineReader.close();
            statement.executeBatch();
            conn.close();
		    
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
