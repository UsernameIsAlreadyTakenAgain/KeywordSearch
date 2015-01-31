import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;


public class MySQL {
	
	Connection_to_MySQL link=new Connection_to_MySQL();
	
	
	public MySQL(){	
		link.toMySQL();	
	}
	
	
	
	public List<String> getTableName(){
		List<String> tables=new ArrayList<>();
		
		try {
			PreparedStatement statement=link.con.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='homework2'");
            ResultSet result=statement.executeQuery();
			
			while(result.next()){
				tables.add(result.getString(1));
				//System.out.println(result.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	
	
	public List<String> getColumnName(String tableName){
		List<String> columns=new ArrayList<>();
		
		PreparedStatement statement;
		try {
			statement = link.con.prepareStatement("SELECT column_name FROM information_schema.columns WHERE table_name = '"+tableName+"'");
			ResultSet result=statement.executeQuery();
			
			while(result.next()){
				columns.add(result.getString(1));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return columns;	
	}
	
	
	
	
	public List<String> getValue(String columnName, String tableName){
		List<String> values=new ArrayList<>();
		
		try {
			PreparedStatement statement=link.con.prepareStatement("select distinct "+columnName+" from "+tableName+"");
			ResultSet result=statement.executeQuery();
			
			while(result.next()){
				values.add(result.getString(1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
	
}
