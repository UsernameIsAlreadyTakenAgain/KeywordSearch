import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


public class tryyyy {

	public static void main(String[] args) {

		String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/test";
		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		
		ExecutionEngine engine = new ExecutionEngine(graphDataService);
		ExecutionResult result;
		//List<ResultModel> resultSet;
		Neo4j neo=new Neo4j();
		String n="Pitt";
		
		 try(Transaction transction=graphDataService.beginTx()){
			 
			 result = engine
						.execute("MATCH p=(a)-[:`BELONG_TO`*..]->(b) Where a.value=~\"(?i).*"+ n + ".*\" and b.type='database' RETURN p");
			 
			 Iterator<Node> kindsOfFruit = result.columnAs("p");
			 System.out.println(result.dumpToString());
			/* while (kindsOfFruit.hasNext()) {
				 Node kindOfFruit = kindsOfFruit.next();
				 System.out.println(kindsOfFruit.toString());
			 
			 }
			 */
			   
			
			   
		 }
		 neo.shutDown(graphDataService);	
	}

	}
