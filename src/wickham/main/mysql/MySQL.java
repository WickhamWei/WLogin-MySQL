package wickham.main.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQL {
	private String host, databaseNameString, usernameString, passwordString;
	private int port;
	
	private Connection connection ; 
	
	public void openConnection() throws SQLException, ClassNotFoundException {  
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + databaseNameString, usernameString, passwordString);
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
	  
    public boolean disconnection() {
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return databaseNameString;
	}

	public void setDatabase(String database) {
		this.databaseNameString = database;
	}

	public String getUsername() {
		return usernameString;
	}

	public void setUsername(String username) {
		this.usernameString = username;
	}

	public String getPassword() {
		return passwordString;
	}

	public void setPassword(String password) {
		this.passwordString = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
    
    
}
