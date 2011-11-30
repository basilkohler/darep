package darep.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import darep.Command;
import darep.Command.ActionType;
import darep.logger.Logger;
import darep.repos.Repository;
import darep.repos.RepositoryException;

public class Server extends Thread {
	
	private Repository repository;
	private Logger serverLogger;
	private Properties properties = new Properties();
	private final String[] propertyValues;
	private boolean running = true;
	private CompletenessChecker completenessChecker;
	
	private static final String[] propertyNames = {"incoming-directory", "html-overview", "log-file", 
		"checking-interval-in-seconds", "completeness-checker.class-name" };
	private static final int INCOMING_DIRECTORY = 0;
	private static final int HTML_OVERVIEW = 1;
	private static final int LOG_FILE = 2;
	private static final int CHECKING_INTERVAL_IN_SECONDS = 3;
	private static final int COMPLETENESS_CHECKER_CLASS_NAME = 4;
	
	
	public Server(Repository repository, Command command) throws ServerException {
		
		propertyValues = new String[] {
				null,
				null,
				new File(repository.getLocation(), "server.log").toString(),
				null,
				null
		};
		
		this.repository = repository;
		loadPropertiesFile(command.getParams()[0]);
		createServerFiles();
		completenessChecker = getCompletenessChecker();
		serverLogger = new ServerLogger(getProperty(LOG_FILE));
		repository.setLogger(serverLogger);
	}
	
	private CompletenessChecker getCompletenessChecker() throws ServerException {
		try {
			Class<?> cls = Class.forName(getProperty(COMPLETENESS_CHECKER_CLASS_NAME));
			Object o = cls.newInstance();
			if (o instanceof CompletenessChecker) {
				return (CompletenessChecker) o;
			} 
			throw new ServerException("Specified class does not implement CompletenessChecker");
		} catch (ClassNotFoundException e) {
			throw new ServerException(e);
		} catch (InstantiationException e) {
			throw new ServerException(e);
		} catch (IllegalAccessException e) {
			throw new ServerException(e);
		}
	}

	private void createServerFiles() throws ServerException {
		File incoming = new File(getProperty(INCOMING_DIRECTORY));
		if(incoming.exists() == false) {
			throw new ServerException("incoming directory " + incoming.getAbsolutePath() + " does not exist.");
		}		
		
		File log = new File(getProperty(LOG_FILE));
		if(log.exists() == false) {
			try {
				log.createNewFile();
			} catch (IOException e) {
				throw new ServerException("could not create logfile " + log.getAbsolutePath() + " because the folder does not exist", e);
			}
		}
	}
	
	@Override
	public void run() {
		try {
			int seconds;
			try {
				seconds = Integer.parseInt(getProperty(CHECKING_INTERVAL_IN_SECONDS));
			} catch (NumberFormatException e) {
				throw new ServerException("checking-interval-in-seconds property must be a number");
			}
			if(seconds < 0) {
				throw new ServerException("checking-interval-in-seconds property must be bigger 1 or bigger");
			}
			
			File dir = new File(getProperty(INCOMING_DIRECTORY));
			
			while(running) {
				File[] files = completenessChecker.getCompletedFiles(dir);
				for(File f : files) {
					try {
						addFile(f); 
					} catch (RepositoryException e) {
						throw new ServerException("server could not add file " + f.getPath());
					}
				}
				try {
					Thread.sleep(seconds*1000);
				} catch (InterruptedException e) {
					throw new ServerException(e);
				}
			}
		} catch (ServerException e) {
			serverLogger.logError(e.getMessage());
		}
	}
	
	private String getProperty(int propertyKey) {
		return properties.getProperty(propertyNames[propertyKey], propertyValues[propertyKey]);
	}
	
	private void loadPropertiesFile(String propertiesPath) throws ServerException {
		
		try {
			FileInputStream is = new FileInputStream(propertiesPath);
			try {
				properties.load(is);
			} finally {
				is.close();
			}
			for(int i = 0; i < propertyNames.length; i++) {
				if(getProperty(i) == null) {
					throw new ServerException("Missing property " + propertyNames[i] + " in the property file " + propertiesPath);
				}
			}
		} catch (FileNotFoundException e) {
			throw new ServerException("could not find properties file " + propertiesPath, e);
		} catch (IOException e) {
			throw new ServerException("could not read properties file " + propertiesPath, e);
		}
	}
	
	private void addFile(File file) throws RepositoryException {
		Command command = new Command(ActionType.add, new String[] {file.getPath()}, null, new String[] {"m"});
		this.repository.add(command);
	}
}
