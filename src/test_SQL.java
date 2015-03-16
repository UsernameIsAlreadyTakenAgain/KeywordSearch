import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class test_SQL {

	public static void main(String[] args) {
		Connection_to_MySQL link=new Connection_to_MySQL();
		String db="homework2";
		link.toMySQL(db);
		String a="d_name";
		String b="diseases";
		
		try {
			PreparedStatement statement=link.con.prepareStatement("select distinct "+a+" from "+b+"");
			ResultSet result=statement.executeQuery();
			
			while(result.next()){
				System.out.println(result.getString(1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
