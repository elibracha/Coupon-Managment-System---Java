package SystemExceptionHandling;

public class ReturnConnectionException extends GeneralCouponSystemException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Couldn't Return Connection, Try again";
	
	public ReturnConnectionException() {
		super(message);
	}
}
