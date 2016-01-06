
	package company.pkg;



	import java.util.ArrayList;
	import java.util.Collection;

import coupon.pkg.Coupon;

	public class Company {

		private long id;
		private String compName;
		private String password;
		private String email;
		private Collection<Coupon> coupons = new ArrayList<>();
		
		
		public Company(String compName, String password) {
			super();
			this.compName = compName;
			this.password = password;
		}
		@Override
		public String toString() {
			return "Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email
					+ ", coupons=" + coupons + "]";
		}
		public Company() {
			super();
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getCompName() {
			return compName;
		}
		public void setCompName(String compName) {
			this.compName = compName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Collection<Coupon> getCoupons() {
			return coupons;
		}
		public void setCoupons(Collection<Coupon> coupons) {
			this.coupons = coupons;
		}
	}


