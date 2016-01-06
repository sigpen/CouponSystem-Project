package couponSystemPack;

import company.pkg.ConnectionPool;
import customer.pkg.CustomerSQLException;
import facade.pkg.AdminFacade;
import facade.pkg.ClientType;
import facade.pkg.CompanyFacade;
import facade.pkg.CouponClientFacade;
import facade.pkg.CustomerFacade;
import facade.pkg.FacadeException;


public class CouponSystem {

	private static CouponSystem instance;

	public static CouponSystem getInstance() {
		if (instance == null){
			instance = new CouponSystem();
			}
		return instance;
	}
	

	public static void shutdown(){
		ConnectionPool.getInstance().closeAllCons();
		
	}
	//Classifying every login to it's correct Facade
	public CouponClientFacade login(String user, String password, ClientType type) throws FacadeException, CustomerSQLException {
		if (type.equals(ClientType.COMPANY)){
			CompanyFacade cf = new CompanyFacade(user, password);
			cf.login(user, password, type);
			return cf;
		}
		if (type.equals(ClientType.ADMIN)){
			AdminFacade af = new AdminFacade(user, password);
			af.login(user, password, type);
			return af;
		}
		if (type.equals(ClientType.CUSTOMER)){
			CustomerFacade cf = new CustomerFacade(user, password);
			cf.login(user, password, type);
			return cf;
		}
		else {
		throw new FacadeException("Login failed.");
		}
	}
	//Verifying the existence of the Enum
	public static ClientType enumCheck(String type) {
	    if (type != null) {
	      for (ClientType c : ClientType.values()) {
	        if (type.equalsIgnoreCase(c.name())) {
	          return c;
	        }
	      }
	    }
	    return null;	
}
}
