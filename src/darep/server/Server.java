package darep.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import darep.Command;
import darep.repos.Repository;

public class Server {
	
	private Repository repository;
	private Properties properties = new Properties();
	private static final String[] propertyNames = {"incoming-directory", "html-overview", "log-file", 
											"checking-interval-in-seconds", "completeness-checker.class-name" };
	
	private static final int INCOMING_DIRECTORY = 0;
	private static final int HTML_OVERVIEW = 1;
	private static final int LOG_FILE = 2;
	private static final int CHECKING_INTERVAL_IN_SECONDS = 3;
	private static final int COMPLETENESS_CHECKER_CLASS_NAME = 4;
	
	private boolean running = true;
	public Server(Repository repository, Command command) throws ServerException {
		this.repository = repository;
		loadPropertiesFile(command.getParams()[0]);
	}
	
	public void start() throws ServerException {
		while(running) {
			
			int seconds = Integer.parseInt(getPropertiesValue(CHECKING_INTERVAL_IN_SECONDS));
			try {
				Thread.sleep(seconds*1000);
			} catch (InterruptedException e) {
				throw new ServerException(e);
			}
		}
	}
	
	private String getPropertiesValue(int propertyKey) {
		return properties.getProperty(propertyNames[propertyKey]);
	}
	
	private void loadPropertiesFile(String propertiesPath) throws ServerException {
		try {
			properties.load(new FileInputStream(propertiesPath));
			for(String p : propertyNames) {
				if(properties.getProperty(p) == null) {
					throw new ServerException("Missing property " + p + " in the property file " + propertiesPath);
				}
			}
		} catch (FileNotFoundException e) {
			throw new ServerException("could not find properties file " + propertiesPath, e);
		} catch (IOException e) {
			throw new ServerException("could not read properties file " + propertiesPath, e);
		}
	}
	
}
