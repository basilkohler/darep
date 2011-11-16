package darep.server;

import darep.DarepException;

public class ServerException extends DarepException {

	private static final long serialVersionUID = 1L;

	public ServerException(String message) {
		super(message);
	}
	
	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServerException(Throwable cause) {
		super(cause);
	}
}
