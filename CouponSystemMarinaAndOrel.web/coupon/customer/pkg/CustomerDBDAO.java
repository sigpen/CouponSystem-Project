package customer.pkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import company.pkg.ConnectionPool;

public class CustomerDBDAO implements CustomerDAO {

	@Override // Creating customer in the DataBase
	public void createCustomer(Customer c) throws CustomerSQLException {
		String cust_name = c.getCust_name();
		String password = c.getPassword();

	 String sql ="INSERT INTO ";
	 Connection conn = ConnectionPool.getInstance().getConnection();	
	 PreparedStatement prpst;
	 
	try {
			prpst = conn.prepareStatement(sql  + "CustomersL (CUSTOMERNAME, CUSTOMERPASSWORD) VALUES(?,?)");
			prpst.setString(1, cust_name);
			prpst.setString(2, password);
			prpst.executeUpdate();
			System.out.println(cust_name + " " + password);
			PreparedStatement prpst2 = conn.prepareStatement("SELECT id FROM CustomersL WHERE CUSTOMERNAME='" + cust_name + "'");
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					long idnum = rs.getLong("id");
					c.setId(idnum);
					System.out.println(cust_name + " id is: " + idnum);
				}	
	
	} catch (SQLException e) {
		e.printStackTrace();
		throw new CustomerSQLException("Error: Customer was not created. Remember, the customer name has to be unique.  " + e);
	}
	}
	//Removing customer from DB
	@Override
	public void removeCustomer(Customer c) throws CustomerSQLException {
		long id = c.getId();
		
		if(id==0){
			
			throw new CustomerSQLException("Customer not found");
		}
		
		String sql = "DELETE FROM CustomersL WHERE ";
		String sql2 = "id = " + id;
		 
		Connection conn = ConnectionPool.getInstance().getConnection();
				
			try {
				
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CustomersL WHERE id=" + id);
				ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					long idnum = rs.getLong("id");
					c.setId(idnum);

					PreparedStatement prpst = conn.prepareStatement(sql + sql2);
					prpst.executeUpdate();
				}
				
				} catch (SQLException e) {
					throw new CustomerSQLException("Could not remove customer. Please try again. ", e);
				}				
	}
		//Updating customer in DB
	@Override
	public void updateCustomer(Customer c) throws CustomerSQLException {
		String custName = c.getCust_name();
		String password = c.getPassword();
		long id = c.getId();
		String sql ="UPDATE CustomersL SET ";
		Connection conn = ConnectionPool.getInstance().getConnection();
			
			try {
				PreparedStatement prpst = conn.prepareStatement(sql  + "CustomerName = ? , CustomerPassword = ?  WHERE id  = " + id);
				if (id == 0){
					throw new CustomerSQLException("Coudln't find the customer.Please try again");
				}
				prpst.setString(1, custName);
				prpst.setString(2, password);
				prpst.executeUpdate();
			} catch (SQLException e) {
				throw new CustomerSQLException("Could not update customer. Please try again. ", e);	
			}		

	}
		//getting customer object through its id
	@Override
	public Customer getCustomer(long id) throws CustomerSQLException {
		String custName = null;
		String password = null;
		long custId = id;
		Customer cust = new Customer();
		
		Connection conn = ConnectionPool.getInstance().getConnection();
		
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CustomersL WHERE id = " + custId);
			ResultSet rs = prpst2.executeQuery();
			System.out.println(rs.toString());
			
			if(rs.next()) { 
			    do { 
			    	custName = rs.getString(2);
					password = rs.getString(3);
					
			    } while (rs.next());
			    
			} else {
				throw new CustomerSQLException("Customer was not found.");
			}
			cust.setCust_name(custName);
			cust.setPassword(password);
			cust.setId(custId);
			
		} catch (SQLException e) {
			throw new CustomerSQLException("Couldn't get the company. ", e );
		}

		return cust;
	}
		//getting a collection of all customers from DB
	@Override
	public Collection<Customer> getAllCustomers() throws CustomerSQLException {
		
		Collection<Customer> custList = new ArrayList<>();
		Connection conn = ConnectionPool.getInstance().getConnection();
		
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CustomersL");
			ResultSet rs = prpst2.executeQuery();
			
			while (rs.next()){
				long id = rs.getLong(1);
				String custName = rs.getString(2);
				String password = rs.getString(3);
				System.out.println(custName + " - " + password + " - " + id);
				
				Customer c = new Customer();
				c.setId(id);
				c.setCust_name(custName);
				c.setPassword(password);
				custList.add(c);
				c.toString();
			}
			} catch (SQLException e) {
				throw new CustomerSQLException("Couldn't get all companies. ", e );
			}
		return custList;
	}
public long getCustomerID(Customer c) throws CustomerSQLException{
	String custName = c.getCust_name();
	Connection conn = ConnectionPool.getInstance().getConnection();
	long id = 0;
	
	try {
		PreparedStatement prpst2 = conn.prepareStatement("SELECT id FROM CustomersL WHERE CustomerName='" + custName + "'");
		ResultSet rs = prpst2.executeQuery();
		
		while (rs.next()){
			id = rs.getLong("id");
		}
	}
	catch (SQLException e) {
		throw new CustomerSQLException("Could not get coupon ID. Please try again. ", e);	
	}
	
	return id;
}

}

