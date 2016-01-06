package coupon.pkg;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Coupon {
	
	private long id;
	private String title;
	private String message;
	private String image;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private double price;


	public Coupon(long id) {
		super();
		this.id = id;
	}

	public Coupon() {
		super();
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public CouponType getType() {
		return type ;
	}
	public void setType(CouponType type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public java.sql.Date updateDate(int y, int m, int d){
		Calendar calendar = new GregorianCalendar();

		calendar.set(Calendar.YEAR, y);
		calendar.set(Calendar.MONTH, m-1); 
		calendar.set(Calendar.DAY_OF_MONTH, d); 
		
		Date date = new Date(calendar.getTime().getTime());
		return date;
		
	}
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + ", message=" + message + ", image=" + image + ", startDate="
				+ startDate + ", endDate=" + endDate + ", amount=" + amount + ", type=" + type + ", price=" + price
				+ "]";
	}
	
	 

}
