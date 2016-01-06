package customer.pkg;

public class Customer {

	private long id;
	private String cust_name;
	private String password;
	
	public Customer() {
		super();
	}
	public Customer(String cust_name, String password) {
		super();
		this.cust_name = cust_name;
		this.password = password;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", cust_name=" + cust_name + ", password=" + password + "]";
	}
	
	
}
