import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.neo4j.graphdb.Node;

import com.mysql.jdbc.Connection;


public class MySQL {
	
	Connection_to_MySQL link=new Connection_to_MySQL();
	
	
	public MySQL(String dbname){	
		link.toMySQL(dbname);	
	}
	
	
	
	public List<String> getTableName(String database){
		List<String> tables=new ArrayList<>();
		
		try {
			PreparedStatement statement=link.con.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='"+database+"'");
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

	
	
	public List<String> getColumnName(String tableName, String dbName){
		List<String> columns=new ArrayList<>();
		
		PreparedStatement statement;
		try {
			statement = link.con.prepareStatement("SELECT distinct column_name FROM information_schema.columns WHERE table_name = '"+tableName+"' and TABLE_SCHEMA='"+dbName+"'");
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
				if(result.getString(1)!=null){
				values.add(result.getString(1));
				}
				//System.out.println(result.getString(1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
	
	public HashSet<FK> getFK(String tableName, String dbName) throws SQLException
	{
		String db_tab=dbName+"."+tableName;
		
		PreparedStatement statement=link.con.prepareStatement("show create table "+db_tab+";");
		ResultSet result=statement.executeQuery();
		String create_table = null;
		while(result.next()){
			create_table=result.getString(2);
		}
		
		HashSet<FK> fks=new HashSet<FK>();
		if(create_table.indexOf("FOREIGN KEY")<0)
			return null;
		else
		{
			String table=create_table.substring(create_table.indexOf("`")+1, create_table.indexOf("`", create_table.indexOf("`")+1));
			while(create_table.indexOf("FOREIGN KEY")>=0)
			{
				create_table=create_table.substring(create_table.indexOf("FOREIGN KEY"));
				String column=create_table.substring(create_table.indexOf("`")+1,create_table.indexOf("`", create_table.indexOf("`")+1));
				create_table=create_table.substring(create_table.indexOf("REFERENCES"));
				String rtable=create_table.substring(create_table.indexOf("`")+1,create_table.indexOf("`", create_table.indexOf("`")+1));
				create_table=create_table.substring(create_table.indexOf("("));
				String rcolumn=create_table.substring(create_table.indexOf("`")+1,create_table.indexOf("`", create_table.indexOf("`")+1));
				
				FK fk=new FK(table,column,rtable,rcolumn);
				fks.add(fk);
			}
			return fks;
		}
	}	
}


