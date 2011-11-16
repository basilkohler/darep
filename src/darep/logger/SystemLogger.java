package darep.logger;

public class SystemLogger extends Logger {

	@Override
	public void log(String message, LOGLEVEL logLevel) {

		switch(logLevel) {
		case ERROR:
			System.out.println("ERROR: " + message);
			break;
		case SUCCESS:
			System.out.println(message);
			break;
		}

	}


}
