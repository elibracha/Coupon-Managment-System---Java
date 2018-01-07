package SystemExceptionHandling;

public class GeneralCouponSystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "The Coupon System Had An Error.";

	public GeneralCouponSystemException() {
		super(message);
	}

	public GeneralCouponSystemException(String message) {
		super(message);
	}
	
	
}
