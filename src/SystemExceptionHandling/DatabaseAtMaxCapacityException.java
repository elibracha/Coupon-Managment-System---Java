package SystemExceptionHandling;

public class DatabaseAtMaxCapacityException extends GeneralCouponSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "No Connection Currently, Try Later.";
	
	public DatabaseAtMaxCapacityException() {
		super(message);
	}
}
