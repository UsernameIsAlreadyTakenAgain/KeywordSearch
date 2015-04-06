import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

public class join {
	public static void main(String args[]){

		String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/IHopeThisIsFinal";
		GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		Transaction tx=graphDataService.beginTx();
		
		
		ExecutionEngine engine = new ExecutionEngine(graphDataService);
		ExecutionResult result;
		
		Neo4j neo=new Neo4j();
		
		Map<List<String>, Integer> unsorted=new HashMap<>();
		ValueComparatorList bvc =  new ValueComparatorList(unsorted);
		TreeMap<List<String>,Integer> T=new TreeMap<>(bvc);
		
		//String[] api={"department.test1","store.test1","city.homework2","c_home.test1"};
		//String[] api={"department.test1","employee.test1"};
		String[] api={"test1.city_state","homework2.city","test1.region","test1.address","test1.store"};
		Set<String> apis=new HashSet<>();  //make sure the unique tables
		for(int i=0;i<api.length;i++){
			apis.add(api[i]);
		}
		List<Node> all=new ArrayList<>();
		System.out.println(apis.size()+"------length of apis");
		//find all the target nodes
		for(String tab:apis){
			String[] temp=tab.split("\\.");
			//System.out.println(temp+"------table");
			result = engine
					.execute("MATCH (n) where n.value='"+temp[1]+"' and n.parent='"+temp[0]+"' RETURN n");
			Node node = null;
			 for(Map<String,Object> tem : result){
				 node=(Node) tem.get("n");
				 all.add(node);
				 System.out.println(node+"------node");
			 }
		}
		
		System.out.println(all+"------all");
		int k=7;
		SteinerTree st=new SteinerTree();
		Map<Map<List<Node>, List<Relationship>>,Integer> unsort=st.KBestSterinerTree(all,k, graphDataService);
		//ValueComparatorResult vcr =  new ValueComparatorResult(unsort);
		//TreeMap<Map<List<Node>, List<Relationship>>,Integer> sorted=new TreeMap<>(vcr);
		//System.out.println(sorted);
		List<Map<Map<List<Node>, List<Relationship>>,Integer>> sort=new ArrayList<>();
		
		
		
		while(!unsort.isEmpty()){
			int curr=Integer.MAX_VALUE;
			Map<List<Node>, List<Relationship>> currg=new HashMap<>();
			Map<Map<List<Node>, List<Relationship>>,Integer> tmp=new HashMap<>();
			Iterator i=unsort.entrySet().iterator();
			while(i.hasNext() ){
				Map.Entry pair=(Entry) i.next();
				Map<List<Node>, List<Relationship>> g=(Map<List<Node>, List<Relationship>>) pair.getKey();
				int cost=(int) pair.getValue();
				if(curr>cost){
					curr=cost;
					currg=g;
				}	
			}
			//System.out.println(curr+"------shortest");
			tmp.put(currg, curr);
			unsort.remove(currg);
			sort.add(tmp);
		}
		
		//System.out.println(sort);
		
		int rank=1;
		for(Map<Map<List<Node>, List<Relationship>>,Integer> list:sort){
			Iterator it=list.entrySet().iterator();
			
			while(it.hasNext()){
				Map.Entry pair=(Entry) it.next();
				Map<List<Node>, List<Relationship>> g=(Map<List<Node>, List<Relationship>>) pair.getKey();
				int cost=(int) pair.getValue();
				Iterator ite=g.entrySet().iterator();
				while(ite.hasNext()){
					Map.Entry p=(Entry) ite.next();
					List<Relationship> r=(List<Relationship>) p.getValue();
					System.out.println("Rank: "+rank);
					for(Relationship rel:r){
						System.out.println(rel.getProperty("RelationType"));
					}
					}
				System.out.println("The Cost of the Path: "+cost);
				System.out.println("---------------------------");
				rank++;
			}

			
		}
				
		tx.success();
		//tx.close();
		
		neo.shutDown(graphDataService);	
			
	}
}
