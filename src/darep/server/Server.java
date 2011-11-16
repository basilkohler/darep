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
	private static String[] propertyNames = {"incoming-directory", "html-overview", "log-file", 
											"checking-interval-in-seconds", "completeness-checker.class-name" };
	
	
	public Server(Repository repository, Command command) throws ServerException {
		this.repository = repository;
		loadPropertiesFile(command.getParams()[0]);
	}
	
	public void start() throws ServerException {
		
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
