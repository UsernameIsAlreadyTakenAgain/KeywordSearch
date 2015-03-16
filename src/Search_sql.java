import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Node;


public class Search_sql {
	
	    //search record
		public Boolean knowRecord(String database,String table, String column,String record){
			
			Connection_to_MySQL link=new Connection_to_MySQL();
			link.toMySQL(database);
			
			
			try{
				
				String stat="select "+column+" from "+table+" where "+column+"=?";
				PreparedStatement statement=link.con.prepareStatement(stat);
				//PreparedStatement statement=link.con.prepareStatement("select "+column+" from "+table+" where "+column+"='"+record+"'");
			    statement.setString(1, record);
				ResultSet result=statement.executeQuery();
			    
			    if(result.next()){
			    	
			    	return true;
			    }
			    
			    
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return false;
		}
		
		 //search column
		public Boolean knowColumn(String database,String table, String column){
			
			Connection_to_MySQL link=new Connection_to_MySQL();
			link.toMySQL(database);
		
			
			try{
				
				//PreparedStatement statement=link.con.prepareStatement("SELECT column_name FROM information_schema.columns WHERE table_name = '"+table+"'");
			    
				PreparedStatement statement=link.con.prepareStatement("SELECT * FROM "+table+"");
				ResultSet result=statement.executeQuery();
			    
			    int i=result.findColumn(column);
			    //System.out.print(table);
			    return true; 
			    
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}
		
		 
}
