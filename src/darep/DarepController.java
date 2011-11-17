package darep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import darep.Command.ActionType;
import darep.logger.Logger;
import darep.logger.SystemLogger;
import darep.parser.ArgConstraint;
import darep.parser.CommandSyntax;
import darep.parser.ParseException;
import darep.parser.Parser;
import darep.repos.Repository;
import darep.repos.RepositoryException;
import darep.server.Server;
import darep.server.ServerException;
import darep.storage.Storage;
import darep.storage.fileStorage.FileStorage;

/**
 * Controls the flow of the program. Contains the main-method.
 * 
 */
public class DarepController {
	
	public static final String RESOURCES = "resources";
	public static final String HELPFILE = "help.txt";
	
	private Logger logger;
	/**
	 * The {@link CommandSyntax}-Array given to the parser. Defines the allowed
	 * syntax when calling the program. The Format for a Command is:
	 * (Name,#Options with Value,Flags)
	 */
	public static final CommandSyntax[] syntax = new CommandSyntax[] {
			new CommandSyntax(ActionType.add, 1,
					new String[] { "r", "n", "d" }, new String[] { "m" }),
			new CommandSyntax(ActionType.delete, 1, new String[] { "r" },
					new String[0]),
			new CommandSyntax(ActionType.replace, 2, new String[] { "r", "d" },
					new String[] { "m" }),
			new CommandSyntax(ActionType.help, 0, new String[0], new String[0]),
			new CommandSyntax(ActionType.list, 0, new String[] { "r" },
					new String[] { "p" }),
			new CommandSyntax(ActionType.export, 2, new String[] { "r" },
					new String[0]),
			new CommandSyntax(ActionType.server, 1,
					new String[] {"r"}, new String[0])
			};

	/**
	 * Map with {@link ArgConstraint}s for the Parser. Values are added in
	 * static initializer since there is no short syntax for Maps in Java.
	 */
	private static Map<String, ArgConstraint> constraints;

	/**
	 * Adds anonymous child objects of ArgConstraint to the constraints-map.
	 */
	private static void createConstraints() {

		constraints = new HashMap<String, ArgConstraint>();

		// name only consists of word chars (digit, letter, _) and -
		// and is no longer than 40 chars long
		constraints.put("n", new ArgConstraint() {
			@Override
			public boolean isValid(String arg) {
				return (arg.matches("[A-Z0-9_-]*") && arg.length() <= 40);
			}

			@Override
			public String getDescription() {
				return "Argument must only contain"
						+ " digits, uppercase letters, \"_\" or \"-\""
						+ " and must not be longer than 40 characters";
			}
		});

		// Description does not contain control characters and is no longer
		// than 1000 chars
		constraints.put("d", new ArgConstraint() {
			@Override
			public boolean isValid(String arg) {
				return (arg.matches("[^\\p{Cntrl}]*") && arg.length() <= 1000);
			}

			@Override
			public String getDescription() {
				return "Argument must not contain ISO control characters"
						+ " and must not be longer than 1000 characters";
			}
		});
	}

	public static Map<String, ArgConstraint> getConstraints() {
		if (constraints == null) {
			createConstraints();
		}
		return constraints;
	}

	private Parser parser;
	private Repository repository;
	
	private Storage storage = new FileStorage();

	public static void main(String[] args) {
		DarepController controller = new DarepController();
		try {
			controller.processCommand(args);
		} catch (DarepException e) {
			quitWithError(controller, e);
		}
	}

	private static void quitWithError(DarepController controller,
			DarepException e) {
		controller.getLogger().logError(e.getMessage());
		// TODO make build set debug mode or something
		e.printStackTrace();
		System.exit(1);
	}
	
	public void processCommand(String[] args) throws ParseException, RepositoryException, ServerException {
		Command command = parser.parse(args);

		// check if command=help => dont need to load the repo, just print help
		if (command.getAction() == ActionType.help) {
			printHelp();
			return;
		}
		
		// check if option -r is set or default repos, and setup repos.
		String repoName;
		if (command.isSet("r")) {
			repoName = command.getOptionParam("r");
		} else {
			repoName = Repository.getDefaultLocation();
		}
		
		repository = new Repository(repoName, logger, this.storage);
			
		switch (command.getAction()) {
		case add:
			repository.add(command);
			break;
		case delete:
			repository.delete(command);
			break;
		case replace:
			repository.replace(command);
			break;
		case export:
			repository.export(command);
			break;
		case server:
			new Server(repository, command).start();
			break;
		case list:
			logger.logSuccess(repository.getList(command));
			break;
		}

	}

	public DarepController() {
		this.logger = new SystemLogger();
		this.parser = new Parser(DarepController.syntax,
				DarepController.getConstraints(), ActionType.help);
	}
	
	void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	private void printHelp() throws RepositoryException{

		String path = RESOURCES+"/"+HELPFILE; 
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(HELPFILE);
			
			if(is == null) {
				is = new FileInputStream(new File(path));
			}
			logger.logSuccess(Helper.streamToString(is));
		} catch (FileNotFoundException e) {
			throw new RepositoryException("could not find the helpfile: " + path, e);
		} catch (IOException e) {
			throw new RepositoryException("could not print the helpfile:" + HELPFILE, e);
		} catch (NullPointerException e) {
			throw new RepositoryException("could not load helpfile " + HELPFILE +" from .jar Archive", e);
		}
	}
	
	Logger getLogger() {
		return this.logger;
	}
}