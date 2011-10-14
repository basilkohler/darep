package darep.repos;

import java.io.File;

import darep.Command;
import darep.Command.ActionType;

/*the repository provides methods to access the Database.class
 * and represents the physical folder located at 'location', which contains the db
 * the repository ensures he correctness of the contents of the db !!!!
 * 
 */
public class Repository {
	private static final String DEFAULT_LOCATION = System
			.getProperty("user.home") + "/.data-repository";
	private File location;
	private Database db;
	
	

	/*
	 * loads(/creates) the default (hidden) repo in user.home
	 */
	public Repository() {
		location = new File(DEFAULT_LOCATION);
		initRepository(DEFAULT_LOCATION);
	}

	/*
	 * loads(/creates) the repo located at path
	 * 
	 * @param path where to find/create repo
	 */
	public Repository(String path) {
		location = new File(path);
		initRepository(path);
	}

	private void initRepository(String path) {
		if (!location.exists()) {
			location.mkdirs();
			System.out.println("created new repository " + path);
		}
		db = new Database(location.getAbsolutePath());
	}

	/*
	 * adds a dataset to the db, as specified by the options in 'command'
	 * 
	 * @param command the command object which stores the options
	 */
	public boolean add(Command command) throws RepositoryException {

		File file = getInputFile(command);
		if (!file.exists()) {
			throw new RepositoryException(file.getAbsolutePath()+" does not exist.");
		}
		if(location.getAbsolutePath().contains(file.getAbsolutePath())){
			throw new RepositoryException("Dataset can not contain the repository itself.");
		}
				
		Metadata meta = createMetaData(command);
		return db.add(file, meta, !command.hasFlag("m"));
	}

	public boolean delete(Command command) throws RepositoryException {
		if (db.delete(command.getParams()[0])) {
			System.out
					.println("The data set "
							+ command.getParams()[0]
							+ " (original name: file/folder name) has been successfully removed from the repository.");
			return true;
		} else {
			throw new RepositoryException("Unknown data set "+ command.getParams()[0]);
		}
	}

	/*
	 * creates a new valid metadata for a new dataset. "valid" means the
	 * returned Metadata is safe to be entered into the db.
	 * 
	 * @ param command
	 */
	private Metadata createMetaData(Command command) throws RepositoryException {
		Metadata meta = new Metadata();
		meta.setOriginalName(getInputFile(command).getName());

		if (command.isSet("d"))
			meta.setDescription(command.getOptionParam("d"));

		String name;
		if (command.isSet("n")) { // name option set?
			name = command.getOptionParam("n");
			if (db.contains(name)) { //if name exists, exit
				throw new RepositoryException("There is already a data" +
						" set named " + name + " name in the repository.");
			}
		} else { // no name provided, make a unique name from originalname
			name = createUniqueName(meta.getOriginalName());
		}
		meta.setName(name);

		return meta;
	}

	private File getInputFile(Command command) {
		// is the input file stored in the second or first parameter?
		if (command.getAction() == ActionType.replace) {
			return new File(command.getParams()[1]);
		} else {
			return new File(command.getParams()[0]);
		}
	}

	private String createUniqueName(String name) {
		name = name.toUpperCase();
		int max = 40;
		if (name.length() > max)
			name = name.substring(0, max);
		if (db.contains(name)) { // if name exists append number
			int append = 1;
			if (name.length() == max)
				name = name.substring(0, max - 1);
			while (db.contains(name + append)) {
				append++;
				if ((name + append).length() > max)
					name = name.substring(0, name.length() - 1);
			}
			name = name + append;
		}
		return name;
	}
	
	public String getList(Command command) throws RepositoryException {
		StringBuilder sb = new StringBuilder();
		for (Dataset dataset: db.getAllDatasets()) {
			sb.append(dataset + "\n");
		}
		// TODO Pretty print and foot-line (total usw)  
		return sb.toString();
	}

	public void replace(Command command) throws RepositoryException {
		boolean success1 = delete(command);
		boolean success2 = add(command);

		if (success1 && success2)  {
			System.out.println("The data set named "
					+ command.getParams()[0] + " has been successfully" +
					" replaced by the file/folder.");
		}
	}

	public void export(Command command) throws RepositoryException {
		String absolutExportPath = new File(command.getParams()[1]).getAbsolutePath();
		if(absolutExportPath.contains(location.getAbsolutePath()))
			throw new RepositoryException("It is not allowed to export something into the "+location.toString()+" repository folder");
		if (!db.contains(command.getParams()[0])) 
			throw new RepositoryException("Unknown data set "+ command.getParams()[0]);
		File exportedFile = db.export(command.getParams()[0],command.getParams()[1]);

		System.out.println("The data set " + command.getParams()[0] +
					" (original name: " + exportedFile.getName() + " )" +
					" has been successfully exported to"
					+ command.getParams()[1]);
	}
	
	protected File getLocation() {
		return this.location;
	}



	public static String getDefaultLocation() {
		return DEFAULT_LOCATION;
	}



}
