import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.kernel.TopLevelTransaction;
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
	
	public static enum link implements RelationshipType{
		//columns link together
		LINK;
	}
	
	public static enum path implements RelationshipType{
		//tables link together
		PATH;
	}

	public Neo4j(){
		//GraphDatabaseService
		//graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		
				
		//Begin Transaction
		//transction=graphDataService.beginTx();	
	}
	
		
	public Relationship createRel(Node first, Node second, String relType,GraphDatabaseService graphDataService){
		  Relationship relation = null;
		  try(Transaction transaction =  graphDataService.beginTx()){
			  
			  ReadableIndex<Node> autoNodeIndex = graphDataService.index().getNodeAutoIndexer().getAutoIndex();
			  //Node n = autoNodeIndex.get("name", "Neo").getSingle();
			  //Node a = autoNodeIndex.get("name", "The Architect").getSingle();
			  
			  transaction.acquireWriteLock(first);
			  transaction.acquireWriteLock(second);
			  
			  Boolean created = false;
			  
			  //judge relationship type
			  RelationshipType relationship;
			  Label label = DynamicLabel.label( "Column" );
			  if(first.hasLabel(label) && second.hasLabel(label)){
				  relationship=link.LINK;
			  }else{
				  relationship=RelTypes.BELONG_TO;
			  }
			  
			  for(Relationship r : first.getRelationships(relationship)) {
			    if(r.getOtherNode(first).equals(second)) { // put other conditions here, if needed
			      created = true;
			      break;
			    }
			  }
			  if(!created) {
			    relation=first.createRelationshipTo(second,relationship );
			    relation.setProperty("RelationType", relType);
			  }
			  transaction.success();
			 
			  return relation;
		  }
	  }
	
	//relationship between tables
	public Relationship createRel(Node first, Node second, String relType,int cost,GraphDatabaseService graphDataService){
		  Relationship relation = null;
		  try(Transaction transaction =  graphDataService.beginTx()){
			  
			  ReadableIndex<Node> autoNodeIndex = graphDataService.index().getNodeAutoIndexer().getAutoIndex();
			  //Node n = autoNodeIndex.get("name", "Neo").getSingle();
			  //Node a = autoNodeIndex.get("name", "The Architect").getSingle();
			  
			  transaction.acquireWriteLock(first);
			  transaction.acquireWriteLock(second);
			  
			  Boolean created = false;
			  
			  
			  RelationshipType relationship=path.PATH;
		 
			  for(Relationship r : first.getRelationships(relationship)) {
			    if(r.getOtherNode(first).equals(second)) { // put other conditions here, if needed
			      created = true;
			      break;
			    }
			  }
			  if(!created) {
			    relation=first.createRelationshipTo(second,relationship );
			    relation.setProperty("RelationType", relType);
			    relation.setProperty("cost", cost);
			  }
			  transaction.success();
			 
			  return relation;
		  }
	  }
	   
     //create unique node
	 public Node createUniqueFactory( String name,String type, String label,GraphDatabaseService graphDataService )
	    {
	        // START SNIPPET: prepareUniqueFactory
	        try( Transaction tx =  graphDataService.beginTx())
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
	 
	 //Create Normal node
	 public Node createNode(String name, String type, String label,String parent,GraphDatabaseService graphDataService){
		 try( Transaction tx =  graphDataService.beginTx())
	        {
	        	Node node=graphDataService.createNode();
	        	node.addLabel(DynamicLabel.label( label ));
	        	node.setProperty("value", name);
	        	node.setProperty("type", type);
	        	node.setProperty("parent", parent);
	        	
	        	tx.success();
	            return node;
	        }
	        // END SNIPPET: prepareUniqueFactory
	 }
	 
	 public Map<Set<Node>, Map<Relationship, Integer>>  FindShortestPath(Node start,Node end,List<Relationship> X,GraphDatabaseService graphDataService)
	 {	
		Map<Path, Integer> unsorted=new HashMap<>();
		ValueComparatorPath bvc =  new ValueComparatorPath(unsorted);
		TreeMap<Path, Integer> sorted=new TreeMap<>(bvc);
		
		 try ( Transaction tx =  graphDataService.beginTx() )
		{
		
	
		//System.out.println(start.getProperty("value"));
		//System.out.println(end.getProperty("value"));
			
		
	    //WeightedPath path = finder.findSinglePath( start, end );
	    Map<Relationship, Integer> map=new HashMap<>();
	    Set<Node> nodes=new HashSet<>();
	    Map<Set<Node>, Map<Relationship, Integer>> t=new HashMap<>();
	    
	    if(!X.isEmpty()){
	    	int maxDepth=20;
	    	int maxHitCount=5;
	    	//PathFinder<Path> finder=GraphAlgoFactory.shortestPath(PathExpanders.forType( path.PATH ), maxDepth, maxHitCount);
	    	PathFinder<Path> finder=GraphAlgoFactory.allSimplePaths(PathExpanders.forType( path.PATH ), maxDepth);
	        Iterator path=finder.findAllPaths(start, end).iterator();
	   
	        
	   int k=0;
	    while(path.hasNext()){
	    	Path p=(Path) path.next();
	    	
	    	int cost = 0;
	    	for(Relationship rel:p.relationships()){
	    		cost+=(int) rel.getProperty("cost");
	    	}
	    	//int cost=(int)p.weight();
	    	unsorted.put(p, cost);	
	    	k++;
	    }
	    //System.out.println("K:"+k);
	    sorted.putAll(unsorted);
	    //System.out.println(sorted.size());
	    
	    Iterator it=sorted.entrySet().iterator();
	    int a=0;
	    while(it.hasNext() && a<6){
	    	a++;
	    	Map.Entry pair=(Entry) it.next();
	    	Path ride=(Path) pair.getKey();
	    	int w=(int) pair.getValue();
	    	//System.out.println(ride);
	    	double tcost=0;
	    	for(Relationship rel:ride.relationships()){
	    		//System.out.println(rel);
	    		//System.out.println(X);
	    		if(X.contains(rel)){
	    			break;
	    		}
	    	    
	    		//String val=(String) rel.getProperty("RelationType");
				int cost=(int) rel.getProperty("cost");
				tcost+=cost;
				map.put(rel, cost);
	    	}
	    	//System.out.println(tcost);
	    	//System.out.println(ride.weight());
	    	if(tcost==w){
	    		//System.out.println("------");
	    		for(Node node:ride.nodes()){
	    			nodes.add(node);
	    		}
	    		t.put(nodes, map);
	    		 
	    		break;
	    	}	
	    	//System.out.println(a);
	    }
	    }else{
	    	PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(
				    PathExpanders.forType( path.PATH ), "cost" );
		    WeightedPath path = finder.findSinglePath( start, end );
		    int weight=(int) path.weight();
			//path.iterator();
			for(Relationship rel:path.relationships()){
				//String val=(String) rel.getProperty("RelationType");
				int cost=(int) rel.getProperty("cost");
				map.put(rel, cost);
				//System.out.println(val);
			}
			
			for(Node node:path.nodes()){
				nodes.add(node);
			}
			
			t.put(nodes, map);
	    }
	    
		tx.success();
		tx.close();
		System.out.println(t+"--------shortestPath");
		return t;
		
				
	   }
	 }
	 
	 
	 

	    public Node get( String name,  UniqueFactory<Node> factory,GraphDatabaseService graphDataService )
	    {
	        // START SNIPPET: getOrCreateWithFactory
	        try ( Transaction tx =  graphDataService.beginTx() )
	        {
	            Node node = factory.getOrCreate( "value", name );
	            //node=factory.getOrCreate("type", type);
	            
	            tx.success();
	            return node;
	        }
	        // END SNIPPET: getOrCreateWithFactory
	    }
	    
	    public Node getNode(String name,Label label,String parent,GraphDatabaseService graphDataService ){
	    	try(Transaction tx =  graphDataService.beginTx()){
	    		for(Node node :GlobalGraphOperations.at( graphDataService).getAllNodesWithLabel(label) )
	    		{
	    			if(node.hasProperty(name) && node.hasProperty(parent))
	    			{
	    				System.out.println(node);
	    				return node;
	    			}
	    		}
	    		tx.success();
	    		tx.close();
	    	}
	    	return null;
	    }
	    
	    //get all the nodes
	    public List<Node> getAllNodes( GraphDatabaseService graphDb )
	    {
	        ArrayList<Node> nodes = new ArrayList<>();
	        try (Transaction tx =  graphDb.beginTx())
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
	        try (Transaction tx =  graphDb.beginTx())
	        {
	            for ( Relationship rel : GlobalGraphOperations.at( graphDb ).getAllRelationships() )
	            {
	                rels.add( rel );
	            }
	            tx.success();
	        }
	        return rels;
	    }
	    
	    //shut down neo4j
	    void shutDown(GraphDatabaseService graphDb){
			//shut down graphDataService
			graphDb.shutdown();
			System.out.println("Neo4j database is shutown");
			
		}
	    
	    
	    
	   

}
