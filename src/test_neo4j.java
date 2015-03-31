import java.util.HashSet;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;



public class test_neo4j {

	public static void main(String[] args) {
		
		String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/yeah";
		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		Transaction transction= graphDataService.beginTx();
		
		String database="homework2";
		
		MySQL test=new MySQL(database);
		
		try(Transaction trans=graphDataService.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(graphDataService);
			ExecutionResult result;
	         for(Node reeve : currTables){
	    	// System.out.println(reeve.getProperty("value"));
		 
		   	HashSet<FK> fks=test.getFK(reeve.getProperty("value").toString(), database);
			if(fks!=null){
			for(FK fk:fks){
				String table=fk.getTable();
				String column=fk.getColumn();
				String rtable=fk.getRtable();
				String rcolumn=fk.getRcolumn();
				
				//Node pk_col=
				result = engine
						.execute("MATCH (n) where n.value='"+column+"' and n.parent='"+table+"' RETURN n");
				System.out.println(result);
				Node primatK = null;
				
				 for(Map<String,Object> temp : result){
					 primatK=(Node) temp.get("n");
					 //System.out.println(primatK);
				 }
				 result = engine
							.execute("MATCH (n) where n.value='"+rcolumn+"' and n.parent='"+rtable+"' RETURN n");
				 Node foreignK = null;
				
				 for(Map<String,Object> temp : result){
					 foreignK=(Node) temp.get("n");
					 }
				 neo.createRel(primatK, foreignK, "PK-FK", graphDataService); 
			}
			}  
	     }
	   
	   //make link among databases' columns
	   for (Node currCol:currCols){
		   String value=currCol.getProperty("value").toString();
		   result = engine
					.execute("MATCH (n:`Column`) where n.value='city' RETURN n");
		   for(Map<String,Object> temp :result){
			   Node col=(Node) temp.get("n");
			   String[] db=currCol.getProperty("parent").toString().split("-");
			   String[] rdb=col.getProperty("parent").toString().split("-");
			   if(!col.equals(currCol) && db[1]!=rdb[1]){
				   neo.createRel(currCol, col, "SameNameCol", graphDataService);   
			   }
		   }
	   }
	    trans.success();
		trans.close();
		}
		
		
		
	}

}
