package Core.Bean;

/**
 * 
 * Simple Bean named Customer (used to pull and put data in the database)
 * 
 * 
 */

import java.util.Collection;

public class Customer {
	
	private long id;
	private String cusName;
	private String password;
	private Collection<Coupon> coupons;
	
	
	public Customer() {
		super();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Collection<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", cusName=" + cusName + ", password=" + password + ", coupons=" + coupons + "]";
	}
	
	
}
