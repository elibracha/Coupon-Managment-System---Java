package SystemExceptionHandling;

public class DriverCantLoadException extends GeneralCouponSystemException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Driver Couldn't Load";
	
	public DriverCantLoadException() {
		super(message);
	}
}
