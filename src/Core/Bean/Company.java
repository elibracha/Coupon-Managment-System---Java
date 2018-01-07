package Core.Bean;

/**
 * 
 * Simple Bean named Company (used to pull and put data in the database)
 * 
 * 
 */

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Company {

	private long id;
	private String compName;
	private String password;
	private String email;
	private Collection<Coupon> coupons;

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

		 Collection<Coupon> copyCoupons = new HashSet<>();
		 Iterator<Coupon> it = coupons.iterator();
		 
		 while (it.hasNext()) {	
			 copyCoupons.add(it.next());
		 }
		 this.coupons = copyCoupons;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email
				+ ", coupons=" + coupons + "]";
	}
	
	

}
