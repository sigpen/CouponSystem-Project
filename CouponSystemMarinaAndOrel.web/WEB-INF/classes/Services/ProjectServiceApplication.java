package Services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import ErrorException.ServiceErrorProvider;


@ApplicationPath("/rest")
public class ProjectServiceApplication extends Application {
	

	static{
		
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(new AdminServiceResource());
		singletons.add(new CompanyServiceResource());
		singletons.add(new CustomerServiceResource());
		singletons.add(new ServiceErrorProvider());
		return singletons;
	}
}
