package Facades;

import java.util.Collection;
import java.util.Iterator;
import Core.Bean.Coupon;
import Core.Bean.CouponType;
import Core.Bean.Customer;
import DAO.CouponDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDAO;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.ServerCantLoadException;

public class CustomerFacade implements CouponClientFacade {

	private CustomerCouponDAO customerCouponDAO;
	private CouponDAO couponDAO;
	private Customer usingFacadeCustomer;

	public CustomerFacade() throws ServerCantLoadException, DataBaseIsCloseException {

		customerCouponDAO = new CustomerCouponDAO();
		couponDAO = new CouponDBDAO();
	}

	public void purchaseCoupon(Coupon coup) throws CouldntGetFromDataBase, DataBaseIsCloseException,
			DatabaseAtMaxCapacityException, ServerCantLoadException, CreateBeanException, CantUpdateException {
		Coupon coupon = couponDAO.getCoupon(coup.getId());
		if (coupon != null && coupon.getAmount() > 0) {
			coupon.setAmount(coupon.getAmount() - 1);
			couponDAO.updateCoupon(coupon);
			customerCouponDAO.createCouponCustomer(coup, usingFacadeCustomer.getId());
		}

	}

	public Collection<Coupon> getAllPurchasedCoupons() throws GeneralCouponSystemException {
		return customerCouponDAO.getAllCouponsByCustomer(usingFacadeCustomer.getId());

	}

	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) throws GeneralCouponSystemException {
		Collection<Coupon> coupCol = getAllPurchasedCoupons();
		Iterator<Coupon> it = coupCol.iterator();
		while (it.hasNext())
			if (it.next().getType() != type) {
				it.remove();
			}
		return coupCol;
	}

	public Collection<Coupon> getALLPurchasedCouponsByPrice(double price) throws GeneralCouponSystemException {
		Collection<Coupon> coupCol = getAllPurchasedCoupons();
		Iterator<Coupon> it = coupCol.iterator();
		while (it.hasNext())
			if (it.next().getPrice() > price) {
				it.remove();
			}
		return coupCol;
	}

	public Customer getUsingFacadeCustomer() {
		return usingFacadeCustomer;
	}

	public void setUsingFacadeCustomer(Customer usingFacadeCustomer) {
		this.usingFacadeCustomer = usingFacadeCustomer;
	}
}
