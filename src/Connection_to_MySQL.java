import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Connection_to_MySQL {
	
	static String host="jdbc:mysql://localhost/homework2";
	static String username="root";
	static String password="root";
	static Connection con;
	
	public static void connection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void toMySQL(){
    	connection();
    	try {
			con=DriverManager.getConnection(host, username, password);
			//System.out.print("Worked!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}