package facade.pkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import company.pkg.Company;
import company.pkg.CompanyDBDAO;
import company.pkg.CompanySQLException;
import company.pkg.ConnectionPool;
import coupon.pkg.CoupSQLException;
import coupon.pkg.CouponDBDAO;
import customer.pkg.Customer;
import customer.pkg.CustomerDBDAO;
import customer.pkg.CustomerSQLException;

public class AdminFacade implements CouponClientFacade {
	
	private final String ADMIN_USER = "admin";
	private final String ADMIN_PASSWORD = "1234";
	
	public AdminFacade() {
		// TODO Auto-generated constructor stub
	}
	
	public AdminFacade(String user, String password) {
		// TODO Auto-generated constructor stub
	}

	public void createCompany(Company c) throws CompanySQLException{
		String cName = c.getCompName();
		String cPass = c.getPassword();
		String company = "";
		 if (cName==null || cName.trim().isEmpty()){
			 	throw new CompanySQLException("Please Enter Company Name.");
		 }
		 if (cPass==null || cPass.trim().isEmpty()){
			 	throw new CompanySQLException("Please Enter Company Password.");
		 }
		 Connection conn = ConnectionPool.getInstance().getConnection();

			try {
				PreparedStatement prpst = conn.prepareStatement("SELECT CompanyName FROM CompaniesL WHERE CompanyName='" + cName + "' ");
				ResultSet rs = prpst.executeQuery();
				while (rs.next()){
					company = rs.getString("CompanyName");
				}
				
			if (cName.equals(company)){
				throw new CompanySQLException("The Company name already exists in the system, please pick a different name.");
			}else{
				CompanyDBDAO dbdao = new CompanyDBDAO();
				dbdao.createCompany(c);
			}	
			
			} catch (SQLException e) {
				throw new CompanySQLException("Couldn't create a company.");
			}	
	}
	
	public void removeCompany(Company c) throws CompanySQLException, CoupSQLException{
		long id = c.getId();
		long coupId = 0;
		
		String sql = "SELECT * FROM COMPANY_COUPON WHERE CompaniesL=" + id;
		String removeSql = "DELETE FROM COMPANY_COUPON WHERE CompaniesL=" + id;
		CompanyDBDAO db = new CompanyDBDAO();
		
		Connection conn = ConnectionPool.getInstance().getConnection();
				
			try {
					PreparedStatement prpst = conn.prepareStatement(sql);
					ResultSet rs = prpst.executeQuery();
					while (rs.next()){
						coupId = rs.getLong("CouponsL");
						CouponDBDAO cdb = new CouponDBDAO();
						System.out.println(coupId);
						cdb.removeCoupon(cdb.getCoupon(coupId));
						
					}
					PreparedStatement prpst2 = conn.prepareStatement(removeSql);
					prpst2.executeUpdate();
					db.removeCompany(c);
				} catch (SQLException e) {
					throw new CompanySQLException("Could not remove coupon. Please try again. ", e);
				}
	}
	
	public void updateCompany(Company c) throws CompanySQLException{
		String cName = c.getCompName();
		String name = "";
		Connection conn = ConnectionPool.getInstance().getConnection();
		
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT CompanyName FROM CompaniesL WHERE CompanyName='" + cName + "'");
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					name = rs.getString("CompanyName");			
				}	
				if (name.equals(cName)){
					CompanyDBDAO dbdao = new CompanyDBDAO();
					dbdao.updateCompany(c);
				}else{
					throw new CompanySQLException("Cannot change company name. Please try again.");
				}
		} catch (SQLException e) {
			throw new CompanySQLException("Couldn't create a company.");
		}				
	}
		
	public Company getCompany(long id) throws CompanySQLException{
			CompanyDBDAO db = new CompanyDBDAO();
			Company comp = db.getCompany(id);
		return comp;
	}
	public Collection<Company> getAllCompanies() throws CompanySQLException{
		CompanyDBDAO db = new CompanyDBDAO();
		Collection<Company> comList = db.getAllCompanies();
		return comList;
	}
	public void createCustomer(Customer c) throws CustomerSQLException {
		String customer = "";
		String custName = c.getCust_name();
		
		//Check if User or Password are given empty.
	 if (c.getCust_name()==null || c.getCust_name().trim().isEmpty()){
		 	throw new CustomerSQLException("Please Enter Username.");
	 	} else {
	 		custName = c.getCust_name();
	 	}
	 if (c.getPassword()==null || c.getPassword().trim().isEmpty()){
		 	throw new CustomerSQLException("Please Enter Password.");
	 	}
	 Connection conn = ConnectionPool.getInstance().getConnection();	
	 
	 try {
			PreparedStatement prpst = conn.prepareStatement("SELECT CustomerName FROM CustomersL WHERE CustomerName = '" + custName + "'");
			ResultSet rs = prpst.executeQuery();
			while (rs.next()){
				customer = rs.getString("CustomerName");
				System.out.println(customer);
			}
			
		if (customer.equals(custName)){
			throw new CustomerSQLException("Username already exists in the system, please pick a different name.");
		}else{	
			CustomerDBDAO dbdao = new CustomerDBDAO();
			dbdao.createCustomer(c);
		}	
		} catch (SQLException e) {
			throw new CustomerSQLException("Error: Customer was not created." , e);
		}
	}
	
	public void removeCustomer(Customer c) throws  CustomerSQLException, CompanySQLException {
		long id = c.getId();
		long customerId = 0;
		
		String sql = "SELECT * FROM CUSTOMER_COUPON WHERE CustomersL=" + id;
		String removeSql = "DELETE FROM CUSTOMER_COUPON WHERE CustomersL=" + id;
		CustomerDBDAO db = new CustomerDBDAO();
		
		Connection conn = ConnectionPool.getInstance().getConnection();
				
			try {
					PreparedStatement prpst = conn.prepareStatement(sql);
					ResultSet rs = prpst.executeQuery();
					while (rs.next()){
						customerId = rs.getLong("CustomersL");
						CustomerDBDAO cdb = new CustomerDBDAO();
						System.out.println(customerId);
						cdb.removeCustomer(cdb.getCustomer(customerId));
						
					}
					PreparedStatement prpst2 = conn.prepareStatement(removeSql);
					prpst2.executeUpdate();
					db.removeCustomer(c);
				} catch (SQLException e) {
					throw new CompanySQLException("Could not remove coupon. Please try again. ", e);
				}
	}
	
	public void updateCustomer(Customer c) throws CustomerSQLException {
		String cName = c.getCust_name();
		String name = "";
		 if (c.getPassword()==null || c.getPassword().trim().isEmpty()){
			 	throw new CustomerSQLException("Please Enter Password.");
		 	}
		
		Connection conn = ConnectionPool.getInstance().getConnection();
		
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT CustomerName FROM CustomersL WHERE CustomerName='" + cName + "'");
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
						name = rs.getString("CustomerName");			
				}	
				if (name.equals(cName)){
					CustomerDBDAO dbdao = new CustomerDBDAO();
					c.setId(dbdao.getCustomerID(c));
					dbdao.updateCustomer(c);
				}else{
					throw new CustomerSQLException("Couldn't find Customer. Customer name cannot be changed.");
				}
		} catch (SQLException e) {
			throw new CustomerSQLException("Couldn't update customer.");
		}				
	}
	
	public Customer getCustomer(long id) throws CustomerSQLException {
		CustomerDBDAO db = new CustomerDBDAO();
		Customer customer = db.getCustomer(id);	
		return customer;
	}
	public Collection<Customer> getAllCustomers() throws  CustomerSQLException {
		Collection<Customer> custList = new ArrayList<>();
		CustomerDBDAO db = new CustomerDBDAO();
		custList = db.getAllCustomers();
		return custList;
	}
	@Override
	public AdminFacade login(String user, String password, ClientType type) throws FacadeException{
		if (type!=ClientType.ADMIN){
			throw new FacadeException("Please login with an Admin account.");
		}
		if (!(ADMIN_USER.equals(user))){
			throw new FacadeException("Incorrect Username. Please try again.");
		}
		if (!(ADMIN_PASSWORD.equals(password))){
			throw new FacadeException("Incorrect Password. Please try again.");
		}
			
		return new AdminFacade();
	}
	
	public long getCustomerID(Customer customer) throws CustomerSQLException{
		CustomerDBDAO cdb = new CustomerDBDAO();
		long id = cdb.getCustomerID(customer);
		return id;
		
	}
	public long getCompanyID(Company c) throws CompanySQLException{
			CompanyDBDAO db = new CompanyDBDAO();
			long id = db.getCompanyID(c);
		return id;
		
	}

}
