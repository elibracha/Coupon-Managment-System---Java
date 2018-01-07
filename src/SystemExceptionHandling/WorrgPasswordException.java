package SystemExceptionHandling;

public class WorrgPasswordException extends GeneralCouponSystemException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Worrg Password or Username. Try again later!";
	
	public WorrgPasswordException() {
		super(message);
	}
}
