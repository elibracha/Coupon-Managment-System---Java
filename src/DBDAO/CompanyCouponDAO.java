package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import Core.Bean.Company;
import Core.Bean.Coupon;
import Core.Bean.CouponType;
import Core.PoolConnection.ConnectionPool;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.ServerCantLoadException;

public class CompanyCouponDAO {

	private ConnectionPool pool;

	public CompanyCouponDAO() throws ServerCantLoadException, DataBaseIsCloseException {
		super();
		pool = ConnectionPool.getInstance();
	}

	public void createCouponComapany(Company comp) throws ServerCantLoadException, DataBaseIsCloseException,
			DatabaseAtMaxCapacityException, CreateBeanException {

		Connection con = pool.getConnection();
		PreparedStatement preStatement;
		try {
			preStatement = con.prepareStatement("INSERT INTO company_coupon VALUES (?,?)");
			Collection<Coupon> couponCol = comp.getCoupons();
			if (couponCol != null) {
				Iterator<Coupon> it = couponCol.iterator();
				while (it.hasNext()) {
					Coupon currentCoupon = it.next();
					preStatement.setLong(1, comp.getId());
					preStatement.setLong(2, currentCoupon.getId());
					preStatement.execute();
				}

			}
		} catch (SQLException e) {
			throw new CreateBeanException();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	public void createCouponComapany(Coupon coup, long id) throws ServerCantLoadException, DataBaseIsCloseException,
			DatabaseAtMaxCapacityException, CreateBeanException {

		Connection con = pool.getConnection();
		PreparedStatement preStatement;
		
		try {
			preStatement = con.prepareStatement("INSERT INTO company_coupon VALUES (?,?)");
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

	public void removeCompanyCoupon(Coupon coupon) throws ServerCantLoadException, DataBaseIsCloseException,
			DatabaseAtMaxCapacityException, CantUpdateException {
		Connection con = pool.getConnection();
		Statement stm;
		try {
			stm = con.createStatement();
			String sql = "DELETE FROM company_coupon WHERE coupons_id =" + coupon.getId();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			throw new CantUpdateException();
		}
	}


	public Collection<Coupon> getAllCouponsByCompany(long id) throws GeneralCouponSystemException {

		Collection<Coupon> coupCollection = new HashSet<>();

		Connection con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM coupon INNER JOIN company_coupon ON coupon.id ="
						+ " company_coupon.coupons_id WHERE company_id = " + id);

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
				throw new GeneralCouponSystemException();
			} finally {
				pool.returnConnection(con);
			}
		}

		return coupCollection;

	}
}
