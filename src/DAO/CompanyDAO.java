package DAO;


import java.util.Collection;
import Core.Bean.*;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.ServerCantLoadException;
import SystemExceptionHandling.WorrgPasswordException;

public interface CompanyDAO  {

	void createCompany(Company comp) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CreateBeanException, ServerCantLoadException ;
	void removeCompany(Company comp) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException, ServerCantLoadException;
	void updateCompany(Company comp) throws CantUpdateException, DataBaseIsCloseException, DatabaseAtMaxCapacityException ;
	Company getCompany(long id) throws CouldntGetFromDataBase, DataBaseIsCloseException, DatabaseAtMaxCapacityException  ;
	Collection<Company> getAllCompanies() throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CouldntGetFromDataBase ;
	Collection<Coupon> getCoupons() throws CouldntGetFromDataBase, DataBaseIsCloseException, DatabaseAtMaxCapacityException, ServerCantLoadException, GeneralCouponSystemException;
	boolean login (String compName,String password) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, WorrgPasswordException;
		
}