

public class test_neo4j {

	public static void main(String[] args) {
		
		Connection_to_neo4j con=new Connection_to_neo4j();
		con.createDatabase();
		//con.removeData();
		//con.shutDown();
		
	}

}
