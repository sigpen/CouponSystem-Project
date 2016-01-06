package facade.pkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


import company.pkg.ConnectionPool;
import coupon.pkg.CoupSQLException;
import coupon.pkg.Coupon;
import coupon.pkg.CouponDBDAO;
import coupon.pkg.CouponType;
import customer.pkg.Customer;
import customer.pkg.CustomerDBDAO;
import customer.pkg.CustomerSQLException;

public class CustomerFacade implements CouponClientFacade{
	
	private long custId;
	public String custName;
	public String custPass;
	
	public CustomerFacade() {
		super();
	}

	public CustomerFacade(long custId, String custName) {
		super();
		this.custId = custId;
		this.custName = custName;
	}

	public CustomerFacade(long custId, String custName, String custPass) {
		super();
		this.custId = custId;
		this.custName = custName;
		this.custPass = custPass;
	}

	public CustomerFacade(String user, String password) throws CustomerSQLException {
		super();
		this.custName = user;
		this.custPass = password;
		CustomerDBDAO db = new CustomerDBDAO();
		this.custId = db.getCustomerID(new Customer(user, password));
		}

	// Purchasing a coupon, adding customer to a joined list and reducing coupons amount by 1.
	public void purchaseCoupon(Coupon c) throws CoupSQLException{
		long id = c.getId();
		CouponDBDAO db = new CouponDBDAO();
		if (c.getId() == 0){
			 id = db.getCouponID(c);	
		}
		CouponDBDAO cd = new CouponDBDAO();
		c = cd.getCoupon(id);
		int amount = c.getAmount();

		if (amount <= 0){
			throw new CoupSQLException("This coupon has sold out.");
		}else{
			amount = amount - 1;
		}
		String sql ="UPDATE CouponsL SET Amount = ? WHERE id  = " + id;
		String joinSql = "INSERT INTO Customer_Coupon (CustomersL, CouponsL) VALUES (?,?)";
		 
		Connection conn = ConnectionPool.getInstance().getConnection();
			
			try {
				PreparedStatement prpst = conn.prepareStatement(sql);
				if (id == 0){
					throw new CoupSQLException("Couldn't find the coupon.Please try again");
				}
				prpst.setInt(1, amount);
				prpst.executeUpdate();	
				PreparedStatement prpst2 = conn.prepareStatement(joinSql);
				prpst2.setLong(1, this.custId);
				prpst2.setLong(2, c.getId());
				prpst2.executeUpdate();
				
			} catch (SQLException e) {
				throw new CoupSQLException("Could not purchase coupon. Customer can make 1 purchase per coupon. ", e);	
			}		
	}
	
	// Getting all purchased items by a specific user, returning a purchased coupons collection 
	public Collection<Coupon> getAllPurchasedCoupons() throws CoupSQLException{
		Coupon coupon = new Coupon();
		CouponDBDAO cdb = new CouponDBDAO();
		long id = 0;
		ArrayList<Coupon> couponList = new ArrayList<>();
		Connection conn = ConnectionPool.getInstance().getConnection();
		String sql ="SELECT CouponsL FROM CUSTOMER_COUPON WHERE CUSTOMERSL=" + this.custId;
	
		try {
			PreparedStatement prpst2 = conn.prepareStatement(sql);
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					id = rs.getLong("CouponsL");
					coupon = cdb.getCoupon(id);
					couponList.add(coupon);
					System.out.println(coupon.toString());
						}
			}catch (SQLException e){
				throw new CoupSQLException("Couldn't get all the coupons of the user");	
			} 
		return couponList;
	}
	
	// Getting  purchased items by type from a specific user, returning a purchased coupons collection 
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) throws CoupSQLException{
		Coupon coupon = new Coupon();
		CouponDBDAO cdb = new CouponDBDAO();
		long id = 0;
		ArrayList<Coupon> couponList = new ArrayList<>();
		Connection conn = ConnectionPool.getInstance().getConnection();
		String sql ="SELECT CouponsL FROM CUSTOMER_COUPON WHERE CUSTOMERSL=" + this.custId;
	
		try {
			PreparedStatement prpst2 = conn.prepareStatement(sql);
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					id = rs.getLong(1);
					coupon = cdb.getCoupon(id);
					if (type.equals(coupon.getType())){
						couponList.add(coupon);
						}
				}
			}catch (SQLException e){
				throw new CoupSQLException("Couldn't get all the coupons by type of the user");
			} 
		return couponList;
	}
	
	// Getting  purchased items by price from a specific user, returning a purchased coupons collection 
	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) throws CoupSQLException{
		Coupon coupon = new Coupon();
		CouponDBDAO cdb = new CouponDBDAO();
		long id = 0;
		ArrayList<Coupon> couponList = new ArrayList<>();
		Connection conn = ConnectionPool.getInstance().getConnection();
		String sql ="SELECT CouponsL FROM CUSTOMER_COUPON WHERE CUSTOMERSL=" + this.custId;
	
		try {
			PreparedStatement prpst2 = conn.prepareStatement(sql);
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					id = rs.getLong(1);
					coupon = cdb.getCoupon(id);
					if (price >= coupon.getPrice()){
						couponList.add(coupon);
						}
				}
			}catch (SQLException e){
				throw new CoupSQLException("Couldn't get all the coupons by price of the user");
			} 
		return couponList;
}
	//logging in as customer, verifying user & pass and returning a CustomerFacade object.
	@Override
	public CustomerFacade login(String user, String password, ClientType type) throws FacadeException {
		String userName = "";
		String pass = "";
		long id = 0;
		CustomerFacade c = new CustomerFacade();
		Connection conn = ConnectionPool.getInstance().getConnection();

			try {
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CustomersL WHERE CustomerName='" + user + "'");
				ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					id = rs.getLong("ID");
					userName = rs.getString("CustomerName");
					pass = rs.getString("CustomerPassword");
	
				}
				if (user.equals(userName) && password.equals(pass)){		
					c.setCustName(user);
					c.setCustPass(password);
					c.setCustId(id);
			} else {
				throw new FacadeException("Username and/or password are incorrect.");
			}
			if (type==ClientType.COMPANY){
				throw new FacadeException("Please login with a Company account.");
			}
			} catch (SQLException e){
				throw new FacadeException("Login Failed.", e);
			}
		return c;
	}
	public Collection<Coupon> getAllCoupons() throws CustomerSQLException, CoupSQLException {
	ArrayList<Coupon> CouponList = new ArrayList<>();
	Coupon c = new Coupon();
	Connection conn = ConnectionPool.getInstance().getConnection();
	
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT CouponsL FROM CUSTOMER_COUPON WHERE CustomersL=" + this.custId);
			ResultSet rs = prpst2.executeQuery();
			while (rs.next()){
					long idnum = rs.getLong("CouponsL");
	
					CouponDBDAO coup = new CouponDBDAO();
					c = coup.getCoupon(idnum);		
					CouponList.add(c);
					}
		}catch (SQLException e) {
			throw new CustomerSQLException("Couldn't get all coupons. Please try again.");
	}		
	return CouponList;
	}
	public long getCustId() {
		return custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustPass() {
		return custPass;
	}

	public void setCustPass(String custPass) {
		this.custPass = custPass;
	}
}
