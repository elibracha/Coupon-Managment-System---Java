package SystemExceptionHandling;

public class TransactionCoulntFinishException extends GeneralCouponSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Server is Closing Transaction Couldn't Finish";
	
	public TransactionCoulntFinishException() {
		super(message);
	}

}
