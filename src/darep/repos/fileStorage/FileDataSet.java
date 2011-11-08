package darep.repos.fileStorage;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import darep.Helper;
import darep.repos.DataSet;
import darep.repos.Metadata;
import darep.repos.StorageException;

/**
 * Represents a Dataset consisting of BOTH the file itself and the Metadata.
 */
public class FileDataSet implements DataSet {
	private File file;
	private Metadata metadata;
	
//	/**
//	 * Creates a new Dataset that can later be saved to the Database.
//	 * @param file
//	 * @param meta
//	 * @param db
//	 * @throws RepositoryException 
//	 * @throws IOException 
//	 */
//	public static FileDataSet createNewDataset(File file, Metadata metadata, FileStorage db) throws RepositoryException {
//		FileDataSet ds = new FileDataSet();
//		
//		File canonicalFile;
//		try {
//			canonicalFile = file.getCanonicalFile();
//		} catch (IOException e) {
//			throw new RepositoryException("Could not get canonical File for " +
//					file.getAbsolutePath());
//		}
//		
//		ds.file = canonicalFile;
//		ds.metadata = metadata;
//		ds.db = db;
//		
//		metadata.setFileSize(ds.calculateFileSize());
//		metadata.setNumberOfFiles(ds.countFiles());
//		
//		return ds;
//	}
	
//	private int countFiles() {
//		return countFiles(file);
//	}

//	private int countFiles(File file) {
//		if (file.isDirectory()) {
//			int currNum = 0;
//			for (File subFile: file.listFiles()) {
//				currNum += countFiles(subFile);
//			}
//			return currNum;
//		} else {
//			return 1;
//		}
//	}
	
//	private long calculateFileSize() {
//		return calculateFileSize(file);
//	}

//	private long calculateFileSize(File file) {
//		if (file.isDirectory()) {
//			long currSize = 0L;
//			for (File subFile: file.listFiles()) {
//				currSize += calculateFileSize(subFile);
//			}
//			return currSize;
//		} else {
//			return file.length();
//		}
//	}
	
	/**
	 * Returns the {@link Metadata} object belonging to this Dataset
	 * @return
	 */
	@Override
	public Metadata getMetadata() {
		return this.metadata;
	}
	
	@Override
	public String toString() {
		return (metadata.getName() + "\t" + metadata.getOriginalName() + "\t"
				+ new Date(metadata.getTimeStamp()) + "\t" + metadata.getNumberOfFiles()
				+ "\t" + metadata.getSize() + "\t" + metadata.getDescription());
	}

	public String getPrettyString() {
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		sb.append(Helper.stringToLength(metadata.getName(), Metadata.getMaxNameLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(metadata.getOriginalName(), Metadata.getMaxOrigNameLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(new Date(metadata.getTimeStamp()).toString(), Metadata.getMaxTimestampLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(""+metadata.getNumberOfFiles(), Metadata.getMaxNumFilesLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(""+metadata.getSize(), Metadata.getMaxSizeLength()));
		sb.append("|");
		sb.append(Helper.stringToLength(metadata.getDescription(), Metadata.getMaxDescriptionLength()));
		sb.append("|\n");
		return sb.toString();
	}

	@Override
	public void copyFileTo(File path) throws StorageException {
		try {
			Helper.copyRecursive(file, path);
		} catch (IOException e) {
			throw new StorageException("Could not copy file to " + path, e);
		}
	}

	void setMetaData(Metadata meta) {
		this.metadata = meta;
	}

	void setFile(File file) {
		this.file = file;
	}
	
}