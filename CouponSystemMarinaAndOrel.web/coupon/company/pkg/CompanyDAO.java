package company.pkg;

import java.util.Collection;

import coupon.pkg.Coupon;

public interface CompanyDAO {
	
	public void createCompany(Company c) throws CompanySQLException;
	
	public void removeCompany(Company c) throws CompanySQLException;
	
	public void updateCompany(Company c) throws CompanySQLException;
	
	public Company getCompany(long id) throws CompanySQLException;
	
	public Collection<Company> getAllCompanies() throws CompanySQLException;
	
	public Collection<Coupon> getAllCoupons() throws CompanySQLException;
	
	
	
	
	

}
