import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;


public class Kruskal {
	
	
	
	public Map<List<Node>, List<Relationship>> executeKruskal(TreeMap<Relationship,Integer> sorted,Set<Node> nodes, GraphDatabaseService graphDataService){
		Map<List<Node>, List<Relationship>> graph=new HashMap<>();
		try(Transaction tx =  graphDataService.beginTx() ){
			
			ExecutionEngine engine = new ExecutionEngine(graphDataService);
			ExecutionResult result;
			List<Node> node=new ArrayList<>();
			List<Relationship> relation=new ArrayList<>();
			//Map<String,Integer> path=new HashMap<>();
			
					
			/*Iterator it = sorted.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();	
		        String val=(String) pair.getKey();
		        result=engine.execute("MATCH (a)-[r:`PATH`]->(b) where r.RelationType='"+val+"' RETURN a,b ");
		        for(Map<String,Object> temp : result){
					 nodes.add((Node) temp.get("a"));
					 nodes.add((Node) temp.get("b"));
				 }
		}*/
		    Iterator it = sorted.entrySet().iterator();
		    int n=0;
		    while(it.hasNext() && n<nodes.size()-1){
		    	Map.Entry pair = (Map.Entry)it.next();	
		    	Relationship rel=(Relationship) pair.getKey();
		    	Node[] temp=rel.getNodes();
		    	String val=(String) rel.getProperty("RelationType");
		        //String val=(String) pair.getKey();
		       // result=engine.execute("MATCH (a)-[r:`PATH`]->(b) where r.RelationType='"+val+"' RETURN a,b ");
		        
		        
					 
					 //tmp.add(node[0]);
					 //tmp.add(node[1]);
					 if(!isCircle(relation,rel)){
						 node.add(temp[0]);
					     node.add(temp[1]);
					     relation.add(rel);
					 }
				 
		    	n++;
		    }
		    graph.put(node, relation);
		    //System.out.println(node);
			tx.success();
			tx.close();
			return graph;
		}
			
	}
	
	public Boolean isCircle(List<Relationship> path, Relationship e){
		
		    List<Relationship> copy=new ArrayList<>();
			Node start=e.getStartNode();
			Node end=e.getEndNode();
			
			int len=path.size();
			int i=0;
			while(!copy.equals(path) && i<len){
				for(Relationship edge:path){
					Node[] nodes=edge.getNodes();
					if(nodes[0].equals(start) || nodes[1].equals(start)){
						if(nodes[1].equals(start)){
							 start=nodes[0];
						}else{
							 start=nodes[1];
						}
						copy.add(edge);
									}
					if(start.equals(end)){
						//System.out.println("This is a circle");
						return true;
					}
				}
				i++;
				}
				return false;
		}
}
