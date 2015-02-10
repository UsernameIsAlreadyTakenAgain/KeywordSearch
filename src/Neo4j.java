import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.tooling.GlobalGraphOperations;




public class Neo4j {
	
	 //String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/test";
	//GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
	//Transaction transction=graphDataService.beginTx();;
	 ExecutionEngine engine;	
	 UniqueFactory<Node> factory;
	 Index<Node> indexa;
	//list of relationship
	public static enum RelTypes implements RelationshipType{
		//KNOW;
		BELONG_TO;
		
	}

	public Neo4j(){
		//GraphDatabaseService
		//graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		
				
		//Begin Transaction
		//transction=graphDataService.beginTx();	
	}
	
	/*public Node createNode(String name,String type,String label){
		
		String Key = "value"; 
		
		try(Transaction tx = graphDataService.beginTx())
		{
		UniqueFactory<Node> factory=new UniqueFactory.UniqueNodeFactory(graphDataService,name) {
			
			@Override
			protected void initialize(Node created, Map<String, Object> properties) {
				created.setProperty("value", name);
				created.setProperty("type", type);	
				Label myLabel = DynamicLabel.label(label);
				created.addLabel(myLabel);
			}
			  //node=factory.getOrCreate( "value", name );
			  
		};
		transction.success();
	    return factory.getOrCreate( "value", name );
		}
		//return factory;
	}	*/
	
		
	public Relationship createRel(Node first, Node second, String relType,GraphDatabaseService graphDataService){
		  Relationship relation = null;
		  try(Transaction transaction = graphDataService.beginTx()){
			  
			  ReadableIndex<Node> autoNodeIndex = graphDataService.index().getNodeAutoIndexer().getAutoIndex();
			  //Node n = autoNodeIndex.get("name", "Neo").getSingle();
			  //Node a = autoNodeIndex.get("name", "The Architect").getSingle();
			  transaction.acquireWriteLock(first);
			  transaction.acquireWriteLock(second);
			  Boolean created = false;
			  for(Relationship r : first.getRelationships(RelTypes.BELONG_TO)) {
			    if(r.getOtherNode(first).equals(second)) { // put other conditions here, if needed
			     
			      created = true;
			      break;
			    }
			  }
			  if(!created) {
			    relation=first.createRelationshipTo(second,RelTypes.BELONG_TO );
			    relation.setProperty("RelationType", relType);
			  }
			  transaction.success();
			  return relation;
		  }
	  }
	   

	
	/*public void createNode(String name,String type, String label){
		//Node result = null;
		//Iterator<Node> resultIterator = null;
		ExecutionResult result;
		
		try 
		{
			graphDataService.schema()
		            .constraintFor( DynamicLabel.label( label ) )
		            .assertPropertyIsUnique( name )
		            .create();
		   
			String queryString = "MERGE (n:'"+label+"' {value: {'"+name+"'},type:{'"+type+"'}}) RETURN n";
		    Map<String, Object> parameters = new HashMap<>();
		    parameters.put( "value", name );
		    result = engine.execute( queryString, parameters );
		    
		    //result = resultIterator.next();
		}catch(Exception e){
			
		}
  
		//return result;
	 }*/
	
	/*public Node createNode( String name, String type,String label)
    {
        // START SNIPPET: getOrCreateWithCypher
        Node result = null;
        ResourceIterator<Node> resultIterator = null;
        try ( Transaction tx = graphDataService.beginTx() )
        {
            String queryString = "MERGE (n: "+label+" {value:{"+name+"}, type:{"+type+"}}) RETURN n";
            System.out.println(queryString);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put( "value", name );
            parameters.put( "type", type );
            resultIterator = graphDataService.execute( queryString ).columnAs( "n" );
            //System.out.println("HTTTT");
            result = resultIterator.next();
            tx.success();
            return result;
        }
        // END SNIPPET: getOrCreateWithCypher
        finally
        {
            if ( resultIterator != null )
            {
                if ( resultIterator.hasNext() )
                {
                    Node other = resultIterator.next();
                    //noinspection ThrowFromFinallyBlock
                    throw new IllegalStateException( "Merge returned more than one node: " + result + " and " + other );
                }
            }
        }
    }*/
	
	 public Node createUniqueFactory( String name,String type, String label,GraphDatabaseService graphDataService )
	    {
	        // START SNIPPET: prepareUniqueFactory
		
	        try( Transaction tx = graphDataService.beginTx())
	        {
	        	//indexa=graphDataService.index().forNodes(label);
	            UniqueFactory<Node> result = new UniqueFactory.UniqueNodeFactory( graphDataService, label )
	            //UniqueFactory.UniqueNodeFactory result = new UniqueFactory.UniqueNodeFactory( indexa )
	            {
	                @Override
	                protected void initialize( Node created, Map<String, Object> properties )
	                {
	                    created.addLabel( DynamicLabel.label( label ) );
	                    created.setProperty( "value", properties.get("value") );
	                    created.setProperty( "type", type );
	                    
	                }
	            };
	            tx.success();
	            Node node;
	            node=result.getOrCreate("value", name);
	            
	            
	           // node=result.getOrCreate("type", type);
	            return node;
	        }
	        // END SNIPPET: prepareUniqueFactory
	    }

	    public Node get( String name,  UniqueFactory<Node> factory,GraphDatabaseService graphDataService )
	    {
	        // START SNIPPET: getOrCreateWithFactory
	        try ( Transaction tx = graphDataService.beginTx() )
	        {
	            Node node = factory.getOrCreate( "value", name );
	            //node=factory.getOrCreate("type", type);
	            
	            tx.success();
	            return node;
	        }
	        // END SNIPPET: getOrCreateWithFactory
	    }
	    
	    //get all the nodes
	    public List<Node> getAllNodes( GraphDatabaseService graphDb )
	    {
	        ArrayList<Node> nodes = new ArrayList<>();
	        try (Transaction tx = graphDb.beginTx())
	        {
	            for ( Node node : GlobalGraphOperations.at( graphDb ).getAllNodes() )
	            {
	                nodes.add( node );
	            }
	            tx.success();
	        }
	        return nodes;
	    }
	    
	    //get all the relationships
	    public List<Relationship> getAllRelationships( GraphDatabaseService graphDb )
	    {
	        List<Relationship> rels = new ArrayList<>();
	        try (Transaction tx = graphDb.beginTx())
	        {
	            for ( Relationship rel : GlobalGraphOperations.at( graphDb ).getAllRelationships() )
	            {
	                rels.add( rel );
	            }
	            tx.success();
	        }
	        return rels;
	    }
	    
	    void shutDown(GraphDatabaseService graphDb){
			//shut down graphDataService
			graphDb.shutdown();
			System.out.println("Neo4j database is shutown");
			
		}
	    
	    
	    
	   

}
