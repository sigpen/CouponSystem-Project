package company.pkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import coupon.pkg.Coupon;
import coupon.pkg.CouponDBDAO;

	public class CompanyDBDAO implements CompanyDAO  {

		//Creating a company in database
		@Override
		public void createCompany(Company c)throws CompanySQLException {
			String cPass = c.getPassword();
			String cName = c.getCompName();
			String cEmail = c.getEmail();
	
			String sql = "INSERT INTO CompaniesL (CompanyName, CompanyPassword,"
					+ " CompanyEmail) VALUES "
					+ "('" + cName + "', '" + cPass + "' , '" + cEmail + "')";
			
			Connection conn = ConnectionPool.getInstance().getConnection();

			try {
				PreparedStatement prpst = conn.prepareStatement(sql);
				prpst.executeUpdate();
				PreparedStatement prpst2 = conn.prepareStatement("SELECT id FROM CompaniesL WHERE CompanyName='" + cName + "'");
				ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					long idnum = rs.getLong("id");
					c.setId(idnum);
					}	
						
			}catch (SQLException e) {
				throw new CompanySQLException("Couldn't create company. Please try again.");
				}	
		}
		//Removing company from database
		@Override
		public void removeCompany(Company c) throws CompanySQLException {
			long  cId = c.getId();
			
			if(cId==0){
				
				throw new CompanySQLException("Company not found");
			}
			Connection conn = ConnectionPool.getInstance().getConnection();
			
			String sql = "DELETE FROM CompaniesL WHERE ID=" + cId;
			
			try {
				PreparedStatement prpst = conn.prepareStatement(sql);
				prpst.executeUpdate();
				
			} catch (SQLException e) {
				throw new CompanySQLException("Couldn't remove company. Please try again.");
			}					
		    }
		//Updating company in database
		@Override public void updateCompany(Company c) throws CompanySQLException {
			String cEmail = c.getEmail();
			String cPass = c.getPassword();
			long cId = this.getCompanyID(c);
			Connection conn = ConnectionPool.getInstance().getConnection();
			
			String sql = "UPDATE CompaniesL SET CompanyPassword = ? , CompanyEmail = ? WHERE id = " + cId;
			
			try {
				PreparedStatement prpst = conn.prepareStatement(sql);
				prpst.setString(1, cPass);
				prpst.setString(2, cEmail);

				prpst.executeUpdate();
				
			} catch (SQLException e) {
				throw new CompanySQLException("Couldn't update company. Please try again." , e);
			}		
		}
		//Getting from database company using it's ID
		@Override
		public Company getCompany(long id) throws CompanySQLException {
			
			Connection conn = ConnectionPool.getInstance().getConnection();
			Company c = new Company();
			try {
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CompaniesL WHERE id=" + id);
			ResultSet rs;
			rs = prpst2.executeQuery();
			while (rs.next()){
				long idnum = rs.getLong("id");
				String name = rs.getString("CompanyName");
				String pass = rs.getString("CompanyPassword");
				String email = rs.getString("CompanyEmail");
			
				c.setCompName(name);
				c.setPassword(pass);
				c.setEmail(email);
				c.setId(idnum);
				
				System.out.print(idnum + ", ");
				System.out.print(name + ", ");
				System.out.print(pass + ", ");
				System.out.println(email + " ");
			
			}
			}catch (SQLException e) {
				throw new CompanySQLException("Couldn't creating company. Please try again.");
			}
					
			return c;
			
		}
		//Getting all Companies into an Array
		@Override
		public Collection<Company> getAllCompanies() throws CompanySQLException {
			ArrayList<Company> companyList = new ArrayList<>();
			Connection conn = ConnectionPool.getInstance().getConnection();
			
			try {
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CompaniesL");
				ResultSet rs = prpst2.executeQuery();
					while (rs.next()){
							long idnum = rs.getLong("id");
							String name = rs.getString("CompanyName");
							String pass = rs.getString("CompanyPassword");
							String email = rs.getString("CompanyEmail");
							Company c = new Company();
							
							c.setCompName(name);
							c.setPassword(pass);
							c.setEmail(email);
							c.setId(idnum);
							companyList.add(c);
				
							System.out.print(idnum + ", ");
							System.out.print(name + ", ");
							System.out.print(pass + ", ");
							System.out.println(email + " ");
							}
			}catch (SQLException e) {
				throw new CompanySQLException("Couldn't creating company. Please try again.");
			}		
			return companyList;
		}

		//Getting all coupons in to a Collection (ArrayList)
		@Override
		public Collection<Coupon> getAllCoupons() throws CompanySQLException{
			
			Collection<Coupon> compList = new ArrayList<>();
			Connection conn = ConnectionPool.getInstance().getConnection();
			
			try {
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CouponsL");
				ResultSet rs = prpst2.executeQuery();
				
				while (rs.next()){
					long id = rs.getLong(1);
					String title = rs.getString(2);
					String message = rs.getString(3);
					String image = rs.getString(4);
					java.sql.Date startDate = rs.getDate(5);
					java.sql.Date endDate = rs.getDate(6);
					int amount = rs.getInt(7);
					double price = rs.getDouble(8);
					String type = rs.getString(9);	
					Coupon c = new Coupon();
					c.setId(id);	
					c.setTitle(title);
					c.setMessage(message);
					c.setImage(image);
					c.setAmount(amount);
					c.setPrice(price);
					c.setStartDate(startDate);
					c.setEndDate(endDate);
					c.setType(CouponDBDAO.enumCheck(type));
					compList.add(c);
			
					System.out.print(c.getId() + ", ");
					System.out.print(c.getTitle() + ", ");
					System.out.print(c.getMessage() + ", ");
					System.out.print(c.getImage() + ", ");
					System.out.print(c.getAmount() + ", ");
					System.out.print(c.getPrice() + ", ");
					System.out.print(startDate + ", ");
					System.out.print(endDate + ", ");
					System.out.println(type + ", ");
				}
				
			} catch (SQLException e) {
				throw new CompanySQLException("Couldn't get all coupons from the company. ", e );
			}
			return compList;
		}
		

		//Get all companies via Console
		public static void getCompanies() throws CompanySQLException{
			
		Connection conn = ConnectionPool.getInstance().getConnection();
		Company c = new Company();
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CompaniesL");
			ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					int idnum = rs.getInt("id");
					String name = rs.getString("CompanyName");
					String pass = rs.getString("CompanyPassword");
					String email = rs.getString("CompanyEmail");
					c.setCompName(name);
					c.setPassword(pass);
					c.setEmail(email);
					c.setId(idnum);
			
					System.out.print("ID...." + idnum + ", ");
					System.out.print("IName...." + name + ", ");
					System.out.print("Pass...." + pass + ", ");
					System.out.println("Email...." + email + " ");
					}
		}catch (SQLException e) {
			throw new CompanySQLException("Couldn't get all companies. Please try again.");
		}
				
		}
		public long getCompanyID(Company c) throws CompanySQLException{
			String cName = c.getCompName();
			Connection conn = ConnectionPool.getInstance().getConnection();
			long id = 0;
			try {
				PreparedStatement prpst2 = conn.prepareStatement("SELECT ID FROM CompaniesL WHERE CompanyName='" + cName + "'");
				ResultSet rs = prpst2.executeQuery();
					while (rs.next()){
						id = rs.getLong("id");	
						}
				}
		catch (SQLException e) {
			throw new CompanySQLException("Couldn't creating company. Please try again.");
		}
			return id;
			
		}
				
	}