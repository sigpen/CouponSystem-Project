package coupon.pkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import company.pkg.ConnectionPool;

public class CouponDBDAO implements CouponDAO {
	
	//Creating coupon in DB
	@Override
	public void createCoupon(Coupon c) throws CoupSQLException {
			String type = null;
			String title = null;
			//Check if title is empty.
			if (c.getTitle()==null || c.getTitle().trim() == ""){
				throw new CoupSQLException("Please Enter Coupon Title.");
		 	} else {
		 		title = c.getTitle();
		 	}
		 
		 //Setting CouponType. Default setting of CouponType is 'OTHER'
		 if (c.getType()!=null){
			 type = c.getType().name();

		 }else {
			 type = CouponType.OTHER.name();
		 	}	
		 String message = c.getMessage();
		 String image = c.getImage();
		 int amount = c.getAmount();
		 java.sql.Date startDate = c.getStartDate();
		 java.sql.Date endDate = c.getEndDate();
		 double price = c.getPrice();
		 String sql ="INSERT INTO ";
		 
		 Connection conn = ConnectionPool.getInstance().getConnection();
		 
		 PreparedStatement prpst;
		 
		try {
				prpst = conn.prepareStatement(sql  + "CouponsL (Title , Message , Image, Amount, startDate, endDate, Price, Type) VALUES(?,?,?,?,?,?,?,?) ");
				prpst.setString(1, title);
				prpst.setString(2, message);
				prpst.setString(3, image);
				prpst.setInt(4, amount);
				prpst.setDate(5, startDate);
				prpst.setDate(6, endDate);
				prpst.setDouble(7, price);
				prpst.setString(8, type);
				prpst.executeUpdate();
				PreparedStatement prpst2 = conn.prepareStatement("SELECT id FROM CouponsL WHERE Title='" + title + "'");
				ResultSet rs = prpst2.executeQuery();
		while (rs.next()){
			long idnum = rs.getLong("id");
			c.setId(idnum);
		}	
		
		} catch (SQLException e) {
			throw new CoupSQLException("Error: Coupon was not created. Remember, the coupon title has to be unique.  " + e);
		}
		}
	//Removing coupon from DB
	@Override
	public void removeCoupon(Coupon c) throws CoupSQLException {
		long id = 0;
		if (c.getId()==0){
			 id = getCouponID(c);
		}else {
			id = c.getId();
		}
		

		if(id==0){
			
			throw new CoupSQLException ("Coupon not found");
		}
		
		String sql = "DELETE FROM CouponsL WHERE id = " + id;
		 
		Connection conn = ConnectionPool.getInstance().getConnection();
				
			try {
				
				PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CouponsL WHERE id=" + id);
				ResultSet rs = prpst2.executeQuery();
				while (rs.next()){
					long idnum = rs.getLong("id");
					String s = rs.getString("Title");
					
					c.setId(idnum);
					System.out.print("Removing: " + idnum + ", ");
					System.out.println(s);


					PreparedStatement prpst = conn.prepareStatement(sql);
					prpst.executeUpdate();
				}
						
				} catch (SQLException e) {
					throw new CoupSQLException("Could not remove coupon. Please try again. ", e);
				}					
	}
	//Update coupon in database
	@Override
	public void updateCoupon(Coupon c) throws CoupSQLException {
		
		long id = c.getId();
		System.out.println();
		String title = c.getTitle();
		String message = c.getMessage();
		String image = c.getImage();
		int amount = c.getAmount();
		java.sql.Date startDate = c.getStartDate();
		java.sql.Date endDate = c.getEndDate();
		double price = c.getPrice();
		System.out.println(title + " " + id);
		String type = null;
		if (c.getType()!=null){
			 type = c.getType().name();
		}
		String sql ="UPDATE CouponsL SET Title = ? , Message = ? , Image = ? ,"
				+ " Amount = ? , Price = ?, startDate = ? , endDate = ?, Type = ?";
		 
		 Connection conn = ConnectionPool.getInstance().getConnection();
			
			try {
				PreparedStatement prpst = conn.prepareStatement(sql  + " WHERE id  = " + id);
				if (id == 0){
					throw new CoupSQLException("Couldn't find the coupon.Please try again");
				}
				prpst.setString(1, title);
				prpst.setString(2, message);
				prpst.setString(3, image);
				prpst.setInt(4, amount);
				prpst.setDouble(5, price);
				prpst.setDate(6, startDate);
				prpst.setDate(7, endDate);
				prpst.setString(8, type);
				
				prpst.executeUpdate();	
						
			} catch (SQLException e) {
				throw new CoupSQLException("Could not update coupon.Customer can purchase only 1  of the coupon. ", e);	
			}		
	}
		//getting coupon object through it's ID that's located in DB
	@Override
	public Coupon getCoupon(long id) throws CoupSQLException {
		long cId = id;
		String title = null;
		String message = null;
		String image = null;
		java.sql.Date startDate = null;
		java.sql.Date  endDate = null;
		int amount = 0;
		double price = 0;
		String type = null;
		Coupon c = new Coupon();
		
		Connection conn = ConnectionPool.getInstance().getConnection();
		
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CouponsL WHERE id = " + cId);
			ResultSet rs = prpst2.executeQuery();
			while(rs.next()) {  
			    	title = rs.getString(2);
					message = rs.getString(3);
					image = rs.getString(4);
					amount = rs.getInt(5);
					startDate = rs.getDate(6);
					endDate = rs.getDate(7);
					price = rs.getDouble(8);
					type = rs.getString(9);
	
					c.setId(cId);	
					c.setTitle(title);
					c.setMessage(message);
					c.setImage(image);
					c.setAmount(amount);
					c.setPrice(price);
					c.setStartDate(startDate);
					c.setEndDate(endDate);
					c.setType(enumCheck(type));
			}
		} catch (SQLException e) {
			throw new CoupSQLException("Couldn't get the Coupon. ", e );
		}
		return c;
	}
		//getting all coupons as an array from the DB
	@Override
	public Collection<Coupon> getAllCoupons() throws CoupSQLException {
		
		Collection<Coupon> coupList = new ArrayList<>();
		Connection conn = ConnectionPool.getInstance().getConnection();
		
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CouponsL");
			ResultSet rs = prpst2.executeQuery();
			
			while (rs.next()){
				long id = rs.getLong(1);
				String title = rs.getString(2);
				String message = rs.getString(3);
				String image = rs.getString(4);
				int amount = rs.getInt(5);
				java.sql.Date startDate = rs.getDate(6);
				java.sql.Date endDate = rs.getDate(7);
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
				c.setType(enumCheck(type));
				coupList.add(c);
		
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
			throw new CoupSQLException("Couldn't get all coupons. ", e );
		}
		return coupList;
	}
		//Getting all coupons in an array  by Type 
	@Override
	public Collection<Coupon> getCouponbyType(CouponType cType) throws CoupSQLException {
		String type = cType.name(); 
		Collection<Coupon> TypeArr = new ArrayList<>();
		
		Connection conn = ConnectionPool.getInstance().getConnection();
		long id = 0;
		
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT * FROM CouponsL WHERE Type='" + type + "'");
			ResultSet rs = prpst2.executeQuery();
	
			while (rs.next()){
				Coupon c = new Coupon();
				id = rs.getLong("id");
				String title = rs.getString(2);
				String message = rs.getString(3);
				String image = rs.getString(4);
				java.sql.Date startDate = rs.getDate(5);
				java.sql.Date endDate = rs.getDate(6);
				int amount = rs.getInt(7);
				double price = rs.getDouble(8);

				c.setId(id);	
				c.setTitle(title);
				c.setMessage(message);
				c.setImage(image);
				c.setAmount(amount);
				c.setPrice(price);
				c.setStartDate(startDate);
				c.setEndDate(endDate);
				c.setType(enumCheck(type));
				TypeArr.add(c);
			}
		}
		catch (SQLException e) {
			throw new CoupSQLException("Could not get coupon by Type. Please try again. ", e);	
			}
		return TypeArr;
	}
			//Getting coupon's ID through its object (using its title)
	public long getCouponID(Coupon c) throws CoupSQLException{
		String title = c.getTitle();
		Connection conn = ConnectionPool.getInstance().getConnection();
		long id = 0;
		try {
			PreparedStatement prpst2 = conn.prepareStatement("SELECT ID FROM CouponsL WHERE Title='" + title + "'");
			ResultSet rs = prpst2.executeQuery();
			while (rs.next()){
				id = rs.getLong("ID");
				}
			}
		catch (SQLException e) {
			throw new CoupSQLException("Could not get coupon ID. Please try again.Remember, Coupon titles are case sensitive. ", e);
			}
		return id;
	}
		//Checking the the CouponType exist in the system as an Enum
	public static CouponType enumCheck(String text) {
	    if (text != null) {
	      for (CouponType c : CouponType.values()) {
	        if (text.equalsIgnoreCase(c.name())) {
	          return c;
	        }
	      }
	    }
	    return null;	
	}
}
