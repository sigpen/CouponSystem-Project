package facade.pkg;

public interface CouponClientFacade {
	
		public CouponClientFacade login(String user, String password, ClientType type) throws FacadeException;
}
