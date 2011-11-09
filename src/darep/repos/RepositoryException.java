package darep.repos;

public class RepositoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public RepositoryException(String message) {
		super("ERROR: " + message);
	}
	
	public RepositoryException(String message, Throwable cause) {
		this(message + " Cause: " + cause.getMessage());
		this.initCause(cause);
	}
}
