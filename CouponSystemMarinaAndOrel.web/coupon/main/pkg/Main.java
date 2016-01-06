package main.pkg;


import company.pkg.Company;
import company.pkg.CompanySQLException;
import coupon.pkg.CoupSQLException;
import coupon.pkg.Coupon;
import coupon.pkg.CouponType;
import couponSystemPack.CouponSystem;
import customer.pkg.Customer;
import customer.pkg.CustomerSQLException;
import facade.pkg.AdminFacade;
import facade.pkg.ClientType;
import facade.pkg.CompanyFacade;
import facade.pkg.CustomerFacade;
import facade.pkg.FacadeException;
import thread.pkg.DailyThread;

public class Main {

	public static void main(String[] args) throws InterruptedException, CoupSQLException{

//		DailyThread dt = new DailyThread();
//		Thread dailyThread = new Thread(dt, "DailyExpirationThread");
//		dailyThread.start();
//		dt.stopTask();
		
		CouponSystem cs = new CouponSystem();
			try {
				//Admin methods
				AdminFacade admin = (AdminFacade) cs.login("admin", "1234", ClientType.ADMIN);
				Company company = new Company();
				company.setCompName("kanye");
				company.setPassword("west");
				company.setEmail("SG@ACDC.com");
				company.setId(admin.getCompanyID(company));

         		admin.createCompany(company);
//				admin.updateCompany(company);
//				admin.getAllCompanies();
//				admin.getAllCustomers();
//				admin.removeCompany(company);
				
				Customer customer = new Customer();
				customer.setCust_name("Kim");
				customer.setPassword("Kardashian");
				customer.setId(admin.getCustomerID(customer));
				admin.createCustomer(customer);
//				admin.updateCustomer(customer);
//				admin.removeCustomer(customer);
//				admin.getAllCustomers();
				
				long compid = admin.getCompanyID(company);
				
				CompanyFacade cf = (CompanyFacade) cs.login("kanye", "west", ClientType.COMPANY);
				Coupon coupon = new Coupon();
				coupon.setTitle("Mark Knofler");
				coupon.setStartDate(coupon.updateDate(2015, 11, 25));
				coupon.setEndDate(coupon.updateDate(2016, 12, 12));
				coupon.setAmount(100);
				coupon.setImage("Layla");
				coupon.setMessage("DreamTheater");
				coupon.setPrice(99.99);
				coupon.setType(CouponType.OTHER);
				coupon.setId(cf.getCouponID(coupon));
								
//				cf.createCoupon(coupon);
//				cf.updateCoupon(coupon);
//				cf.removeCoupon(coupon);
				cf.getAllCoupons();
//				cf.getCouponByType(CouponType.OTHER);
				
				CustomerFacade customerFac = (CustomerFacade) cs.login("Kim", "Kardashian", ClientType.CUSTOMER);
//				customerFac.purchaseCoupon(coupon);
//				System.out.println(customerFac.getCustName());
				customerFac.getAllPurchasedCoupons();	
//				System.out.println(customerFac.getAllPurchasedCouponsByPrice(300));
//				System.out.println(customerFac.getAllPurchasedCouponsByType(CouponType.HEALTH));
//				System.out.println(customerFac.getAllCoupons());
		
				
			} catch (FacadeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CustomerSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CompanySQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}finally {
				CouponSystem.shutdown();
//				dt.stopTask();
			}
			
		}
}