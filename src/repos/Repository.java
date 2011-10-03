package repos;

import java.io.File;

import darep.Command;

public class Repository {
	private File location;
	private Database db;
	
	//loads(/creates) the default repo in user.home
	public Repository() {
		location=new File(System.getProperty("user.home")+"/.data-repository");
		if (!location.exists()) {
			location.mkdir();
			System.out.println("created new repository "+System.getProperty("user.home")+"/.data-reository");
		}
		db=new Database(location.getAbsolutePath());
	}

	//loads(/creates) the repo located at path
	public Repository(String path) {
		location=new File(path);
		if (!location.exists()) {
			location.mkdir();
			System.out.println("created new repository "+path);	
		}
		db=new Database(location.getAbsolutePath());
	}

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
