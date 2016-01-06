package Services;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import Beans.Message;
import ErrorException.CouponSystemException;
import coupon.pkg.Coupon;
import coupon.pkg.CouponType;
import couponSystemPack.CouponSystem;
import customer.pkg.CustomerSQLException;
import facade.pkg.ClientType;
import facade.pkg.CompanyFacade;
import facade.pkg.CustomerFacade;
import facade.pkg.FacadeException;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerServiceResource {

	public static final String CUSTOMER_FACADE_ATTR = "customer";

	public CustomerServiceResource() {

	}

	@GET
	@Path("/login/{username}/{password}")
	public Message login(@PathParam("username") String username, @PathParam("password") String password,@Context HttpServletRequest request) throws FacadeException, CustomerSQLException{

		Message message = new Message();

		HttpSession session;

		CustomerFacade cf;
		try {
			cf = (CustomerFacade) CouponSystem.getInstance().login(username, password, ClientType.CUSTOMER);

		session = request.getSession(true);

		if (cf != null) {

			session.setAttribute(CUSTOMER_FACADE_ATTR, cf);

			message.setMessage("Login successful , Welcome customer");

		} else {
			
			throw new FacadeException("login failed ,Please try again");
		}
		
		}catch (FacadeException e) {

			throw new FacadeException("login failed ,Please try again");
		}

		return message;

	}

	private CustomerFacade getCustomerFacade(HttpServletRequest request) {

		return (CustomerFacade) request.getSession(false).getAttribute(CUSTOMER_FACADE_ATTR);

	}
	
	// Coupon //

	@POST
	@Path("/purchaseCoupon")
	public Message purchaseCoupon(Coupon coupon, @Context HttpServletRequest request) throws CouponSystemException {

		CustomerFacade customerfacade = getCustomerFacade(request);
		Message message = new Message();
		
		try {
			
			customerfacade.purchaseCoupon(coupon);
			CompanyFacade cf = new CompanyFacade();
			Coupon c = cf.getCoupon(coupon.getId());
			message.setMessage("purchase Coupon successful : " + c.getId() + ",title: " + c.getTitle()+ ",message: "+ c.getMessage() + ",image: "+ c.getImage() + ",startDate: "+ c.getStartDate() + ",endDate: "+ c.getEndDate() + ",amount: "+ c.getAmount() + ", type: "+ c.getType() + ", price: "+ c.getPrice());
			System.out.println(coupon.toString());
		} catch (Exception e) {
			
			message.setMessage(e.getMessage());

		}

		return message;

	}

	@GET
	@Path("/getAllPurchasedCoupons")
	public Collection<Coupon> getAllPurchasedCoupons(@Context HttpServletRequest request) {

		CustomerFacade customerfacade = getCustomerFacade(request);

		try {
			
			return customerfacade.getAllPurchasedCoupons();


		} catch (Exception e) {
			
			e.printStackTrace();

		}

		return null;

	}

	@GET
	@Path("/getAllPurchasedCouponsByType/{type}")
	public Collection<Coupon> getAllPurchasedCouponsByType(@PathParam("type")String type, @Context HttpServletRequest request) {

		CustomerFacade customerfacade = getCustomerFacade(request);
		
		ArrayList<Coupon> couponlistByType = new ArrayList<>();
		
		CouponType CouponsByType = CouponType.valueOf(type);

		try {
			
			couponlistByType = (ArrayList<Coupon>) customerfacade. getAllPurchasedCouponsByType(CouponsByType);
			System.out.println(CouponsByType.name());

		} catch (Exception e) {

			e.printStackTrace();

		}

		return couponlistByType;

	}

	@GET
	@Path("/getAllPurchasedCouponsByPrice/{price}")
	public Collection<Coupon> getAllPurchasedCouponsByPrice(@PathParam("price")double price, @Context HttpServletRequest request) {

		CustomerFacade customerfacade = getCustomerFacade(request);

		ArrayList<Coupon> CouponslistByPrice = new ArrayList<>();
		
		CouponType[] CouponsByPrice = CouponType.values();

		try {

			CouponslistByPrice = (ArrayList<Coupon>) customerfacade. getAllPurchasedCouponsByPrice(price);
			System.out.println(CouponsByPrice.length);

		} catch (Exception e) {

			return null;

		}

		return CouponslistByPrice;

	}

	@GET
	@Path("/logout")
	public Message logout(@Context HttpServletRequest request) {
		
		Message message = new Message();

		try {
			if (request.getSession(false) == null) {

				throw new CouponSystemException("You are not logged in");

			} else if (request.getSession(false) != null) {

				request.getSession(false).invalidate();
				System.out.println("logged out");
				
				message.setMessage("customer logout successful , goodbye");

			}

		} catch (CouponSystemException e) {

			e.printStackTrace();
		}
		
		return message;

	}
	
}
