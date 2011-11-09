package darep.repos;

import darep.DarepException;

public class StorageException extends DarepException {

	public StorageException(String message) {
		super(message);
	}
	
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public StorageException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;
	
}
