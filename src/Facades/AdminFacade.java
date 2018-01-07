package Facades;

import java.util.Collection;
import java.util.Iterator;
import Core.Bean.Company;
import Core.Bean.Coupon;
import Core.Bean.Customer;
import DAO.CompanyDAO;
import DAO.CouponDAO;
import DAO.CustomerDAO;
import DBDAO.CompanyCouponDAO;
import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDAO;
import DBDAO.CustomerDBDAO;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.ServerCantLoadException;

public class AdminFacade implements CouponClientFacade{

	private CompanyDAO companyDAO;
	private CustomerDAO customerDAO;
	private CouponDAO coupDAO;
	private CompanyCouponDAO compCouponDAO;
	private CustomerCouponDAO custCouponDAO;

	public AdminFacade() throws DataBaseIsCloseException, ServerCantLoadException {
		companyDAO = new CompanyDBDAO();
		customerDAO = new CustomerDBDAO();
		compCouponDAO = new CompanyCouponDAO();
		custCouponDAO = new CustomerCouponDAO();
		coupDAO = new CouponDBDAO();

	}

	public void createCompany(Company comp) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException,
			ServerCantLoadException, CantUpdateException, CreateBeanException {
		try {
			companyDAO.createCompany(comp);
			compCouponDAO.createCouponComapany(comp);
			Collection<Coupon> coupCol = comp.getCoupons();

			if (coupCol != null) {
				Iterator<Coupon> it = coupCol.iterator();
				while (it.hasNext())
					coupDAO.createCoupon(it.next());
			}
		} catch (Exception e) {
			throw new CreateBeanException();
		}

	}

	public void removeCompany(Company comp) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException,
			CantUpdateException, ServerCantLoadException {

		companyDAO.removeCompany(comp);
		Collection<Coupon> coupCol = null;
		try {
			coupCol = compCouponDAO.getAllCouponsByCompany(comp.getId());
		} catch (GeneralCouponSystemException e) {
		}
		if (coupCol != null) {
			Iterator<Coupon> it = coupCol.iterator();
			while (it.hasNext()) {
				Coupon currentCoupon = it.next();
				compCouponDAO.removeCompanyCoupon(currentCoupon);
				coupDAO.removeCoupon(currentCoupon);
				custCouponDAO.removeCustomerCoupon(currentCoupon);
			}
		}
	}

	public void updateCompany(Company afterUpdate)
			throws CantUpdateException, DataBaseIsCloseException, DatabaseAtMaxCapacityException, CouldntGetFromDataBase {

		Company beforeUpdate = companyDAO.getCompany(afterUpdate.getId());
		afterUpdate.setCompName(beforeUpdate.getCompName());
		companyDAO.updateCompany(afterUpdate);

	}

	public Company getCompany(long id)
			throws CouldntGetFromDataBase, DataBaseIsCloseException, DatabaseAtMaxCapacityException {
		return companyDAO.getCompany(id);

	}

	public Collection<Company> getAllCompanies() throws GeneralCouponSystemException {
		return companyDAO.getAllCompanies();
	}

	public void createCustomer(Customer cust) throws CreateBeanException, DataBaseIsCloseException,
			DatabaseAtMaxCapacityException, ServerCantLoadException {

		customerDAO.createCustomer(cust);
		custCouponDAO.createCustomerCoupon(cust);

	}

	public void removeCustomer(Customer cust)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException {

		customerDAO.removeCustumer(cust);

		Collection<Coupon> coupCol = cust.getCoupons();

		if (coupCol != null) {
			Iterator<Coupon> it = coupCol.iterator();
			while (it.hasNext()) {
				custCouponDAO.removeCustomerCoupon(it.next());

			}
		}
	}

	public void updateCustomer(Customer afterUpdate)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException, CouldntGetFromDataBase {

		Customer beforeUpdate = customerDAO.getCustomer(afterUpdate.getId());
		afterUpdate.setCusName(beforeUpdate.getCusName());
		customerDAO.updateCustomer(afterUpdate);
	}

	public Customer getCustomer(long id)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CouldntGetFromDataBase {
		return customerDAO.getCustomer(id);
	}

	public Collection<Customer> getAllCustomer() throws GeneralCouponSystemException {
		return customerDAO.getAllCustomers();
	}

	public boolean login(String name, String password) {
		if (name.equals("admin") && password.equals("1234"))
			return true;
		return false;
	}

}
