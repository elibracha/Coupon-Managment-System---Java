package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import Core.Bean.*;
import Core.PoolConnection.*;
import DAO.CompanyDAO;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.ServerCantLoadException;
import SystemExceptionHandling.WorrgPasswordException;

public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool;

	public CompanyDBDAO() throws DataBaseIsCloseException, ServerCantLoadException {
		super();
		pool = ConnectionPool.getInstance();
	}

	@Override
	public void createCompany(Company comp) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException,
			CreateBeanException, ServerCantLoadException {

		Connection con = pool.getConnection();
		String sql = "INSERT INTO company VALUES (?,?,?,?)";
		PreparedStatement preStatement = null;

		try {

			preStatement = con.prepareStatement(sql);
			preStatement.setLong(1, comp.getId());
			preStatement.setString(2, comp.getCompName());
			preStatement.setString(3, comp.getPassword());
			preStatement.setString(4, comp.getEmail());
			preStatement.executeUpdate();

		} catch (SQLException e) {
			throw new CreateBeanException();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	@Override
	public void removeCompany(Company comp) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException,
			CantUpdateException, ServerCantLoadException {

		Connection con = null;

		try {
			con = pool.getConnection();
			Statement stm = con.createStatement();

			String sql = "DELETE FROM company WHERE id =" + comp.getId();
			stm.executeUpdate(sql);

		} catch (SQLException e) {
			throw new CantUpdateException();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	@Override
	public void updateCompany(Company comp)
			throws CantUpdateException, DataBaseIsCloseException, DatabaseAtMaxCapacityException {

		Connection con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();

				String sql = "UPDATE Company SET id =" + comp.getId() + ", password = '" + comp.getPassword()
						+ "', Email='" + comp.getEmail() + "' , comp_name = '" + comp.getCompName()
						+ "'WHERE comp_name = '" + comp.getCompName() + "'";
				stm.executeUpdate(sql);

			} catch (SQLException e) {
				throw new CantUpdateException();
			} finally {
				pool.returnConnection(con);
			}
		}

	}

	@Override
	public Company getCompany(long id)
			throws CouldntGetFromDataBase, DataBaseIsCloseException, DatabaseAtMaxCapacityException {

		Connection con = null;
		try {
			con = pool.getConnection();

			if (con != null) {

				Company comp = new Company();
				Statement stm = con.createStatement();
				ResultSet result;

				result = stm.executeQuery("SELECT * FROM company");
				if (result != null)
					while (result.next() == true) {
						if (result.getLong("id") == id) {
							comp.setCompName(result.getString("comp_name"));
							comp.setEmail(result.getString("email"));
							comp.setPassword(result.getString("password"));
							comp.setId(id);
							return comp;
						}
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CouldntGetFromDataBase();
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

		throw new CouldntGetFromDataBase();
	}

	@Override
	public Collection<Company> getAllCompanies() throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CouldntGetFromDataBase {

		Collection<Company> compCollection = new HashSet<>();

		Connection con = pool.getConnection();

		if (con != null) {

			try {

				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM company");

				while (result.next() == true) {
					if (result != null) {

						Company comp = new Company();

						comp.setCompName(result.getString("comp_name"));
						comp.setEmail(result.getString("email"));
						comp.setPassword(result.getString("password"));
						comp.setId(result.getLong("id"));

						compCollection.add(comp);
					}

				}

			} catch (SQLException e) {
				throw new CouldntGetFromDataBase();
			} finally {
				pool.returnConnection(con);
			}
		}
		return compCollection;

	}

	@Override
	public Collection<Coupon> getCoupons() throws GeneralCouponSystemException {

		Collection<Coupon> coupCollection = new HashSet<>();

		CouponDBDAO DAO = new CouponDBDAO();
		coupCollection = DAO.getAllCoupons();
		return coupCollection;

	}

	@Override
	public boolean login(String compName, String password)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, WorrgPasswordException {

		Connection con = pool.getConnection();
		try {
			String query = "SELECT * FROM company";
			Statement statement = con.createStatement();
			ResultSet set = statement.executeQuery(query);

			while (set.next()) {
				if (set.getString("comp_name").equals(compName) && set.getString("password").equals(password)) {
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
