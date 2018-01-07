package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import Core.Bean.Coupon;
import Core.Bean.Customer;
import Core.PoolConnection.ConnectionPool;
import DAO.CustomerDAO;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.ServerCantLoadException;
import SystemExceptionHandling.WorrgPasswordException;

public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool pool = null;
	private Connection con = null;
	PreparedStatement preStatement = null;

	public CustomerDBDAO() throws ServerCantLoadException, DataBaseIsCloseException {
		super();
		pool = ConnectionPool.getInstance();
	}

	@Override
	public void createCustomer(Customer cust)
			throws CreateBeanException, DataBaseIsCloseException, DatabaseAtMaxCapacityException {

		con = pool.getConnection();
		String sql = "INSERT INTO customer VALUES (?,?,?)";
		PreparedStatement preStatement = null;

		try {

			preStatement = con.prepareStatement(sql);

			preStatement.setLong(1, cust.getId());
			preStatement.setString(2, cust.getCusName());
			preStatement.setString(3, cust.getPassword());
			preStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CreateBeanException();
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void removeCustumer(Customer cust)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException {

		con = pool.getConnection();

		if (con != null) {
			try {

				Statement stm = con.createStatement();

				String sql = "DELETE FROM Customer WHERE id =" + cust.getId();
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
	public void updateCustomer(Customer cust)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException {

		con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();

				String sql = "UPDATE Customer SET Id =" + cust.getId() + ", password = '" + cust.getPassword()
						+ "' WHERE cust_name = '" + cust.getCusName() + "'";
				stm.executeUpdate(sql);
				
			} catch (SQLException e) {
				throw new CantUpdateException();
			} finally {
				pool.returnConnection(con);
			}
		}

	}

	@Override
	public Customer getCustomer(long id)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CouldntGetFromDataBase {

		try {
			con = pool.getConnection();

			if (con != null) {

				Customer cust = new Customer();
				Statement stm = con.createStatement();
				ResultSet result;

				result = stm.executeQuery("SELECT * FROM customer");

				while (result.next()) {
					if (result != null && result.getLong("id") == id) {
						cust.setCusName(result.getString("cust_name"));
						cust.setPassword(result.getString("password"));
						cust.setId(id);
						return cust;
					}
				}
			}
		} catch (SQLException e) {
			throw new CouldntGetFromDataBase();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
		throw new CouldntGetFromDataBase();
	}


	@Override
	public Collection<Customer> getAllCustomers() throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, WorrgPasswordException  {

		Collection<Customer> compCollection = new HashSet<>();

		con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM customer");

				while (result.next() == true) {
					if (result != null) {

						Customer cust = new Customer();

						cust.setCusName(result.getString("cust_name"));
						cust.setPassword(result.getString("password"));
						cust.setId(result.getLong("id"));

						compCollection.add(cust);
					}

				}

			} catch (SQLException e) {
				throw new WorrgPasswordException();
			} finally {
				pool.returnConnection(con);
			}
		}
		return compCollection;

	}

	@Override
	public Collection<Coupon> getCoupons() throws GeneralCouponSystemException {

		Collection<Coupon> coupCollection = new HashSet<>();

		con = pool.getConnection();

		if (con != null) {
			CouponDBDAO DAO = new CouponDBDAO();
			coupCollection = DAO.getAllCoupons();

		} else {
			throw new CouldntGetFromDataBase();
		}
		return coupCollection;
	}

	@Override
	public boolean login(String custName, String password)

			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, WorrgPasswordException {

		con = pool.getConnection();
		try {
			String query = "SELECT DISTINCT cust_name, password FROM customer";
			Statement statement = con.createStatement();
			ResultSet set = statement.executeQuery(query);

			while (set.next()) {
				if (custName.equals(set.getString("cust_name")) && password.equals(set.getString("password"))) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new WorrgPasswordException();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

		return false;
	}

}
