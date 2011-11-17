package darep.logger;

import java.io.IOException;

public abstract class Logger {
	public enum LOGLEVEL {ERROR, SUCCESS};
	
	public abstract void log(String message, LOGLEVEL logLevel);
	
	public abstract String getContent() throws IOException;
	
	public void logSuccess(String message) {
		this.log(message, LOGLEVEL.SUCCESS);
	}
	public void logError(String message) {
		this.log(message, LOGLEVEL.ERROR);
	}
}
