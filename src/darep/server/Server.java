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
	private String incomingDirectory;
	private String htmlOverview;
	private String logFile;
	private String checkingIntervalInSeconds;
	private String completenessCheckerClassName;
	
	public Server(Repository repository, Command command) throws ServerException {
		this.repository = repository;
		loadPropertiesFile(command.getParams()[0]);
	}
	
	public void start() throws ServerException {
		
	}
	
	private void loadPropertiesFile(String propertiesPath) throws ServerException {
		try {
			properties.load(new FileInputStream(propertiesPath));
			incomingDirectory = properties.getProperty("incoming-directory");
			htmlOverview = properties.getProperty("html-overview");
			logFile = properties.getProperty("log-file");
			checkingIntervalInSeconds = properties.getProperty("checking-interval-in-seconds");
			completenessCheckerClassName = properties.getProperty("completeness-checker.class-name");
			
		} catch (FileNotFoundException e) {
			throw new ServerException("could not find properties file " + propertiesPath, e);
		} catch (IOException e) {
			throw new ServerException("could not read properties file " + propertiesPath, e);
		}
	}
	
}
