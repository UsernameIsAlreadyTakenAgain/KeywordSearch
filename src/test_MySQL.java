import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.kernel.TopLevelTransaction;
import org.neo4j.tooling.GlobalGraphOperations;

public class test_MySQL {

	public static void main(String[] args) throws Exception {
	String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/Version3";
  

		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		Transaction transction= graphDataService.beginTx();
		//ExecutionEngine engine = new ExecutionEngine(graphDataService);
		//ExecutionResult result;
		
		
		//IndexManager index=graphDataService.index();
		//Index<Node> index=graphDataService.index().forNodes("value");
		//UniqueFactory<Node> factory = null;
		String database="homework2";
		
		MySQL test=new MySQL(database);
		
		

		List<String> tables=test.getTableName(database);
		//Map<String,List<String>> columns = new HashMap<String,List<String>>();
		//List<String> column=new ArrayList<String>();
		//List<String> value=new ArrayList<>();
		//List<String> values=new ArrayList<>();
		//List<Node> Node=new ArrayList<>();
		//List<Relationship> relation=new ArrayList<>();
		
		Neo4j neo=new Neo4j();
		
		//create node for database
		Node data=neo.createUniqueFactory(database, "database", "Database",graphDataService);
		List<Node> currTables=new ArrayList<>();
		List<Node> currCols=new ArrayList<>();
		
		for(int t=0;t<tables.size();t++){
			
			//table--database
			Node table=neo.createNode(tables.get(t), "table", "Table", database, graphDataService);
			currTables.add(table);
			neo.createRel(table, data, "value-record", graphDataService);
			
			//column--table
			List<String> columns=test.getColumnName(tables.get(t),database);
			for(String column:columns){
				String parent=tables.get(t)+"-"+database;
				Node col=neo.createNode(column, "column", "Column", parent, graphDataService);
				currCols.add(col);
				neo.createRel(col, table, "column-table", graphDataService);
				
				//record--column
				List<String> records=test.getValue(column, tables.get(t));
				for(String record:records){
					Node cell=neo.createUniqueFactory(record, "record", "Record", graphDataService);
					neo.createRel(cell, col, "record-column", graphDataService);
				}
			}
		}
		transction.success();
		transction.close();
		
		
		//Label label = DynamicLabel.label( "Table" );
		try(Transaction trans=graphDataService.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(graphDataService);
			ExecutionResult result;
			
			//link PK-FK and tables
	       for(Node reeve : currTables){
		   	HashSet<FK> fks=test.getFK(reeve.getProperty("value").toString(), database);
			if(fks!=null){
			for(FK fk:fks){
				String table=fk.getTable();
				String column=fk.getColumn();
				String rtable=fk.getRtable();
				String rcolumn=fk.getRcolumn();
				
				String parent=table+"-"+database;
				String rparent=rtable+"-"+database;
				result = engine
						.execute("MATCH (n) where n.value='"+column+"' and n.parent='"+parent+"' RETURN n");
				Node foreignK = null;
				 for(Map<String,Object> temp : result){
					 foreignK=(Node) temp.get("n");
				 }
				 result = engine
							.execute("MATCH (n) where n.value='"+rcolumn+"' and n.parent='"+rparent+"' RETURN n");
				 Node primaryK = null;
				 for(Map<String,Object> temp : result){
					 primaryK=(Node) temp.get("n");
					 }
				 neo.createRel(primaryK, foreignK, "PK-FK", graphDataService); 
				 
				 //link table
				 String rel=table+"."+column+"="+rtable+"."+rcolumn;
				 result = engine
							.execute("MATCH (n) where n.value='"+table+"' and n.parent='"+database+"' RETURN n");
					Node table1 = null;
					 for(Map<String,Object> temp : result){
						 table1=(Node) temp.get("n");
					 }
					 
					 result = engine
								.execute("MATCH (n) where n.value='"+rtable+"' and n.parent='"+database+"' RETURN n");
					 Node table2 = null;
					 for(Map<String,Object> temp : result){
						 table2=(Node) temp.get("n");
						 }
				 neo.createRel(table1, table2, rel, 1, graphDataService);
			}
			}  
	     }
	   
	   //make link among databases' columns and tables
	   for (Node currCol:currCols){
		   String value=currCol.getProperty("value").toString();
		   result = engine
					.execute("MATCH (n:`Column`) where n.value='"+value+"' RETURN n");
		   for(Map<String,Object> temp :result){
			   Node col=(Node) temp.get("n");
			   String[] db=currCol.getProperty("parent").toString().split("-");
			   String[] rdb=col.getProperty("parent").toString().split("-");
			   if(!col.equals(currCol) && !db[1].equals(rdb[1])){
				   neo.createRel(currCol, col, "SameNameCol", graphDataService); 
				   
				   result = engine
							.execute("MATCH (n) where n.value='"+db[0]+"' and n.parent='"+db[1]+"' RETURN n");
					Node table1 = null;
					 for(Map<String,Object> tem : result){
						 table1=(Node) tem.get("n");
					 }
					 
					 result = engine
								.execute("MATCH (n) where n.value='"+rdb[0]+"' and n.parent='"+rdb[1]+"' RETURN n");
					 Node table2 = null;
					 for(Map<String,Object> tem : result){
						 table2=(Node) tem.get("n");
						 }
			     String rel=db[0]+"."+value+"="+rdb[0]+"."+col.getProperty("value");
				 neo.createRel(table1, table2, rel, 2, graphDataService);
			   }
		   }
	   }
	    trans.success();
		trans.close();
		}
		
		//table---database
		/*int n=0;
		while(n<tables.size()){
			Node table=neo.createNode(tables.get(n), "table", "Table", database, graphDataService);
			//Node table=neo.createUniqueFactory(tables.get(n), "table", "Table",graphDataService);
			relation.add(neo.createRel(table, data, "value-record", graphDataService));
			n++;	
			
		}*/
		
		
		
		/*int i=0;
		while(i<tables.size()){
			columns.put(tables.get(i), test.getColumnName(tables.get(i)));
			i++;
			//System.out.println(columns);				
		}
		Label label = DynamicLabel.label( "Table" );
		
		for(String key:columns.keySet()){
			
		   for(String val:columns.get(key)){
			   
			   int k=0; 
			   
			   Node col=neo.createNode(val, "column", "Column", key, graphDataService);
			   //Node tab=neo.createNode(key, "table", "Table", database, graphDataService);
			   
			   Node tab=neo.getNode(key, label, database, graphDataService);
			   //Node col=neo.createUniqueFactory(val, "column","Column",graphDataService);
			  //Node tab=neo.createUniqueFactory(key, "table","Table",graphDataService);
			   relation.add(neo.createRel(col, tab, "column-table", graphDataService));
			   //System.out.println("-----------");
			   
			values=test.getValue(val, key);
			//System.out.println(values);
			
			while(k<values.size()){
				
				Node record=neo.createUniqueFactory(values.get(k), "record","Record",graphDataService);
				relation.add(neo.createRel(record, col, "value-record", graphDataService));
				k++;
				
			}*/

		
		
		neo.shutDown(graphDataService);
					
		   }
		
		
    
      
      }
       		
			

	
