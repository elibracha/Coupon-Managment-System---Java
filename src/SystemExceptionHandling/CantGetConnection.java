package SystemExceptionHandling;

public class CantGetConnection extends GeneralCouponSystemException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Connection Cant Establish, You May Try Later ";
	
	public CantGetConnection() {
		super(message);
	}
	
}
