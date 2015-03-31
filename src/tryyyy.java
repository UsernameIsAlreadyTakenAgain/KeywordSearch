import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.RelationshipExpander;
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
			 
			 //result = engine
						.execute("MATCH p=(a)-[:`BELONG_TO`*..]->(b) Where a.value=~\"(?i).*"+ n + ".*\" and b.type='database' RETURN p");
			 
			 Iterator<Node> kindsOfFruit = result.columnAs("p");
			 System.out.println(result.dumpToString());
			path r=Neo4j.path.PATH;
			PathExpander p=Neo4j.path.PATH;

			 PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(Neo4j.path.PATH, "cost");

					   
		Iterator path=finder.findAllPaths(start, end).iterator();
	
	    //WeightedPath path = finder.findSinglePath( start, end );
	    Map<String, Integer> map=new HashMap<>();
	    Set<Node> nodes=new HashSet<>();
	    Map<Set<Node>, Map<String, Integer>> t=new HashMap<>();
	    while(path.hasNext()){
	    	 Map.Entry pair = (Map.Entry)path.next();
	    	 pair.getKey();
	    	 pair.getValue();
	    	 //System.out.print(b);
	    }
			
			   
		 }
		 neo.shutDown(graphDataService);	
	}

	}
