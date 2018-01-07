package SystemExceptionHandling;

public class DataBaseIsCloseException extends GeneralCouponSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "DataBase Temporary Closed.";
	
	public DataBaseIsCloseException() {
		super(message);
	}

}
