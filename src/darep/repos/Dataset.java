package darep.repos;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import darep.Helper;

/**
 * Represents a Dataset consisting of BOTH the file itself and the Metadata.
 */
public class Dataset {
	private File file;
	private Metadata metadata;
	private Database db;
	
	/**
	 * Tries to read an existing Dataset inside a {@link Database}.
	 * Throws a {@link RepositoryException} if the file does not exist,
	 * or for some Reason it failed to read the Metadata.
	 * @param name
	 * @param db
	 * @throws RepositoryException
	 */
	public static Dataset readDataset(String name, Database db) throws RepositoryException {
		Dataset ds = new Dataset();
		ds.db = db;
		ds.file = new File(db.getFileDB(), name);
		if (!ds.file.exists()) {
			throw new RepositoryException("File \"" + name + "\" does not" +
					" exist in Repository");
		}
		ds.metadata = Metadata.readFile(new File(db.getMetaDB(), name));
		
		return ds;
	}
	
	// Used by static methods that create Dataset-Objects
	private Dataset() {	};
	
	/**
	 * Creates a new Dataset that can later be saved to the Database.
	 * @param file
	 * @param meta
	 * @param db
	 * @throws RepositoryException 
	 * @throws IOException 
	 */
	public static Dataset createNewDataset(File file, Metadata metadata, Database db) throws RepositoryException {
		Dataset ds = new Dataset();
		
		File canonicalFile;
		try {
			canonicalFile = file.getCanonicalFile();
		} catch (IOException e) {
			throw new RepositoryException("Could not get canonical File for " +
					file.getAbsolutePath());
		}
		
		ds.file = canonicalFile;
		ds.metadata = metadata;
		ds.db = db;
		
		metadata.setFileSize(ds.calculateFileSize());
		metadata.setNumberOfFiles(ds.countFiles());
		
		return ds;
	}
	
	private int countFiles() {
		return countFiles(file);
	}

	private int countFiles(File file) {
		if (file.isDirectory()) {
			int currNum = 0;
			for (File subFile: file.listFiles()) {
				currNum += countFiles(subFile);
			}
			return currNum;
		} else {
			return 1;
		}
	}
	
	private long calculateFileSize() {
		return calculateFileSize(file);
	}

	private long calculateFileSize(File file) {
		if (file.isDirectory()) {
			long currSize = 0L;
			for (File subFile: file.listFiles()) {
				currSize += calculateFileSize(subFile);
			}
			return currSize;
		} else {
			return file.length();
		}
	}

	/**
	 * Saves the Dataset to the Repository.
	 * Makes sure either Metadata AND files are added, or none of them.
	 * @param copyMode
	 * @return
	 */
	public boolean saveToRepository(boolean copyMode) {
		File dbdir = db.getMetaDB();
		try {
			metadata.saveInDir(dbdir);
			if (!saveFilesToRepository(copyMode)) {
				throw new RepositoryException("Could not save File");
			}
			return true;
		} catch (RepositoryException e) {
			// TODO make this work again
			//this.delete();
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	private boolean saveFilesToRepository(boolean copyMode) throws IOException {
		String name = metadata.getName();
		File fileDB = db.getFileDB();
		
		if (!file.getParentFile().equals(db.getFileDB())) {
			File fileInDB = new File(fileDB, name);
			if (copyMode) {
				try {
					db.copyFile(file, fileInDB);
					this.file = fileInDB;
					return true;
				} catch(RepositoryException e) {
					return false;
				}
				
			} else {
				if (file.renameTo(fileInDB)) {
					this.file = fileInDB;
					return true;
				} else {
					return false;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the {@link Metadata} object belonging to this Dataset
	 * @return
	 */
	public Metadata getMetadata() {
		return this.metadata;
	}
	
	/**
	 * Returns the {@link File} of the actual files of this Dataset
	 * @return
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Deletes the Dataset
	 * @return
	 */
	public boolean delete() {
		return (Helper.deleteDir(file) && metadata.delete());
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
	
}