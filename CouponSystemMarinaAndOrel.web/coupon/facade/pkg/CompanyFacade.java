package facade.pkg;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import company.pkg.Company;
import company.pkg.CompanySQLException;
import company.pkg.ConnectionPool;
import coupon.pkg.CoupSQLException;
import coupon.pkg.Coupon;
import coupon.pkg.CouponDBDAO;
import coupon.pkg.CouponType;
import customer.pkg.Customer;
import customer.pkg.CustomerDBDAO;
import customer.pkg.CustomerSQLException;


public class CompanyFacade implements CouponClientFacade {
	
	private long companyID;
	private String companyName = null;
	private String companyPassword = null;

	public CompanyFacade(long companyID) {
		super();
		this.companyID = companyID;
	}
	public CompanyFacade(long companyID, String companyName, String companyPassword) {
		super();
		this.companyID = companyID;
		this.companyName = companyName;
		this.companyPassword = companyPassword;
	}
	public CompanyFacade(String companyName, String companyPassword) throws FacadeException {
		super();
		this.companyName = companyName;
		this.companyPassword = companyPassword;
		this.companyID = getCompanyByName(companyName).getId();
		System.out.println(this.companyID);
	}
	public CompanyFacade(){
	}
	
	public void createCoupon(Coupon c) throws CompanySQLException{
		Date startDate = new Date(Calendar.getInstance().getTime().getTime());
		Date end = c.getEndDate();
		Date start = c.getStartDate();
		String coupon = "";
		String cTitle = c.getTitle();
		if (cTitle==null || cTitle.trim().isEmpty()){
		 	throw new CompanySQLException("Please Enter Coupon Name.");
		}	
		if (c.getStartDate()==null){
			c.setStartDate(startDate);
		}
		if (start.after(startDate)){
				c.setStartDate(start);
				}	
		if (c.getEndDate() == null){
			throw new CompanySQLException("Please enter a valid expiration date.");	
		}
		if (start.after(end)){
			throw new CompanySQLException("Please enter a valid expiration date.");	
		}
		if (c.getAmount() < 0){
			throw new CompanySQLException("Please enter a valid amount of coupons");
		}

	 Connection conn = ConnectionPool.getInstance().getConnection();	
	 
	 try {
			PreparedStatement prpst = conn.prepareStatement("SELECT Title FROM CouponsL WHERE Title = '" + cTitle + "'");
			ResultSet rs = prpst.executeQuery();
			while (rs.next()){
				coupon = rs.getString("Title");
			}
			if (c.getTitle().equals(coupon)){
				throw new CompanySQLException("Coupon name already exists in the system, please pick a different name.");
			}else{	
				CouponDBDAO dbdao = new CouponDBDAO();
				dbdao.createCoupon(c);
			}	
			
			long compId = this.companyID;
			String sql = "INSERT INTO COMPANY_COUPON (CompaniesL , CouponsL) VALUES (?,?)";
			PreparedStatement prpst2 = conn.prepareStatement(sql);
			prpst2.setLong(1, compId);
			prpst2.setLong(2, c.getId());
			prpst2.executeUpdate();
	 
	 } catch (SQLException | CoupSQLException e) {
			throw new CompanySQLException("Error: Coupon was not created." , e);
		}	
	}
	public void removeCoupon(Coupon c) throws CompanySQLException, CoupSQLException{
		long id = c.getId();
		
		String sql = "DELETE FROM CUSTOMER_COUPON WHERE CouponsL = " + id;
		 
		Connection conn = ConnectionPool.getInstance().getConnection();
				
			try {
					PreparedStatement prpst = conn.prepareStatement(sql);
					prpst.executeUpdate();
					CouponDBDAO cdb =  new CouponDBDAO();
					cdb.removeCoupon(c);

				} catch (SQLException e) {
					throw new CompanySQLException("Could not remove coupon. Please try again. ", e);
				}			
	}
	
	public void updateCoupon(Coupon c) throws CompanySQLException, CoupSQLException{
		String title = "";
		long id = 0;
		Date start = c.getStartDate();
		Date end = c.getEndDate();
		Date startDate = new Date(Calendar.getInstance().getTime().getTime());
		if (c.getStartDate()==null){
			c.setStartDate(startDate);
		}
		if (c.getStartDate().after(startDate)){
				c.setStartDate(start);
				}	
		if (c.getEndDate() == null){
			throw new CompanySQLException("Please enter a valid expiration date.");	
		}
		if (c.getStartDate().after(end)){
			throw new CompanySQLException("Please enter a valid expiration date.");	
		}
		if (c.getAmount() < 0){
			throw new CompanySQLException("Please enter a valid amount of coupons");
		}
		 Connection conn = ConnectionPool.getInstance().getConnection();	
		 
		 try {
				PreparedStatement prpst = conn.prepareStatement("SELECT id,Title FROM CouponsL WHERE Title = '" + c.getTitle() + "' OR ID = " + c.getId());
				ResultSet rs = prpst.executeQuery();
				while (rs.next()){
					id = rs.getLong("ID");
					title = rs.getString("Title");

				}
				if (c.getTitle().equals(title) || id == c.getId()){
					CouponDBDAO db = new CouponDBDAO();
					db.updateCoupon(c);
					System.out.println(c.getId());
		
				}else {
					throw new CompanySQLException("Couldn't find Coupon.");
				}
		 }catch (SQLException e) {
				throw new CompanySQLException("Couldn't update company. Please try again.");
		}	
		}
	
	public Coupon getCoupon(long id) throws CompanySQLException, CoupSQLException{
		CouponDBDAO db = new CouponDBDAO();
		Coupon coupon = db.getCoupon(id);
		return coupon;	
	}
	
	public Collection<Coupon> getAllCoupons() throws CompanySQLException, CoupSQLException{
		long compId = this.companyID;
		String sql = "SELECT * FROM COMPANY_COUPON WHERE CompaniesL=" + compId;
		CouponDBDAO db = new CouponDBDAO();
		Collection<Coupon> companyCoupList = new ArrayList<>();
		Connection conn = ConnectionPool.getInstance().getConnection();	
		 System.out.println(this.companyID);
				PreparedStatement prpst;
				try {
					prpst = conn.prepareStatement(sql);
					ResultSet rs = prpst.executeQuery();
					while (rs.next()){
						long coupId = rs.getLong("CouponsL");
						Coupon c = db.getCoupon(coupId);
						companyCoupList.add(c);
						System.out.println(c.toString());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		return companyCoupList;
	}
	
	public Collection<Coupon> getCouponByType(CouponType type) throws CompanySQLException{
		String cType = null;
		Coupon coupon = new Coupon();
		long id = 0;
		ArrayList<Coupon> couponList = new ArrayList<>();
		Connection conn = ConnectionPool.getInstance().getConnection();
		String sql ="SELECT CouponsL FROM COMPANY_COUPON WHERE CompaniesL=" + this.getCompanyID();
		try {
			PreparedStatement prpst2 = conn.prepareStatement(sql);
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					id = rs.getLong(1);
					coupon = this.getCoupon(id);
					if (id!=0 && coupon.getType()!=null){
						cType = coupon.getType().toString();
							if( cType==type.toString()){
								System.out.println(coupon.toString());	
								couponList.add(coupon);
						}
					}
			}
		}catch (SQLException | CoupSQLException e) {
			throw new CompanySQLException("Couldn't get coupon by type. Please try again.", e);
		}		
		return couponList;
	}
	
	@Override
	public CompanyFacade login(String user, String password, ClientType type) throws FacadeException {
		String cName = user;
		String cPass = password;
		String company = "";
		String pass = "";
		String email = "";
		long id = 0;
	
		 Connection conn = ConnectionPool.getInstance().getConnection();

			try {
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CompaniesL WHERE CompanyName='" + cName + "'");
				ResultSet rs = prpst2.executeQuery();
				if (rs.next()){
					id = rs.getLong("ID");
					company = rs.getString("CompanyName");
					pass = rs.getString("CompanyPassword");
					email = rs.getString("CompanyEmail");
				}
			if (cName.equals(company) && cPass.equals(pass)){
				System.out.println("login");
				Company c = new Company();
				c.setCompName(cName);
				c.setPassword(cPass);	
				c.setEmail(email);
				c.setId(id);
			} else {
				throw new FacadeException("Username and/or password are incorrect.");
			}
			if (type!=ClientType.COMPANY){
				throw new FacadeException("Please login with a Company account.");
			}
			} catch (SQLException e){
				throw new FacadeException("Login Failed.", e);
			}		
	return new CompanyFacade();
	}

	public Company getCompanyByName(String name) throws FacadeException {
		String company = "";
		String pass = "";
		String email = "";
		Company c = new Company();
		long id = 0;
	
		 Connection conn = ConnectionPool.getInstance().getConnection();

			try {
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CompaniesL WHERE CompanyName='" + name + "'");
				ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					id = rs.getLong("ID");
					company = rs.getString("CompanyName");
					pass = rs.getString("CompanyPassword");
					email = rs.getString("CompanyEmail");
					
					c.setCompName(company);
					c.setPassword(pass);	
					c.setEmail(email);
					c.setId(id);
				}
			}catch (SQLException e){
				throw new FacadeException("Couldn't get company details." , e);
			}
		return c;
	}
	public long getCustomerID(Customer customer) throws CustomerSQLException{
			CustomerDBDAO cdb = new CustomerDBDAO();
			long id = cdb.getCustomerID(customer);
		return id;
		
	}
	public long getCouponID(Coupon coupon) throws  CoupSQLException{
		CouponDBDAO cdb = new CouponDBDAO();
		long id = cdb.getCouponID(coupon);
	return id;
	
}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyPassword() {
		return companyPassword;
	}
	public void setCompanyPassword(String companyPassword) {
		this.companyPassword = companyPassword;
	}
	public void setCompanyID(long companyID) {
		this.companyID = companyID;
	}
	public long getCompanyID() {
		return companyID;
	}
}
