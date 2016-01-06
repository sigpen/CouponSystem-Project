package thread.pkg;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import company.pkg.CompanySQLException;
import company.pkg.ConnectionPool;
import coupon.pkg.CoupSQLException;
import coupon.pkg.Coupon;
import facade.pkg.CompanyFacade;

public class DailyThread implements Runnable {
	
	Date endDate;
	java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	
	public DailyThread() {
		super();
	}

	@Override
	public void run() {
//		
//		Connection conn = ConnectionPool.getInstance().getConnection();
//
//		try {
//			PreparedStatement prpst = conn.prepareStatement("Select id,endDate FROM CouponsL");
//			ResultSet rs = prpst.executeQuery();
//			while (rs.next()){
//				long id = rs.getLong("id");
//				endDate = rs.getDate("endDate");	
//				
//				if (currentDate.after(endDate)){
//					CompanyFacade cf = new CompanyFacade();
//					Coupon coup = cf.getCoupon(id);
//					cf.removeCoupon(coup);	
//			}
//			}
//			Thread.sleep(1000 * 3600 * 24);
//			
//		}catch (SQLException | CoupSQLException | CompanySQLException | InterruptedException e) {
//				try {
//					throw new CoupSQLException("Expired coupons were not deleted." , e);
//				} catch (CoupSQLException e1) {
//					e1.printStackTrace();
//				}	
//		}finally {
//			try {
//				Thread.sleep(1000 * 3600 * 24);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	public void stopTask(){
		
		Thread.interrupted();
		
	}	
}
