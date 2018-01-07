package SystemExceptionHandling;

public class ConnectionClosedException extends GeneralCouponSystemException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Connection Cant Be Closed, Or already Closed.";
	
	public ConnectionClosedException() {
		super(message);
	}
}
