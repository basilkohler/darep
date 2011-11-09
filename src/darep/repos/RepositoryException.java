package darep.repos;

import darep.DarepException;

public class RepositoryException extends DarepException {

	private static final long serialVersionUID = 1L;

	public RepositoryException(String message) {
		super(message);
	}
	
	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RepositoryException(Throwable cause) {
		super(cause);
	}
}
