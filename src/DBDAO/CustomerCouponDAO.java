package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import Core.Bean.Coupon;
import Core.Bean.CouponType;
import Core.Bean.Customer;
import Core.PoolConnection.ConnectionPool;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.ServerCantLoadException;

public class CustomerCouponDAO {

	private ConnectionPool pool;

	public CustomerCouponDAO() throws ServerCantLoadException, DataBaseIsCloseException {
		super();
		pool = ConnectionPool.getInstance();
	}

	public void removeCustomerCoupon(Coupon coupon)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException {

		Connection con = pool.getConnection();

		if (con != null) {
			Statement stm;
			try {

				stm = con.createStatement();
				String sql = "DELETE FROM customer_coupon WHERE coupons_id =" + coupon.getId();
				stm.executeUpdate(sql);

			} catch (SQLException e) {
				throw new CantUpdateException();
			}
		}

	}

	public void createCustomerCoupon(Customer cust) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException,
			ServerCantLoadException, CreateBeanException {

		Connection con = pool.getConnection();

		PreparedStatement preStatement;

		try {
			preStatement = con.prepareStatement("INSERT INTO customer_coupon VALUES (?,?)");
			Collection<Coupon> couponCol = cust.getCoupons();

			if (couponCol != null) {
				Iterator<Coupon> it = couponCol.iterator();
				while (it.hasNext()) {
					Coupon currentCoupon = it.next();
					preStatement.setLong(1, cust.getId());
					preStatement.setLong(2, currentCoupon.getId());
					preStatement.execute();
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CreateBeanException();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	public void createCouponCustomer(Coupon coup, long id) throws ServerCantLoadException, DataBaseIsCloseException,
			DatabaseAtMaxCapacityException, CreateBeanException {

		Connection con = pool.getConnection();
		PreparedStatement preStatement;

		try {
			preStatement = con.prepareStatement("INSERT INTO customer_coupon VALUES (?,?)");
			preStatement.setLong(1, id);
			preStatement.setLong(2, coup.getId());
			preStatement.execute();

		} catch (SQLException e) {
			throw new CreateBeanException();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}
	
	public Collection<Coupon> getAllCouponsByCustomer(long id) throws GeneralCouponSystemException  {

		Collection<Coupon> coupCollection = new HashSet<>();

		Connection con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM coupon INNER JOIN customer_coupon ON coupon.id ="
						+ " customer_coupon.coupons_id WHERE customer_ID = " + id);

				while (result.next() == true) {
					if (result != null) {

						Coupon coupon = new Coupon();

						coupon.setAmount(result.getInt("amount"));
						coupon.setTitle(result.getString("title"));
						coupon.setStartDate(result.getDate("start_date"));
						coupon.setEndDate(result.getDate("end_date"));
						coupon.setType(CouponType.valueOf(result.getString("type")));
						coupon.setMessage(result.getString("amount"));
						coupon.setPrice(result.getDouble("price"));
						coupon.setImage(result.getString("amount"));
						coupon.setId(result.getLong("id"));

						coupCollection.add(coupon);
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new GeneralCouponSystemException();
			} finally {
				pool.returnConnection(con);
			}
		}

		return coupCollection;

	}

}
