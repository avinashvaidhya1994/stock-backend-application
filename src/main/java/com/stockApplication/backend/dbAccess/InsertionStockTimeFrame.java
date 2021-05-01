package com.stockApplication.backend.dbAccess;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockApplication.backend.entity.TimeFrame;
public class InsertionStockTimeFrame {
	public static void main(String[] args) throws IOException, ParseException {
		
//		 File file = new File("C:\\Users\\skone\\OneDrive\\Desktop\\DB\\Data\\history");
		File file = new File("G:\\Data\\history");
	        File[] files = file.listFiles();
	        for(File f: files){
	            String fileNameWithOutExt = FilenameUtils.removeExtension(f.getName());
	            System.out.println(f);
	            List<TimeFrame> timeFrameList = reteriveFromPrice(fileNameWithOutExt);
	           insertionToData(timeFrameList);
	        }
		
		
	}

	private static List<TimeFrame> reteriveFromPrice(String fileNameWithOutExt) {
		ResultSet results;
		List<TimeFrame> timeFrameList = new ArrayList<TimeFrame>();
		String sql_select = "SELECT MIN(date) AS min, MAX(date) AS max, symbol FROM price where symbol=?";
		
		try(Connection conn = DBConnection.createNewDBconnection()){
			conn.createStatement();
			PreparedStatement stmt = conn.prepareStatement(sql_select);
			stmt.setString(1,fileNameWithOutExt);
			results = stmt.executeQuery();
			 while (results.next()) {
				 
				 TimeFrame timeFrameObject = new TimeFrame();
				
				 timeFrameObject.setSymbol(results.getString("symbol"));
				 timeFrameObject.setStartDate(results.getDate("min"));
				 timeFrameObject.setEndDate(results.getDate("max"));
				 timeFrameList.add(timeFrameObject);
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return timeFrameList;
	}

	private static void insertionToData(List<TimeFrame>timeFrameList) {
		String sql_select = "INSERT INTO timeframe (symbol,startdate,enddate) VALUES (?, ?,?)";
		try(Connection conn = DBConnection.createNewDBconnection()){
			conn.createStatement();
			PreparedStatement statement = conn.prepareStatement(sql_select);
			if(!timeFrameList.isEmpty()) {
				timeFrameList.stream().forEach(timeFrame ->{
					try {
						statement.setString(1, timeFrame.getSymbol());
						statement.setDate(2,timeFrame.getStartDate());
						statement.setDate(3,timeFrame.getEndDate());
						statement.addBatch();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				);
			}
			
			statement.executeBatch();
            conn.close(); 
		} catch (SQLException e) {
			e.printStackTrace();
	}
	}
}
