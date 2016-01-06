package company.pkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import couponSystemPack.DBCreator;


public class ConnectionPool {
	
	private final int MAX_POOL_CONNECTIONS = 20;
	private ArrayList<Connection> cons = new ArrayList<>();
	private static ConnectionPool instance;
	private String url = "jdbc:derby://localhost:1527/CouponDB;create=true;";
	Connection con = null;
	
	static{
		
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private ConnectionPool(){
		for (int i = 0; i < MAX_POOL_CONNECTIONS; i++) {	
			try {
				con = DriverManager.getConnection(url);
				cons.add(con);
			}	
			catch (SQLException e) {
				System.out.println("Connection was not established.");
				e.printStackTrace();
			}
		} 
	}
	public static ConnectionPool getInstance() {
		if (instance == null){
		instance = new ConnectionPool();
		}
		return instance;
	}

	public synchronized Connection getConnection() {
		
		while (cons.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	 return cons.remove(0);
	}

	public synchronized void returnCon(Connection con){
		cons.add(con);
		notifyAll();
		
	}
	
	public void closeAllCons(){
		for (Connection con : cons) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}