package darep.repos;

import java.io.File;

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
	 */
	public static Dataset createNewDataset(File file, Metadata metadata, Database db) {
		Dataset ds = new Dataset();
		ds.file = file;
		ds.metadata = metadata;
		ds.db = db;
		
		metadata.setFileSize(ds.calculateFileSize());
		metadata.setNumberOfFiles(ds.countFiles());
		
		return ds;
	}
	
	private int countFiles() {
		return countFiles(file, 0);
	}

	private int countFiles(File file, int currNum) {
		if (file.isDirectory()) {
			for (File subFile: file.listFiles()) {
				currNum += countFiles(subFile, currNum);
			}
			return currNum;
		} else {
			return (currNum + 1);
		}
	}
	
	private long calculateFileSize() {
		return calculateFileSize(file, 0);
	}

	private long calculateFileSize(File file, long currSize) {
		if (file.isDirectory()) {
			for (File subFile: file.listFiles()) {
				currSize += calculateFileSize(subFile, currSize);
			}
			return currSize;
		} else {
			return (currSize + file.length());
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
			if (saveFilesToRepoisory(copyMode)) {
				return true;
			} else {
				metadata.delete();
				return false;
			}
		} catch (RepositoryException e) {
			return false;
		}
	}
	
	private boolean saveFilesToRepoisory(boolean copyMode) {
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
				+ metadata.getTimeStamp() + "\t" + metadata.getNumberOfFiles()
				+ "\t" + metadata.getSize() + "\t" + metadata.getDescription());
	}
}