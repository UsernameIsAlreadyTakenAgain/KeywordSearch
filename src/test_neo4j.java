import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;



public class test_neo4j {

	public static void main(String[] args) {
		
		String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/yeah";
		  //String Neo4j_Path="/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/data";

				GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
				Transaction transction=graphDataService.beginTx();
				
		
		Connection_to_neo4j con=new Connection_to_neo4j();
		con.createDatabase();
		con.removeData();
		con.shutDown();
		/*Neo4j neo=new Neo4j();
		neo.createUniqueFactory("1", "table", "Table",graphDataService);
		neo.createUniqueFactory("2", "table", "Table",graphDataService);
		neo.createUniqueFactory("3", "table", "Table",graphDataService);
		neo.createUniqueFactory("4", "table", "Table",graphDataService);
		transction.success();
		transction.close();*/
		
		
		
		
	}

}
