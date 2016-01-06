package ErrorException;

public class ServiceError {

	private String error;

	public ServiceError() {
	}

	public ServiceError(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "PhonebookError [error=" + error + "]";
	}

}
