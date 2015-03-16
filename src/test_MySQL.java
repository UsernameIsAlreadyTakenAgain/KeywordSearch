import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class test_MySQL {

	public static void main(String[] args) {
	String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/NewVersion";
  //String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/data";

		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		Transaction transction= graphDataService.beginTx();
		
		
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
		
		for(int t=0;t<tables.size();t++){
			
			//table--database
			Node table=neo.createNode(tables.get(t), "table", "Table", database, graphDataService);
			neo.createRel(table, data, "value-record", graphDataService);
			
			//column--table
			List<String> columns=test.getColumnName(tables.get(t),database);
			for(String column:columns){
				Node col=neo.createNode(column, "column", "Column", tables.get(t), graphDataService);
				neo.createRel(col, table, "column-table", graphDataService);
				
				//record--column
				List<String> records=test.getValue(column, tables.get(t));
				for(String record:records){
					Node cell=neo.createUniqueFactory(record, "record", "Record", graphDataService);
					neo.createRel(cell, col, "record-column", graphDataService);
				}
			}
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

		transction.success();
		transction.close();
		
		neo.shutDown(graphDataService);
					
		   }
		
		
    
      
      }
       		
			

	
