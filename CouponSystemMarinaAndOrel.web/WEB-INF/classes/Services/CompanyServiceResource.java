package Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import Beans.Message;
import ErrorException.CouponSystemException;
import company.pkg.CompanySQLException;
import coupon.pkg.CoupSQLException;
import coupon.pkg.Coupon;
import coupon.pkg.CouponType;
import couponSystemPack.CouponSystem;
import customer.pkg.CustomerSQLException;
import facade.pkg.ClientType;
import facade.pkg.CompanyFacade;
import facade.pkg.FacadeException;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyServiceResource {

	public static final String COMPANY_FACADE_ATTR = "companyfacade";

	public CompanyServiceResource() {
	}

	@GET
	@Path("/login/{username}/{password}")
	public Message login(@PathParam("username") String user, @PathParam("password") String password,@Context HttpServletRequest request) throws FacadeException  {
			
		Message message = new Message();
		HttpSession session;
		
		try {
			CompanyFacade cf = (CompanyFacade) CouponSystem.getInstance().login(user, password, ClientType.COMPANY);
			session = request.getSession(false);
			
			if (cf != null) {
				session.setAttribute(COMPANY_FACADE_ATTR, cf);
				message.setMessage("welcome");
			} else {
				
				throw new FacadeException("Company login failed");
			}
		} catch (FacadeException e) {

			throw new FacadeException("Company login failed");
		} catch (CustomerSQLException e) {

			e.printStackTrace();
		}

		return message;

	}

	private CompanyFacade getCompanyFacade(HttpServletRequest request) {
		return (CompanyFacade) request.getSession(false).getAttribute(COMPANY_FACADE_ATTR);
	}

	@POST
	@Path("/createCoupon")
	public Message createCoupon(Coupon c, @Context HttpServletRequest request) throws CouponSystemException {
		
		CompanyFacade cf = getCompanyFacade(request);
		Message message = new Message();
		
		try {
			
			cf.createCoupon(c);
			message.setMessage("create Coupon successful : " + c.getId() + ",title: " +  c.getTitle()+ ",message: "+ c.getMessage() + ",image: "+ c.getImage() + ",startDate: "+ c.getStartDate() + ",endDate: "+ c.getEndDate() + ",amount: "+ c.getAmount() + ", type: "+ c.getType() + ", price: "+ c.getPrice());
			System.out.println("create coupon");
			
		} catch (CompanySQLException e) {
			
			message.setMessage(e.getMessage());
		}
		
		return message;
	}

	@DELETE
	@Path("/removeCoupon/{id}")
	public Message removeCoupon(@PathParam("id") long couponId, @Context HttpServletRequest request) throws CouponSystemException, CoupSQLException, CompanySQLException {
		
		CompanyFacade cf = getCompanyFacade(request);
		Coupon c = cf.getCoupon(couponId);
		Message message = new Message();
		
		try {
			c.setId(couponId);
			long cId = cf.getCouponID(c);
			c.setId(cId);
			cf.removeCoupon(c);
			
			message.setMessage("remove Coupon successful : " + couponId );
			System.out.println(c.getTitle() + " was removed");
			
		} catch (CompanySQLException e) {
			
			message.setMessage(e.getMessage());
		} 
		
		return message;

	}

	@PUT
	@Path("/updateCoupon")
	public Message updateCoupon(Coupon c, @Context HttpServletRequest request) throws CouponSystemException, CoupSQLException {
		
		CompanyFacade cf = getCompanyFacade(request);
		Message message = new Message();
		
		try {
			c.setId(cf.getCouponID(c));
			cf.updateCoupon(c);
			message.setMessage("update Coupon successful : " + c.getTitle()+ ",message: "+ c.getMessage() + ",image: "+ c.getImage() + ",startDate: "+ c.getStartDate() + ",endDate: "+ c.getEndDate() + ",amount: "+ c.getAmount() + ", type: "+ c.getType() + ", price: "+ c.getPrice());
			
		} catch (CompanySQLException e) {
			
			message.setMessage(e.getMessage());

		}
		
		return message; 

	}

	@GET
	@Path("/getCoupon/{couponId}")
	public Coupon getCoupon(@PathParam("couponId") long id, @Context HttpServletRequest request) {
		CompanyFacade cf = getCompanyFacade(request);
		Coupon coupon = new Coupon();
		try {
			coupon = cf.getCoupon(id);
		} catch (CompanySQLException e) {
			// TODO Auto-generated catch block
		} catch (CoupSQLException e) {
			// TODO Auto-generated catch block
		}
		return coupon;
	}

	@GET
	@Path("/getAllCoupons")
	public Collection<Coupon> getAllCoupons(@Context HttpServletRequest request) throws CompanySQLException, CoupSQLException {
		CompanyFacade cf = getCompanyFacade(request);
		System.out.println(cf.getAllCoupons());
		
		try {
			ArrayList<Coupon> couponList = (ArrayList<Coupon>) cf.getAllCoupons();
			return couponList;
			
		} catch (CompanySQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoupSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@GET
	@Path("/getCouponByType/{type}")
	public Collection<Coupon> getCouponByType(@PathParam("type") String type, @Context HttpServletRequest request) {
		CompanyFacade cf = getCompanyFacade(request);
		List<Coupon> couponList = new ArrayList<>();
		CouponType couponType = CouponType.valueOf(type);
		try {
			couponList = (List<Coupon>) cf.getCouponByType(couponType);
			System.out.println(couponType.name());
		} catch (CompanySQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return couponList;
	}

	@GET
	@Path("/logout")
	public Message logout(@Context HttpServletRequest request) {
		
		Message message = new Message();

		try {
			if (request.getSession(false) == null) {
				
				throw new CouponSystemException("You are not logged in");

			} else {
				
				request.getSession(false).invalidate();
				System.out.println("logged out");
				
				message.setMessage("company logout successful , goodbye");
			}
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;

	}

}
