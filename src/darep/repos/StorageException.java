package darep.repos;

public class StorageException extends Exception {

	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
		super("ERROR: " + message);
	}
	
	public StorageException(String message, Throwable cause) {
		this(message + ": " + cause.getMessage());
		this.initCause(cause);
	}
	
}
