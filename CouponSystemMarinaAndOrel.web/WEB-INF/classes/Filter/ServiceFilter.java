package Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import Services.AdminServiceResource;
import Services.CompanyServiceResource;
import Services.CustomerServiceResource;

@WebFilter(urlPatterns = "/rest/*")
public class ServiceFilter implements Filter {

	public ServiceFilter() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String url = ((HttpServletRequest) request).getRequestURI();

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		System.out.println("url before: " + url);
		url = url.substring("/CouponSystem.web/rest".length());
		System.out.println("url after" + url);

		if (checkValidSession(req, url)) {

			System.out.println("checkValidSession");
			System.out.println("jsessionid=" + req.getSession().getId());
			chain.doFilter(request, response);

		} else {
			
			res.setStatus(500);
			res.getWriter().println("{\"error\":\"please your not login\"}");
			res.setContentType(MediaType.APPLICATION_JSON);

		}

	}

	private boolean checkValidSession(HttpServletRequest req , String url){
		
		HttpSession session =req.getSession(false);
		
		if(url.startsWith("/admin")){
			if(url.startsWith("/admin/login")) {
				System.out.println(" if");
				return true;
			}else if (session == null || session.getAttribute(AdminServiceResource.ADMIN_FACADE_ATTR)== null){
				System.out.println("else if");
				return false;
			}else{
				
				System.out.println("else");
				return true;
			}
		}
		
			
		if(url.startsWith("/company")){
			if(url.startsWith("/company/login")) {
				
				return true;
			}else if (session == null || session.getAttribute(CompanyServiceResource.COMPANY_FACADE_ATTR)== null){
				return false;
			}else{
				return true;
			}
		}

		
		if(url.startsWith("/customer")){
			if(url.startsWith("/customer/login")) {
				
				return true;
			}else if (session == null || session.getAttribute(CustomerServiceResource.CUSTOMER_FACADE_ATTR)== null){
				return false;
			}else{
				return true;
			}
		}
		
		return true;
			
		
		
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

	public void destroy() {
	}
}
