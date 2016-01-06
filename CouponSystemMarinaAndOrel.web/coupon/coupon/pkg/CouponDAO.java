package coupon.pkg;

import java.util.Collection;

import company.pkg.CompanySQLException;

public interface CouponDAO {


	public void createCoupon(Coupon c)  throws CoupSQLException, CompanySQLException;
	
	public void removeCoupon(Coupon c) throws CoupSQLException;
	
	public void updateCoupon(Coupon c)throws CoupSQLException;
	
	public Coupon getCoupon(long id) throws CoupSQLException;
	
	public Collection<Coupon> getAllCoupons() throws CoupSQLException;

	public Collection<Coupon> getCouponbyType(CouponType cType) throws CoupSQLException;

	
	
	
	
}
