package repos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

	public void saveAt(String pathStr) {
		path = new File(pathStr);
		save();
	}
	
	public void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
