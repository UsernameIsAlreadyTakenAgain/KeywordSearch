

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


public class Connection_to_neo4j {
	
	static String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/test2";
	Node first;
	Node second;
	Relationship relation;
	GraphDatabaseService graphDataService;
	
	//list of relationship
	private static enum RelTypes implements RelationshipType{
		KNOW;
	}

	void createDatabase(){
		
		//GraphDatabaseService
		graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		
		//Begin Transaction
		
		
		try(Transaction transction=graphDataService.beginTx();)
		{
			//Create nodes & Set properties 
			first=graphDataService.createNode();
			first.setProperty("name", "Jie");
			first.setProperty("type", "node");
			
			second=graphDataService.createNode();
			second.setProperty("name", "test");
			
			//Relationship
			relation=first.createRelationshipTo(second, RelTypes.KNOW);
			relation.setProperty("relationship-type", "knows");
			
			System.out.println(first.getProperty("name").toString());
			System.out.println(first.getProperty("type").toString());
			System.out.println(relation.getProperty("relationship-type").toString());
			System.out.println(second.getProperty("name").toString());
			
			//Success transaction
			transction.success();
			
			
		}
		finally{
			//Finish the transaction
			//transction.finish();
		}
		
		
	}
	
	void removeData(){
		//Begin Transaction
	    Transaction transction=graphDataService.beginTx();
	    
	    try{
	    	//delete the relationship
	    	first.getSingleRelationship(RelTypes.KNOW, Direction.OUTGOING).delete();
	    	System.out.println("Nodes are removed");
	    	
	    	//delete the node
	    	first.delete();
	    	second.delete();
	    	
	    	//Success transaction
			transction.success();	
	    }
		finally{
			//finish the transaction
			transction.finish();
		}
	}
	
	void shutDown(){
		//shut down graphDataService
		graphDataService.shutdown();
		System.out.println("Neo4j database is shutown");
		
	}
	
	

}
