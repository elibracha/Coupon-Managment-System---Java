package SystemExceptionHandling;

public class CreateTableException extends GeneralCouponSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Creating DataBase Failed, Possiblely Because Tables Already Exists In DataBase.";

	public CreateTableException() {
		super(message);
	}
}
