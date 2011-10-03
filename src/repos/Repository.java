package repos;

import java.io.File;

import darep.Command;


/*the repository provides methods to acces the Database.class
 * represents the physical folder located at 'location', which contains the db
 * the repository ensures he correctness of the contents of the db !!!!
 * FINDET IHR DAS SINVOLL? 
 * (Repository.class KONTROLLIERT, Database.class SPEICHERT NUR WAS IHR GESAGT WIRD?)
 */
public class Repository {
	private File location;
	private Database db;
	
	/* loads(/creates) the default repo in user.home
	 * 
	 */
	public Repository() {
		location=new File(System.getProperty("user.home")+"/.data-repository");
		if (!location.exists()) {
			location.mkdir();
			System.out.println("created new repository "+System.getProperty("user.home")+"/.data-reository");
		}
		db=new Database(location.getAbsolutePath());
	}

	/* loads(/creates) the repo located at path
	 * 
	 *  @param path where to find/create repo
	 */
	public Repository(String path) {
		location=new File(path);
		if (!location.exists()) {
			location.mkdir();
			System.out.println("created new repository "+path);	
		}
		db=new Database(location.getAbsolutePath());
	}

	/*adds a dataset to the db, as specified by the option in 'command'
	 *  @param command the command object which stores the options
	 */
	public void add(Command command) {
		File dataset =new File(command.getParams()[0]);
		if (!dataset.exists()) {
			System.out.println("file/folder does not exist.");
			System.exit(1);
		}
		Metadata meta=makeMetadata(command);
		db.add(dataset.getAbsoluteFile(),meta, !command.isSet("m"));
		System.out.println("The file/folder "+meta.getOriginalName()+
				" has been successfully added to the repository" +
				" as data set named "+meta.getName());
	}

	private Metadata makeMetadata(Command command) {
		Metadata meta=new Metadata();
		meta.setOriginalName(new File(command.getParams()[0]).getName());
		if (command.isSet("d"))
			meta.setDescription(command.getOptionParam("d"));
		String name;
		if (command.isSet("n")) {
			name=command.getOptionParam("n");
			if (db.contains(name)) {
				System.out.println("ERROR: There is already a data set named data set name in the repository.");
				System.exit(1);
		}
		}
		else {
			name=meta.getOriginalName().toUpperCase();
			if (name.length()>40)
				name=name.substring(0,39);
		if (db.contains(name)) {
			int append=1;
		while (db.contains(name.concat(Integer.toString(append)))) {
		append++;	
		}
		name=name.concat(Integer.toString(append));
		}
		}
		meta.setName(name);
		return meta;

	}

}
