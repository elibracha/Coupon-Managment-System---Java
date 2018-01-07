package DAO;

import java.util.Collection;
import Core.Bean.*;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;

public interface CouponDAO {
	
	
	void removeCoupon(Coupon coupon) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CantUpdateException;
	void updateCoupon(Coupon coupon) throws CantUpdateException, DataBaseIsCloseException, DatabaseAtMaxCapacityException;
	Coupon getCoupon(long id) throws CouldntGetFromDataBase, DataBaseIsCloseException, DatabaseAtMaxCapacityException;
	Collection<Coupon> getAllCoupons() throws GeneralCouponSystemException;
	Collection<Coupon> getCouponByType(CouponType type) throws GeneralCouponSystemException;
	void createCoupon(Coupon coupon)
			throws DataBaseIsCloseException, DatabaseAtMaxCapacityException, CreateBeanException;
	
	
}

