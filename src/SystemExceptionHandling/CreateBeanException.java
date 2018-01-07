package SystemExceptionHandling;

public class CreateBeanException extends GeneralCouponSystemException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already,\nIf Not Try Again Later .  ";
	
	public CreateBeanException() {
		super(message);
	}
	

}
