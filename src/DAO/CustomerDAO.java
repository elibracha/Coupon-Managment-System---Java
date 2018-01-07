package DAO;

import java.util.Collection;
import Core.Bean.*;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.WorrgPasswordException;

public interface CustomerDAO {
	
	void createCustomer(Customer cust) throws CreateBeanException, DataBaseIsCloseException, DatabaseAtMaxCapacityException;
	void removeCustumer(Customer cust) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException;
	void updateCustomer(Customer cust) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException;
	Customer getCustomer(long id) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CouldntGetFromDataBase;
	Collection<Customer> getAllCustomers() throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, WorrgPasswordException;
	Collection<Coupon> getCoupons() throws GeneralCouponSystemException;
	boolean login (String custName,String password) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, WorrgPasswordException;
	
}