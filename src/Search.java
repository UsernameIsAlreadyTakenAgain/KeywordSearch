import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;


public class Search {

	public static void main(String[] args) {
		
		String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/all";
		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		
		ExecutionEngine engine = new ExecutionEngine(graphDataService);
		ExecutionResult result;
		//List<ResultModel> resultSet;
		Neo4j neo=new Neo4j();
		
		
		//Label label = DynamicLabel.label( "Table" );
		
		//List<Node> nodes=neo.getAllNodes(graphDataService);
		//System.out.println(nodes.size());
		
		//show all the node
		/*System.out.println("-----------Show ALL The Nodes----------");
		int a=0;
		while(a<nodes.size()){
			//System.out.println(nodes.get(a).getProperty("value").toString());
			//System.out.println(Node.get(a).getProperty("type").toString());
			//System.out.println(Node.get(a).getLabels().toString());
			a++;
		}*/
		
		
		
	       
	       //search according to value
		/*System.out.println("----------Search by Value-----------");
		   int m=0;
	       while(m<nodes.size()){
	    	   if(nodes.get(m).getProperty("type")=="table"){
	    	   //System.out.println(nodes.get(m).getProperty("value"));	   
	    		   }
	    	   m++;
	    	   
	       }*/
	   
	   //get the nodes with the same label
	      /* System.out.println("----------Show All the Nodes with the Same Label-----------");
	   for(Node reeve : GlobalGraphOperations.at( graphDataService ).getAllNodesWithLabel(label)){
	    	// System.out.println(reeve.getProperty("value"));
	     }*/
	   
	 //according to the value in a node, find the nodes it relates to.
		
	   //System.out.println("----------Show its related nodes-----------");
	   //int e=0;
	   String val="pitt";
	   
	   try(Transaction transction=graphDataService.beginTx()){
		  
		   result = engine
					.execute("MATCH (a) Where a.value=~\"(?i).*"+val+".*\" RETURN  a");
		  
		   
		   for(Map<String,Object> node : result){
			   
			   Node a=(Node) node.get("a");
			   //String value=(String) a.getProperty("value");
			   String type=(String) a.getProperty("type");
			   String value=""
			  
			   
			   if(type.equals("record")){
				   //System.out.print("This is a test!");
				   ExecutionResult res;
				   Map<String, Object> params = new HashMap<String, Object>();
				   params.put( "val", value);
				   
				   //res=engine.execute("MATCH (a)-[:`BELONG_TO`]->(b)-[:`BELONG_TO`]->(c)-[:`BELONG_TO`]->(d) where a.value={value} and a.type='record' RETURN b,c,d");
				  String que= "MATCH (a:Record{type:'record',value:{val}})-[:`BELONG_TO`]->(b)-[:`BELONG_TO`]->(c)-[:`BELONG_TO`]->(d)  RETURN b,c,d";
				     res=engine.execute(que, params);
           
	               for(Map<String,Object> temp : res){
	            	   Node col=(Node) temp.get("b");
	            	   Node tab=(Node) temp.get("c");
	            	   Node db=(Node) temp.get("d");
	            	   //System.out.print(col);
	            	   String column=col.getProperty("value").toString();
	            	   String table=tab.getProperty("value").toString();
	            	   String database=db.getProperty("value").toString();
	            	   Search_sql check=new Search_sql();
	            	  if( check.knowRecord(database, table, column,value)){
	            		  System.out.println("value:"+value+"  "+"type:"+type);
	            		  System.out.println("value:"+column+"  "+"type:"+col.getProperty("type").toString());
	            		  System.out.println("value:"+table+"  "+"type:"+tab.getProperty("type").toString());
	            		  System.out.println("value:"+database+"  "+"type:"+db.getProperty("type").toString());
	            		  System.out.println("-----------------");
	            	  }
	            	   
	            	   
	               }
			   }
			   
			   if(type.equals("column")){
				   
				   Map<String, Object> params = new HashMap<String, Object>();
				   params.put( "val", value);
				  
				   
				   ExecutionResult res;
				   String que="MATCH (a:Column{type:'column',value:{val}})-[:`BELONG_TO`]->(b)-[:`BELONG_TO`]->(c) RETURN b,c";
				   //res=engine.execute("MATCH (a)-[:`BELONG_TO`]->(b)-[:`BELONG_TO`]->(c) where a.value='"+value+"' and a.type='column' RETURN b,c");
	               res=engine.execute(que, params);
				   for(Map<String,Object> temp : res){
	            	  
	            	   Node tab=(Node) temp.get("b");
	            	   Node db=(Node) temp.get("c");
	            	   
	            	   
	            	   String table=tab.getProperty("value").toString();
	            	   String database=db.getProperty("value").toString();
	            	   Search_sql check=new Search_sql();
	            	   
	            	  if( check.knowColumn(database, table, value)){
	            		 
	            		  System.out.println("value:"+value+"  "+"type:"+type);
	            		  System.out.println("value:"+table+"  "+"type:"+tab.getProperty("type").toString());
	            		  System.out.println("value:"+database+"  "+"type:"+db.getProperty("type").toString());
	            		  System.out.println("-----------------");
	            	  }
	            	     
	               }
				   
			   }
			   
			   if(type.equals("table")){
				   
				   Map<String, Object> params = new HashMap<String, Object>();
				   params.put( "val", value);
				   
				  
				   ExecutionResult res;
				   String que="MATCH (a:Table{type:'table',value:{val}})-[:`BELONG_TO`]->(b) RETURN b";
				   //res=engine.execute("MATCH (a)-[:`BELONG_TO`]->(b) where a.value='"+value+"' and a.type='table' RETURN b");
				   res=engine.execute(que, params);
	               for(Map<String,Object> temp : res){
	            	  
	            	  // Node tab=(Node) node.get("b");
	            	   Node db=(Node) temp.get("b");
	            	   
	            	   
	            	   
	            	   //String table=tab.getProperty("value").toString();
	            	   String database=db.getProperty("value").toString();
	            	   Search_sql check=new Search_sql();
	            	  
	         	 
	            		  //System.out.println("value:"+table+"  "+"type:"+tab.getProperty("type").toString());
	            	      System.out.println("value:"+value+"  "+"type:"+type);
	            		  System.out.println("value:"+database+"  "+"type:"+db.getProperty("type").toString());
	            		  System.out.println("-----------------");
	            	  
	            	     
	               }
				   
			   }
			   
			   if(type.equals("database")){
				   ExecutionResult res;
				   
	         	 
	            		  //System.out.println("value:"+table+"  "+"type:"+tab.getProperty("type").toString());
	            		  System.out.println("value:"+value+"  "+"type:"+type);
	            		  System.out.println("-----------------");
	            	  
	            	     
	               }
				   
			   }
			   
			   
		   }
	   neo.shutDown(graphDataService);	
		   
	   }
	
}	
	   
	   

