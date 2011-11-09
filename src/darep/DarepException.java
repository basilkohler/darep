package darep;

public class DarepException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DarepException(String message) {
		super(message);
	}
	
	public DarepException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DarepException(Throwable cause) {
		super(cause.getMessage());
		this.initCause(cause);
	}
	
}
