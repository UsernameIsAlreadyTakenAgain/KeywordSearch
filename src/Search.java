import java.util.List;

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
		
		String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/Jie";
		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		Transaction transction=graphDataService.beginTx();
		Neo4j neo=new Neo4j();
		
		Label label = DynamicLabel.label( "Table" );
		
		List<Node> nodes=neo.getAllNodes(graphDataService);
		//System.out.println(nodes.size());
		
		//show all the node
		System.out.println("-----------Show ALL The Nodes----------");
		int a=0;
		while(a<nodes.size()){
			//System.out.println(nodes.get(a).getProperty("value").toString());
			//System.out.println(Node.get(a).getProperty("type").toString());
			//System.out.println(Node.get(a).getLabels().toString());
			a++;
		}
		
		
		
	       
	       //search according to value
		System.out.println("----------Search by Value-----------");
		   int m=0;
	       while(m<nodes.size()){
	    	   if(nodes.get(m).getProperty("type")=="table"){
	    	   //System.out.println(nodes.get(m).getProperty("value"));	   
	    		   }
	    	   m++;
	    	   
	       }
	   
	   //get the nodes with the same label
	       System.out.println("----------Show All the Nodes with the Same Label-----------");
	   for(Node reeve : GlobalGraphOperations.at( graphDataService ).getAllNodesWithLabel(label)){
	    	// System.out.println(reeve.getProperty("value"));
	     }
	   
	 //according to the value in a node, find the nodes it relates to.
	   System.out.println("----------Show its related nodes-----------");
	   int e=0;
	   String val="Pittsburgh";
		while(e<nodes.size()){
			if(nodes.get(e).getProperty("value").toString().equals(val)){
				//System.out.println(nodes.get(e).getProperty("value"));
				for(Relationship rel:nodes.get(e).getRelationships()){
					//rel.getEndNode();
					//System.out.print(rel.getProperty("RelationType")+"  ");
					//System.out.println(rel.getStartNode().getProperty("value"));
					// System.out.println(rel.getEndNode().getProperty("value"));
					System.out.println(rel.getStartNode().getProperty("value")+"  "+rel.getProperty("RelationType")+"  "+val);
					System.out.println(val+"  "+rel.getProperty("RelationType")+"  "+rel.getEndNode().getProperty("value"));
				}
				break;
			}
			e++;
		}
	   
	   neo.shutDown(graphDataService);
		
		
	}

}
