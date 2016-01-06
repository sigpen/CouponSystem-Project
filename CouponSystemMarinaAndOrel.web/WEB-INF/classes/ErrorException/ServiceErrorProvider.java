package ErrorException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServiceErrorProvider implements ExceptionMapper<Exception> {
	
	public Response toResponse(Exception e) {
		return Response.serverError()
				.entity(new ServiceError(e.getMessage()))
				.build();
	}

}
