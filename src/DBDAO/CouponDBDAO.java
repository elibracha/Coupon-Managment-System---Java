package DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import Core.Bean.Coupon;
import Core.Bean.CouponType;
import Core.PoolConnection.ConnectionPool;
import DAO.CouponDAO;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.ServerCantLoadException;

public class CouponDBDAO implements CouponDAO {

	private ConnectionPool pool = null;
	private Connection con = null;
	PreparedStatement preStatement = null;

	public CouponDBDAO() throws ServerCantLoadException, DataBaseIsCloseException {
		super();
		pool = ConnectionPool.getInstance();
	}

	@Override
	public void createCoupon(Coupon coupon)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CreateBeanException {

		con = pool.getConnection();
		try {

			if (con != null) {

				String sql2 = "INSERT INTO coupon VALUES (?,?,?,?,?,?,?,?,?)";
				preStatement = con.prepareStatement(sql2);

				preStatement.setLong(1, coupon.getId());
				preStatement.setString(2, coupon.getTitle());
				java.sql.Date startDate = new java.sql.Date(coupon.getStartDate().getTime());
				preStatement.setDate(3, startDate);
				java.sql.Date endDate = new java.sql.Date(coupon.getEndDate().getTime());
				preStatement.setDate(4, endDate);
				preStatement.setInt(5, coupon.getAmount());
				preStatement.setString(6, coupon.getType().name());
				preStatement.setString(7, coupon.getMessage());
				preStatement.setDouble(8, coupon.getPrice());
				preStatement.setString(9, coupon.getImage());
				preStatement.executeUpdate();
			}

		} catch (SQLException e) {
			throw new CreateBeanException();
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void removeCoupon(Coupon coupon)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException {

		con = pool.getConnection();

		if (con != null) {
			try {

				Statement stm = con.createStatement();

				String sql = "DELETE FROM coupon WHERE id =" + coupon.getId();
				stm.executeUpdate(sql);

			} catch (SQLException e) {
				throw new CantUpdateException();
			} finally {
				if (con != null)
					pool.returnConnection(con);
			}
		}

	}

	@Override
	public void updateCoupon(Coupon coupon)
			throws CantUpdateException, DataBaseIsCloseException, DatabaseAtMaxCapacityException {

		try {

			con = pool.getConnection();
			
			if (con != null && coupon != null) {

				Statement stm = con.createStatement();

				Date startDate = new Date(coupon.getStartDate().getTime());
				Date endDate = new Date(coupon.getEndDate().getTime());

				String sql = "UPDATE Coupon SET TITLE = '" + coupon.getTitle() + "',START_DATE = '" + startDate
						+ "',END_DATE = '" + endDate + "', AMOUNT = " + coupon.getAmount() + ", " + " TYPE = '"
						+ coupon.getType().name() + "',MESSAGE = '" + coupon.getMessage() + "',PRICE = "
						+ coupon.getPrice() + ",IMAGE = '" + coupon.getImage() + "' WHERE id = " + coupon.getId();

				stm.executeUpdate(sql);
			}

		} catch (SQLException e) {
			throw new CantUpdateException();
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Coupon getCoupon(long id)
			throws CouldntGetFromDataBase, DataBaseIsCloseException, DatabaseAtMaxCapacityException {

		try {
			con = pool.getConnection();

			if (con != null) {

				Coupon coupon = new Coupon();
				Statement stm = con.createStatement();
				ResultSet result;

				result = stm.executeQuery("SELECT * FROM coupon");

				while (result.next() == true) {
					if (result != null && result.getLong("id") == id) {
						coupon.setAmount(result.getInt("amount"));
						coupon.setTitle(result.getString("title"));
						coupon.setStartDate(result.getDate("start_date"));
						coupon.setEndDate(result.getDate("end_date"));
						coupon.setType(CouponType.valueOf(result.getString("type")));
						coupon.setMessage(result.getString("amount"));
						coupon.setPrice(result.getDouble("price"));
						coupon.setImage(result.getString("amount"));
						coupon.setId(id);
						return coupon;
					}
				}
			}
		} catch (SQLException e) {
			CouldntGetFromDataBase getFail = new CouldntGetFromDataBase();
			throw getFail;
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

		throw new CouldntGetFromDataBase();
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws GeneralCouponSystemException {

		Collection<Coupon> coupCollection = new HashSet<>();

		con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM coupon");

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

	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws GeneralCouponSystemException {

		Collection<Coupon> coupCollection = new HashSet<>();

		con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM coupon");

				while (result.next() == true)

					if (type.name().equals(result.getString(6))) {

						Coupon coupon = new Coupon();

						coupon.setAmount(result.getInt("amount"));
						coupon.setTitle(result.getString("title"));
						coupon.setStartDate(result.getDate("start_date"));
						coupon.setEndDate(result.getDate("end_date"));
						coupon.setType(CouponType.valueOf(result.getString(6)));
						coupon.setMessage(result.getString("amount"));
						coupon.setPrice(result.getDouble("price"));
						coupon.setImage(result.getString("amount"));
						coupon.setId(result.getLong("id"));

						coupCollection.add(coupon);
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
