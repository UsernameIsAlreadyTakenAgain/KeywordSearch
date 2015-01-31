import java.util.List;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;


public class Search {

	public static void main(String[] args) {
		
		String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/Jie";
		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		Transaction transction=graphDataService.beginTx();
		Neo4j neo=new Neo4j();
		
		Label label = DynamicLabel.label( "Table" );
		
		List<Node> nodes=neo.getAllNodes(graphDataService);
	       int m=0;
	       
	       //search according to value
	       while(m<nodes.size()){
	    	   if(nodes.get(m).getProperty("type")=="table"){
	    	   System.out.println(nodes.get(m).getProperty("value"));	   
	    		   }
	    	   m++;
	    	   
	       }
	   
	   //get the nodes with the same label
	   for(Node reeve : GlobalGraphOperations.at( graphDataService ).getAllNodesWithLabel(label)){
	    	 System.out.println(reeve.getProperty("value"));
	     }
	   
	   
		
		
	}

}
