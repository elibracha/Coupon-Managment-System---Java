package Core.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import SystemExceptionHandling.CreateTableException;
import SystemExceptionHandling.DriverCantLoadException;

/*
 * 
 * 
 * 
 * 
 * 
 * This class has one method. with creates 5 tables in the "apache derby" database.
 * Company TABLE - id long (primary key), comany_name String,  password String, Email String.
 * Customer TABLE - id long (primary key), customer_name String, password String.
 * Coupon TABLE - id long (primary key), title String, start_datee Date, end_date Date, amount int, typr (enum) String, message String,price double,image String.
 * Company_Coupon TABLE - company_id long(primary key), coupon_id long(primary key).
 * CustomerCoupon TABLE -  Customer_id long(primary key),  coupon_id long(primary key).
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class DBStartUp {
	
	/*
	 * 
	 * 
	 * class instances :
	 * 		connection - null.
	 * 		Statement - null.
	 * 		
	 * final public String Driver - Database client Driver address.
	 * final public String URL - Tables address.
	 * 
	 * 
	 * 
	 */
	
	private static Connection conection = null;
	private static Statement statement = null;
	
	public static final String Driver = "org.apache.derby.jdbc";
	public static final String URL = "jdbc:derby://localhost:1527/CouponSystem; create=true;";

	/*
	 * 
	 * 
	 * 
	 * creates a database called DerbyCoupons and, Create five tables.
	 * 
	 * 
	 * 
	 */
	
	

	public static void createDatebase()
			throws DriverCantLoadException, CreateTableException {

		try {

			// load Driver up to memory
			//Class.forName(Driver);

			conection = DriverManager.getConnection(URL);
			statement = conection.createStatement();

			// auto commit off
			conection.setAutoCommit(false);

			String sql = "CREATE TABLE Company(id BIGINT PRIMARY KEY, comp_name VARCHAR(50) UNIQUE, password VARCHAR(50), email VARCHAR (50))";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE Customer (id BIGINT PRIMARY KEY, cust_name VARCHAR(50) UNIQUE, password VARCHAR(50))";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE Coupon (id BIGINT PRIMARY KEY,title VARCHAR(50),start_date DATE,end_date DATE, amount INTEGER,type VARCHAR(25), message VARCHAR(50),"
					+ "price FLOAT, image VARCHAR(100))";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE Company_Coupon (Company_id BIGINT,Coupons_id BIGINT, PRIMARY KEY(Company_id,Coupons_id))";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE Customer_Coupon (Customer_id BIGINT,Coupons_id BIGINT, PRIMARY KEY(Customer_id,Coupons_id))";
			statement.executeUpdate(sql);

			conection.commit();
			
		} catch (SQLException e) {
			CreateTableException TableProblem = new CreateTableException();
			throw TableProblem;

		}

	}

}
