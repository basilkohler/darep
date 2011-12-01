package darep.storage;

import java.io.Serializable;
import java.util.Date;

public class Metadata implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String originalName;
	private Date timestamp;
	private String description;
	private int numberOfFiles;
	private long size;
	
	public static final String[] columnHeaders = new String[] {
			"Name",
			"Original Name",
			"Timestamp",
			"Number of Files",
			"Size",
			"Description"
	};

	public Metadata() {
		timestamp = new Date();
		description = "";
		name = "";
		originalName = "";
	}
	
	public Metadata(String name, String origName, String descr, int nrFiles, int size, String reposPath) {
		this.setTimestamp(new Date());
		this.setDescription(descr);
		this.setName(name);
		this.setOriginalName(origName);
		this.setNumberOfFiles(nrFiles);
		this.setFileSize(size);
	}
	
	public void update(Metadata m) {
		String desc = m.getDescription();
		if (m != null && !desc.isEmpty()) {
			this.setDescription(desc);
		}
		
		this.setOriginalName(m.getOriginalName());
		this.setTimestamp(m.getTimeStamp());
		this.setFileSize(m.getFileSize());
		this.setNumberOfFiles(m.getNumberOfFiles());
	}
	
	public String[] toStringArray() {
		String[] values = new String[columnHeaders.length];
		
		values[0] = getName();
		values[1] = getOriginalName();
		values[2] = getTimeStamp().toString();
		values[3] = String.valueOf(getNumberOfFiles());
		values[4] = String.valueOf(getFileSize());
		values[5] = getDescription();
		
		return values;
	}
	public void setTimestamp(Date date) {
		this.timestamp = date;
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
	
	@Override
	public String toString() {
		return "METADATA:\nname: " + name + "\noriginalName: " + originalName + "\ntimestamp: " + timestamp.toString() +
				"\ndescription: " + description + "\nnumberOfFiles: " + numberOfFiles + "\nsize: " + size;
	}

	public Date getTimeStamp() {
		return (Date)timestamp.clone();
	}

	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	public String getDescription() {
		return description;
	}

	public long getFileSize() {
		return size;
	}

	public void setFileSize(long fileSize) {
		this.size = fileSize;
	}

	public void setNumberOfFiles(int countFiles) {
		this.numberOfFiles = countFiles;
	}
	
}
