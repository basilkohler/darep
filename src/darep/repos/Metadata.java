package darep.repos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Metadata implements Serializable {
	private static final long serialVersionUID = 201110031237L;
	private String name;
	private String originalName;
	private Date timestamp;
	private String description;
	private int numberOfFiles;
	private int size;
	private File path;

	public Metadata() {
		timestamp = new Date();
		description = new String();
	}
	
	public Metadata(String name, String origName, String descr, int nrFiles, int size, String reposPath) {
		this.timestamp = new Date();
		this.description = descr;
		this.name=name;
		this.originalName=origName;
		this.numberOfFiles=nrFiles;
		this.size=size;
		this.path=new File(reposPath+"/metadata/"+name);
	}

	public void saveAt(String pathStr) throws RepositoryExeption {
		path = new File(pathStr);
		save();
	}
	
	public void save() throws RepositoryExeption {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RepositoryExeption("could not save metadata");
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getOriginalName() {
		return originalName;
	}

}
