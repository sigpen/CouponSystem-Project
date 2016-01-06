package customer.pkg;

import java.util.Collection;


public interface CustomerDAO {

	
	public void createCustomer(Customer c) throws CustomerSQLException;
	
	public void removeCustomer(Customer c) throws CustomerSQLException;
	
	public void updateCustomer(Customer c) throws CustomerSQLException;
	
	public Customer getCustomer(long id) throws CustomerSQLException;
	
	public Collection<Customer> getAllCustomers() throws CustomerSQLException;
	

	
	
}
