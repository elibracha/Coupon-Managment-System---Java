package Facades;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import Core.Bean.Company;
import Core.Bean.Coupon;
import Core.Bean.CouponType;
import DBDAO.CompanyCouponDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDAO;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.ServerCantLoadException;


public class CompanyFacade implements CouponClientFacade {

	private CouponDBDAO couponDBDAO;
	private CompanyCouponDAO compCoupon;
	private CustomerCouponDAO custCoupon;
	private Company usingFacadeCompany;

	public CompanyFacade() throws ServerCantLoadException, DataBaseIsCloseException {
		couponDBDAO = new CouponDBDAO();
		compCoupon = new CompanyCouponDAO();
		custCoupon = new CustomerCouponDAO();
	}


	public void createCoupon(Coupon cou) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException,
			CreateBeanException, ServerCantLoadException, CouldntGetFromDataBase {

		compCoupon.createCouponComapany(cou,usingFacadeCompany.getId());
		couponDBDAO.createCoupon(cou);
	}

	public void removeCoupon(Coupon coup) throws DataBaseIsCloseException, DatabaseAtMaxCapacityException,
			CantUpdateException, ServerCantLoadException {

		couponDBDAO.removeCoupon(coup);
		compCoupon.removeCompanyCoupon(coup);
		custCoupon.removeCustomerCoupon(coup);

	}

	public void updateCoupon(Coupon afterUpdate)
			throws CantUpdateException, DataBaseIsCloseException, DatabaseAtMaxCapacityException, CouldntGetFromDataBase {
		Coupon newCoupon = couponDBDAO.getCoupon(afterUpdate.getId());
		newCoupon.setEndDate(afterUpdate.getEndDate());
		newCoupon.setPrice(afterUpdate.getPrice());
		couponDBDAO.updateCoupon(newCoupon);

	}

	public Coupon getCoupon(long id) throws CouldntGetFromDataBase, DataBaseIsCloseException, DatabaseAtMaxCapacityException {
		return couponDBDAO.getCoupon(id);
	}

	public Collection<Coupon> getAllCoupons() throws GeneralCouponSystemException {

		Collection<Coupon> coupons = couponDBDAO.getAllCoupons();
		coupons = compCoupon.getAllCouponsByCompany(usingFacadeCompany.getId());
		return coupons;

	}

	public Collection<Coupon> getCouponByType(CouponType type) throws GeneralCouponSystemException {
		Collection<Coupon> coup = couponDBDAO.getAllCoupons();
		Iterator<Coupon> it = coup.iterator();
		while (it.hasNext())
			if (it.next().getType() != type) {
				it.remove();
			}
		return coup;
	}

	public Collection<Coupon> getCouponsByDate(Date date) throws GeneralCouponSystemException{
		Collection<Coupon> coupCol = getAllCoupons();
		Iterator<Coupon> it = coupCol.iterator();
		while (it.hasNext())
			if (it.next().getEndDate().after(date)) {
				it.remove();
			}
		return coupCol;
	}


	public Company getUsingFacadeCompany() {
		return usingFacadeCompany;
	}


	public void setUsingFacadeCompany(Company usingFacadeCompany) {
		this.usingFacadeCompany = usingFacadeCompany;
	}
}

