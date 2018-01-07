package SystemExceptionHandling;

public class CouldntGetFromDataBase extends GeneralCouponSystemException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Couldn't Get Data From DataBase, Data Not Exist Or Invaild ID";
	
	public CouldntGetFromDataBase() {
		super(message);
	}
	

}
