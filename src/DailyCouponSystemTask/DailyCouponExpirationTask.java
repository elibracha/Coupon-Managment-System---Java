package DailyCouponSystemTask;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Core.Bean.Coupon;
import Core.DataBase.DBStartUp;
import DBDAO.CompanyCouponDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDAO;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.ServerCantLoadException;


public class DailyCouponExpirationTask implements Runnable{
	
	private static DailyCouponExpirationTask task = new  DailyCouponExpirationTask();
	private CouponDBDAO Coupons;
	private CompanyCouponDAO companyCoupon;
	private CustomerCouponDAO customerCoupon;
	private boolean quit;
	
	private DailyCouponExpirationTask(){
		
		try {
			Coupons = new CouponDBDAO();
			companyCoupon = new CompanyCouponDAO();
			customerCoupon = new CustomerCouponDAO();
			this.quit = false;
		} catch (ServerCantLoadException | DataBaseIsCloseException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public static DailyCouponExpirationTask getTask(){
		return task;
	}

	@Override
	public void run(){
		
		String url = DBStartUp.URL;
		
		
		try {
			Connection con = DriverManager.getConnection(url);
			Statement stm = con.createStatement();
			ResultSet set = stm.executeQuery("SELECT * FROM coupon");
			while (quit!=true){
				if (set.next()){
					Date d1 = new Date(System.currentTimeMillis());
					Date d2 = set.getDate("end_date");
					if(d1.after(d2)){
						Coupon coupon = new Coupon();
						coupon.setId(set.getLong("id"));
						try {
							
							Coupons.removeCoupon(coupon);
							companyCoupon.removeCompanyCoupon(coupon);
							customerCoupon.removeCustomerCoupon(coupon);
							
						} catch (DataBaseIsCloseException | DatabaseAtMaxCapacityException | CantUpdateException | ServerCantLoadException e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
		}catch (SQLException e){
			try {
				throw new GeneralCouponSystemException();
			} catch (GeneralCouponSystemException e1) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void StopTask(){
		this.quit = false;
	}
	
	

}
