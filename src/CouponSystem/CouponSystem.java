package CouponSystem;

import java.util.Collection;
import java.util.Iterator;
import Core.Bean.Company;
import Core.Bean.Customer;
import Core.PoolConnection.ConnectionPool;
import DAO.CompanyDAO;
import DAO.CustomerDAO;
import DBDAO.CompanyDBDAO;
import DBDAO.CustomerDBDAO;
import DailyCouponSystemTask.DailyCouponExpirationTask;
import Facades.AdminFacade;
import Facades.CompanyFacade;
import Facades.CouponClientFacade;
import Facades.CustomerFacade;
import SystemExceptionHandling.ConnectionClosedException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.ServerCantLoadException;
import SystemExceptionHandling.TransactionCoulntFinishException;
import SystemExceptionHandling.WorrgPasswordException;

public class CouponSystem {

	private static CouponSystem system = new CouponSystem();
	private DailyCouponExpirationTask task = DailyCouponExpirationTask.getTask();
	private CompanyDAO companyDAO;
	private CustomerDAO customerDAO;

	private CouponSystem() {

		try {
			companyDAO = new CompanyDBDAO();
			customerDAO = new CustomerDBDAO();
			new Thread(task).start();
		} catch (DataBaseIsCloseException e) {
			System.out.println(e.getMessage());
		} catch (ServerCantLoadException e) {
			System.out.println(e.getMessage());
		}

	}

	public static CouponSystem getInstance() {
		return system;
	}

	public CouponClientFacade login(String name, String password, String type) throws ServerCantLoadException,
			DataBaseIsCloseException, DatabaseAtMaxCapacityException, WorrgPasswordException {

		if (type.equals("company") && companyDAO.login(name, password)) {
			try {
				CompanyFacade client = new CompanyFacade();
				Collection<Company> compCol = companyDAO.getAllCompanies();
				Iterator<Company> it = compCol.iterator();
				while (it.hasNext()) {
					Company current = it.next();
					if (name.equals(current.getCompName())) {
						client.setUsingFacadeCompany(current);
						return client;
					}
				}
			} catch (CouldntGetFromDataBase e) {
				throw new WorrgPasswordException();
			}

		} else if (type.equals("customer") && customerDAO.login(name, password)) {

			CustomerFacade client = new CustomerFacade();
			Collection<Customer> custCol = customerDAO.getAllCustomers();
			Iterator<Customer> it = custCol.iterator();
			while (it.hasNext()) {
				Customer current = it.next();
				if (name.equals(current.getCusName())) {
					client.setUsingFacadeCustomer(current);
					return client;
				}
			}

		} else if (type.equals("admin") && password.equals("1234") && name.equals("admin")) {
			CouponClientFacade client = new AdminFacade();
			return client;
		}
		
		throw new WorrgPasswordException();
	}
	
	public void shutDown() throws ConnectionClosedException, TransactionCoulntFinishException{
		ConnectionPool.closeAllConnections();
		task.StopTask();
	}
}
