package darep.logger;

public class SystemLogger extends Logger {
	
	StringBuilder buf = new StringBuilder();
	
	@Override
	public void log(String message, LOGLEVEL logLevel) {
		switch(logLevel) {
		case ERROR:
			message = "ERROR: " + message;
			buf.append(message + '\n');
			System.err.println(message);
			break;
		case SUCCESS:
			buf.append(message + '\n');
			System.out.println(message);
			break;
		}
	}

	@Override
	public String getContent() {
		return buf.toString();
	}
}
