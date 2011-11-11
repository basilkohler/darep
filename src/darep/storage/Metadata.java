package darep.storage;

import java.io.Serializable;
import java.util.Date;

import darep.Helper;

public class Metadata implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String originalName;
	private Date timestamp;
	private String description;
	private int numberOfFiles;
	private long size;
	
	private static int maxNameLength = "Name".length();
	private static int maxOrigNameLength = "Original Name".length();
	private static int maxTimestampLength = "Timestamp".length();
	private static int maxNumFilesLength = "Number of Files".length();
	private static int maxSizeLength = "Size".length();
	private static int maxDescriptionLength = "Description".length();
	
	public static int getMaxNameLength() {
		return maxNameLength;
	}

	public static int getMaxOrigNameLength() {
		return maxOrigNameLength;
	}

	public static int getMaxTimestampLength() {
		return maxTimestampLength;
	}

	public static int getMaxNumFilesLength() {
		return maxNumFilesLength;
	}

	public static int getMaxSizeLength() {
		return maxSizeLength;
	}

	public static int getMaxDescriptionLength() {
		return maxDescriptionLength;
	}
	
	public static int getTotalMaxWidth() {
		return (maxNameLength
				+ maxOrigNameLength
				+ maxTimestampLength
				+ maxNumFilesLength
				+ maxSizeLength
				+ maxDescriptionLength);
	}

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
	
	private static void updateMaxLengthValues(Metadata m) {
		if (maxNameLength < m.name.length()) {
			maxNameLength = m.name.length();
		}
		if (maxTimestampLength < m.timestamp.toString().length()) {
			maxTimestampLength = m.timestamp.toString().length();
		}
		if (maxOrigNameLength < m.originalName.length()) {
			maxOrigNameLength = m.originalName.length();
		}
		if (maxDescriptionLength < m.description.length()) {
			maxDescriptionLength = m.description.length();
		}
		if (maxSizeLength < String.valueOf(m.size).length()) {
			maxSizeLength = String.valueOf(m.size).length();
		}
		if (maxNumFilesLength < String.valueOf(m.numberOfFiles).length()) {
			maxNumFilesLength = String.valueOf(m.numberOfFiles).length();
		}
	}

	public void setTimestamp(Date date) {
		this.timestamp = date;
		if (maxTimestampLength < date.toString().length()) {
			maxTimestampLength = date.toString().length();
		}
	}

	public void setName(String name) {
		this.name = name;
		if (maxNameLength < name.length()) {
			maxNameLength = name.length();
		}
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
		if (maxOrigNameLength < originalName.length()) {
			maxOrigNameLength = originalName.length();
		}
	}

	public void setDescription(String description) {
		this.description = description;
		if (maxDescriptionLength < description.length()) {
			maxDescriptionLength = description.length();
		}
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
		if (maxSizeLength < String.valueOf(fileSize).length()) {
			maxSizeLength = String.valueOf(fileSize).length();
		}
	}

	public void setNumberOfFiles(int countFiles) {
		this.numberOfFiles = countFiles;
		if (maxNumFilesLength < String.valueOf(numberOfFiles).length()) {
			maxNumFilesLength = String.valueOf(numberOfFiles).length();
		}
	}
	
	public String getPrettyString() {
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		sb.append(Helper.stringToLength(getName(), Metadata.getMaxNameLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(getOriginalName(), Metadata.getMaxOrigNameLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(getTimeStamp().toString(), Metadata.getMaxTimestampLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(""+getNumberOfFiles(), Metadata.getMaxNumFilesLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(""+getFileSize(), Metadata.getMaxSizeLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(getDescription(), Metadata.getMaxDescriptionLength()));
		sb.append("|\n");
		return sb.toString();
	}

}
