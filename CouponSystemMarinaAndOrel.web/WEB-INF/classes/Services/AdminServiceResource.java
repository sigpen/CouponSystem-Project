package Services;

import java.util.Collection;
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
import company.pkg.Company;
import company.pkg.CompanySQLException;
import coupon.pkg.CoupSQLException;
import couponSystemPack.CouponSystem;
import customer.pkg.Customer;
import customer.pkg.CustomerSQLException;
import facade.pkg.AdminFacade;
import facade.pkg.ClientType;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminServiceResource {

	public static final String ADMIN_FACADE_ATTR = "admin";
	

	public AdminServiceResource() {

	}
	
	
	@GET
	@Path("/login/{username}/{password}")
	public Message login(@PathParam("username") String username, @PathParam("password") String password,@Context HttpServletRequest request) throws CouponSystemException  {
		
		Message message = new Message();
		
			HttpSession session;
		
		try {
			

			AdminFacade af = (AdminFacade) CouponSystem.getInstance().login(username, password, ClientType.ADMIN);
			
			session = request.getSession(true);
			
			if(af != null){
				
				session.setAttribute(ADMIN_FACADE_ATTR, af);
	  
				message.setMessage("Login successful , Welcome administrator");
				
			}else{
				
				throw new CouponSystemException("login failed ,Please try again");
				
			}
			
		} catch (Exception e) {
			
			throw new CouponSystemException("login failed ,Please try again");
		}	
			
			return message;
		
	}
	

	
	private AdminFacade getAdminFacade(HttpServletRequest request) {

		return (AdminFacade)request.getSession(false).getAttribute(ADMIN_FACADE_ATTR);

	}
	
	// Company //

	@POST
	@Path("/createCompany")
	public Message createCompany(Company company, @Context HttpServletRequest request) throws CouponSystemException{

		AdminFacade adminfacade = getAdminFacade(request);
		Message message = new Message();
		
	     try {
	    	 System.out.println(company.getCompName());
			adminfacade.createCompany(company);
			message.setMessage("create Company successful: "+ company.getId() + ",CompName: " + company.getCompName() + ",password: "+ company.getPassword()+ ",email: " +company.getEmail());
			System.out.println(message.getMessage());
		} catch (CompanySQLException e) {
	
			message.setMessage(e.getMessage());
			
		}
	     
			return message;


	     

	}

	@DELETE
	@Path("/removeCompany/{id}")
	public Message removeCompany(@PathParam ("id")long companyId, @Context HttpServletRequest request) throws CouponSystemException, CompanySQLException {

		AdminFacade adminfacade = getAdminFacade(request);
		Message message = new Message();

		try {
			
			Company c =new Company();
			c.setId(companyId);
			adminfacade.removeCompany(c);
			message.setMessage("remove Company successful : " + companyId);
			System.out.println(companyId + "was removed");

		} catch (CompanySQLException | CoupSQLException e) {

			message.setMessage(e.getMessage());
		}
		
		return message;

	}
	
	
	@PUT
	@Path("/updateCompany")
	public Message updateCompany(Company company, @Context HttpServletRequest request) throws CouponSystemException {
		
		AdminFacade adminfacade = getAdminFacade(request);
		Message message = new Message();
		
		try {
			
			adminfacade.getCompanyID(company);
			adminfacade.updateCompany(company);
			message.setMessage("update Company successful : " + company.getCompName() + ",password: "+ company.getPassword()+ ",email: " +company.getEmail());
			
		} catch (CompanySQLException e) {
			
			message.setMessage(e.getMessage());
		}
		
		return message;

	}

	@GET
	@Path("/getCompany/{id}")
	public Company getCompany(@PathParam("id")long id ,@Context HttpServletRequest request) throws CouponSystemException{
		
		AdminFacade adminfacade = getAdminFacade(request);
		
		try {
			
			return adminfacade.getCompany(id);
			
			
		} catch (Exception e) {

			
			throw new CouponSystemException("don't have Company");
		}
		
		
	}


	@GET
	@Path("/getAllCompanies")
	public Collection<Company> getAllCompanies(@Context HttpServletRequest request) throws CouponSystemException {
		
		AdminFacade adminfacade = getAdminFacade(request);
		
		try {
			
			return adminfacade.getAllCompanies();
			
		} catch (Exception e) {

			throw new CouponSystemException("don't have Companies");
		}
		
		

	}

	
	
	// Customer //
	
	@POST
	@Path("/createCustomer")
	public Message createCustomer(Customer customer, @Context HttpServletRequest request) throws CouponSystemException {

		AdminFacade adminfacade = getAdminFacade(request);
		Message message = new Message();

		try {
			
			adminfacade.createCustomer(customer);
			message.setMessage("create Customer successful : "+ customer.getId() + ",CustName: "  + customer.getCust_name() + ",password: "+ customer.getPassword());
			
		} catch (CustomerSQLException e) {
			
			message.setMessage(e.getMessage());
		}

		return message;
	}

	@DELETE
	@Path("/removeCustomer/{id}")
	public Message removeCustomer(@PathParam ("id")long customerId, @Context HttpServletRequest request) throws CouponSystemException, CustomerSQLException {

		AdminFacade adminfacade = getAdminFacade(request);
		Message message = new Message();

		try {

			Customer c = new Customer();
			c.setId(customerId);
			adminfacade.removeCustomer(c);
			message.setMessage("remove Customer successful : " + customerId);
			System.out.println(customerId + "was removed");

		} catch (CustomerSQLException | CompanySQLException e) {
			
			message.setMessage(e.getMessage());
		}
		
		return message;

	}
	

	@PUT
	@Path("/updateCustomer")
	public Message updateCustomer(Customer customer, @Context HttpServletRequest request) throws CouponSystemException {
		
		AdminFacade adminfacade = getAdminFacade(request);
		Message message = new Message();

		try {
			
			adminfacade.getCustomerID(customer);
			adminfacade.updateCustomer(customer);
			message.setMessage("update Customer successful : " + customer.getCust_name() + ", password: "+ customer.getPassword());
			
		} catch (CustomerSQLException e) {
			
			message.setMessage(e.getMessage());

		}
		
		return message;
		
	}

	@GET
	@Path("/getCustomer/{id}")
	public Customer getCustomer(@PathParam("id")long id,@Context HttpServletRequest request) throws CouponSystemException {
		
		AdminFacade adminfacade = getAdminFacade(request);
		
		try {
			
			return adminfacade.getCustomer(id);
			
		} catch (Exception e) {

			throw new CouponSystemException("don't have Customer");
		
		}

	}
	

	@GET
	@Path("/getAllCustomer")
	public Collection<Customer> getAllCustomer(@Context HttpServletRequest request) throws CouponSystemException {
		
		AdminFacade adminfacade = getAdminFacade(request);
		
		try {
			
			return adminfacade.getAllCustomers();
			
		} catch (Exception e) {

			throw new CouponSystemException("don't have Customers");
		
		}
		
	}

	
	@GET
	@Path("/logout")
	public Message logout(@Context HttpServletRequest request) throws CouponSystemException {
		
		Message message = new Message();

			
			try {
				
				if (request.getSession(false) == null) {

					throw new CouponSystemException("Admin logout failed because you are not logged in");
					
				}else if (request.getSession(false) != null) {
					
					request.getSession(false).invalidate();
					System.out.println("goodbye");
					
					message.setMessage("admin logout successful , goodbye");

				}
				
			} catch (CouponSystemException e) {

				e.printStackTrace();
			}
			
			return message;
			

	}


}
