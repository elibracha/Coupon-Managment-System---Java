package SystemExceptionHandling;

public class ServerCantLoadException extends GeneralCouponSystemException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String ServerMessage = "Server Couldn't Load, Please Try Again Later"; 

	public ServerCantLoadException() {
		super(ServerMessage);
		
	}
	
}
