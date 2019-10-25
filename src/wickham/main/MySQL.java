package wickham.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQL {
	private String host, database, username, password;
	private int port;
	
	private Connection connection ; 
	
	public void openConnection() throws SQLException, ClassNotFoundException {
		host = "localhost";
	    port = 3306;
	    database = "mc";
	    username = "root";
	    password = "16816816";   
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database, username, password);
	    }
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean isConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
	        return true;
	    }else {
	    	return false;
	    }
	}
	  
    protected boolean disconnection() {
    	 // invoke on disable.
        try { //using a try catch to catch connection errors (like wrong sql password...)
            if (connection!=null && !connection.isClosed()){ //checking if connection isn't null to
                //avoid receiving a nullpointer
                connection.close(); //closing the connection field variable.
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
