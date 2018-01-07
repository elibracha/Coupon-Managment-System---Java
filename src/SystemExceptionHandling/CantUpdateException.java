package SystemExceptionHandling;

public class CantUpdateException extends GeneralCouponSystemException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String UpdateMessage = "Update Couldn't Execute, Please Try Again Later"; 

	public CantUpdateException() {
		super(UpdateMessage);
		
	}
	

}
