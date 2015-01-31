import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;






public class test_MySQL {

	public static void main(String[] args) {
	//String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/Jie";
  String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/data";

		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		Transaction transction=graphDataService.beginTx();
		
		
		//IndexManager index=graphDataService.index();
		//Index<Node> index=graphDataService.index().forNodes("value");
		UniqueFactory<Node> factory = null;
		
		MySQL test=new MySQL();
		
		List<String> tables=test.getTableName();
		Map<String,List<String>> columns = new HashMap<String,List<String>>();
		List<String> column=new ArrayList<String>();
		List<String> value=new ArrayList<>();
		List<String> values=new ArrayList<>();
		List<Node> Node=new ArrayList<>();
		List<Relationship> relation=new ArrayList<>();
		
		Neo4j neo=new Neo4j();
		int n=0;
		while(n<tables.size()){
			//neo.createNode(tables.get(n), "table","Table");
			//Unique.getOrCreateWithOutcome(Neo4j_Path, neo.createUniqueFactory( "table", "Table",graphDataService));
			//factory=neo.createUniqueFactory(tables.get(n), "table", "Table",graphDataService);
			
			//Node.add(neo.get(tables.get(n),  factory,graphDataService));
			Node.add(neo.createUniqueFactory(tables.get(n), "table", "Table",graphDataService));
			//Node.add(neo.createNode(tables.get(n), "table","Table"));
			//System.out.println(Node.get(n).getProperty("value").toString());
			//System.out.println(Node.get(n).getProperty("type").toString());
			n++;
			
			
		}
		
		
		
		int i=0;
		while(i<tables.size()){
			columns.put(tables.get(i), test.getColumnName(tables.get(i)));
			i++;
			//System.out.println(columns);				
		}
		
		for(String key:columns.keySet()){
			
		   for(String val:columns.get(key)){
			   //neo.createNode(val, "column","Column");
			   int k=0; 
			//Node first=neo.createNode(val, "column","Column");
			//Node second=Node.
			//Node.add(first);
			   Node.add(neo.createUniqueFactory(val, "column", "Column",graphDataService));
			   Node col=neo.createUniqueFactory(val, "column","Column",graphDataService);
			   Node tab=neo.createUniqueFactory(key, "table","Table",graphDataService);
			   relation.add(neo.createRel(col, tab, "column-table", graphDataService));
			   
			   
			   //column.add(val);
			     //factory=neo.createUniqueFactory( "column", "Column",graphDataService);
			     //Node.add(neo.get(val, factory,graphDataService)); 
			//Node.add(neo.createNode(val, "column","Column"));
			//relation.add(neo.createRel(first, second, relType))
			
			values=test.getValue(val, key);
			//System.out.println(value);
			
			
			
			
			while(k<values.size()){
				//value.add(values.get(k));
				    //factory=neo.createUniqueFactory( "record", "Record",graphDataService);
				    //Node.add(neo.get(values.get(k), factory,graphDataService)); 
				//Node.add(neo.createNode(values.get(k), "value","Value"));
				Node.add(neo.createUniqueFactory(values.get(k).toString(),"record", "Record",graphDataService));
				
				Node record=neo.createUniqueFactory(values.get(k), "record","Record",graphDataService);
				relation.add(neo.createRel(record, col, "value-record", graphDataService));
				k++;
				//System.out.println(Node.get(k).getProperty("value").toString());
			}
			
			
			
			//System.out.println(Node.get(n).getProperty("name").toString());
			//System.out.println(Node.get(n).getProperty("type").toString());
		   }
		}	
		
		/*int c=0,v=0;
		while(c<column.size()){
			//factory=neo.createUniqueFactory(column.get(c).toString(), "column", "Column",graphDataService);
		     //Node.add(neo.get(column.get(c).toString(), factory,graphDataService)); 
			Node.add(neo.createUniqueFactory(column.get(c).toString(), "column", "Column",graphDataService));
		     c++;	
		}
		while(v<value.size()){
			//factory=neo.createUniqueFactory( value.get(v).toString(),"record", "Record",graphDataService);
		    // Node.add(neo.get(value.get(v).toString(), factory,graphDataService)); 
			Node.add(neo.createUniqueFactory(value.get(v).toString(),"record", "Record",graphDataService));
		     v++;	
		}*/
		
		List<Node> nodes=neo.getAllNodes(graphDataService);
		int a=0;
		/*while(a<nodes.size()){
			
			System.out.println(nodes.get(a).getProperty("value").toString());
			
			//System.out.println(Node.get(a).getProperty("type").toString());
			//System.out.println(Node.get(a).getLabels().toString());
			a++;
		}*/
		//System.out.println(nodes.size());
		
		//according to the value in a node, find the end node.
		int e=0;
		while(e<nodes.size()){
			if(nodes.get(e).getProperty("value").toString().equals("city")){
				//System.out.println(nodes.get(e).getProperty("value"));
				for(Relationship rel:nodes.get(e).getRelationships()){
					//rel.getEndNode();
					System.out.print(rel.getProperty("RelationType")+"  ");
					System.out.println(rel.getEndNode().getProperty("value"));
				}
				break;
			}
			e++;
		}
		
		
		//find the shortest path between two nodes
		/*PathFinder<Path> finder = GraphAlgoFactory.shortestPath(
			    PathExpanders.forType(RelTypes.BELONG_TO);
			Iterable<Path> paths = finder.findAllPaths( nodes.get(0), nodes.get(100) );*/
		
		
       
       
      
      }
       		
			
}
	
