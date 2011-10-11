package darep.repos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	private long size;
	private File path;

	public Metadata() {
		timestamp = new Date();
		description = new String();
		name = new String();
		originalName = new String();
		path = null;
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

	public void saveAt(String pathStr) throws RepositoryException {
		path = new File(pathStr);
		save();
	}
	
	public void save() throws RepositoryException {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			try {
				out.writeObject(this);
			} finally {
				out.close();
				fileOut.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RepositoryException("could not save metadata");
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
	
	public void incrementNumberOfFiles() {
		this.numberOfFiles++;
	}
	
	public void addFileSize(long sizeToAdd) {
		this.size += sizeToAdd;
	}
	
	@Override
	public String toString() {
		String filePath = null;
		if(path != null)
			filePath = path.getAbsolutePath();
		else
			filePath = new String();
			
		return "METADATA:\nname: " + name + "\noriginalName: " + originalName + "\ntimestamp: " + timestamp.toString() +
				"\ndescription: " + description + "\nnumberOfFiles: " + numberOfFiles + "\nsize: " + size + "\npath: " + filePath;
	}


}
