package darep.storage.fileStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import darep.Helper;
import darep.storage.DataSet;
import darep.storage.Metadata;
import darep.storage.Storage;
import darep.storage.StorageException;

/*representation of the physical database.
 *a database has two folders one for the datasets, located at 'filedb' 
 *and one for the metadatafiles, located at 'metadb'
 */
public class FileStorage implements Storage {
	
	private static final String dataDirName = "data";
	private static final String metadataDirName = "metadata";
	
	private File filedb;
	private File metadb;

	File getMetaDB() {
		return metadb;
	}

	File getFileDB() {
		return filedb;
	}

	@Override
	public void store(DataSet ds) throws StorageException {
		
		String name = ds.getMetadata().getName();
		if (getDataSet(name) != null) {
			throw new StorageException("There is already a data set named "
										+ name + " in the repository");
		}
		
		try {
			saveMetadata(ds.getMetadata());
			ds.copyFileTo(new File(filedb, name));
		} catch (IOException e) {
			throw new StorageException("Could not store Dataset", e);
		}
	}
	
	
	// TODO RepositoryException;
	private void saveMetadata(Metadata m) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(new File(metadb, m.getName()));
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		try {
			out.writeObject(m);
		} finally {
			out.close();
			fileOut.close();
		}
	}



	/*
	 * return true if the repo/db contains a dataset named 'name', else false
	 * 
	 * @ param name name of the file you are searching for
	 */
	@Override
	public boolean contains(String name) {
		File metaFile = new File(metadb, name);
		File dataFile = new File(filedb, name);
		return (metaFile.exists() && dataFile.exists());
	}

	@Override
	public void delete(String name) throws StorageException {
		File data = new File(filedb, name);
		boolean successData = Helper.deleteRecursive(data);
		
		File metadata = new File(metadb, name);
		boolean successMeta = metadata.delete();
		
		if (!successData || !successMeta) {
			throw new StorageException("There was an error while deleting " +
					"data set '" + name + "'");
		}
	}

	@Override
	public DataSet[] getAllDataSets() throws StorageException {
		String[] filenames = filedb.list();
		ArrayList<DataSet> datasets = new ArrayList<DataSet>();
		for (String name : filenames) {
			datasets.add(getDataSet(name));
		}
		return datasets.toArray(new FileDataSet[0]);
	}

//	public File export(String sourceName, String destFolder)
//			throws RepositoryException {
//		FileDataset dataset = getDataSet(sourceName);
//
//		File sourceFile = dataset.getFile();
//		if (!sourceFile.exists())
//			throw new RepositoryException("There is no File with name \'"
//					+ sourceName + "\' in this Repository");
//
//		String originalName = dataset.getMetadata().getOriginalName();
//		File destFile = new File(destFolder, originalName);
//
//		if (!(new File(destFolder).exists()))
//			throw new RepositoryException("Destination folder \'" + destFolder
//					+ "\' does not exist.");
//		if (!(new File(destFolder).isDirectory()))
//			throw new RepositoryException("Destination folder \'" + destFolder
//					+ "\' is a file and not a folder.");
//		if (destFile.exists())
//			throw new RepositoryException("There is already a file/folder"
//					+ " named \"" + originalName + "\""
//					+ " in destination folder \"" + destFolder + "\"");
//
//		copyFile(sourceFile, destFile);
//		return destFile;
//
//	}

	@Override
	public DataSet getDataSet(String name) throws StorageException {
		
		if (!contains(name)) {
			return null;
		}
		
		FileDataSet ds = new FileDataSet();
		
		Metadata meta;
		try {
			meta = getMeta(name);
		} catch (IOException e) {
			throw new StorageException("IOException", e);
		} catch (ClassNotFoundException e) {
			throw new StorageException("Could not read Metadatafile", e);
		}
		ds.setMetaData(meta);
		ds.setFile(new File(filedb, meta.getName()));
		
		return ds;
	}

	
	// TODO nicer file format
	private Metadata getMeta(String name) throws StorageException, IOException, ClassNotFoundException {
		File metadataFile = new File(metadb, name);
		
		FileInputStream fileIn = new FileInputStream(metadataFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		
		try {
			 Object obj = in.readObject();
			 if (obj instanceof Metadata) {
				 Metadata m = (Metadata) obj;
//					 updateMaxLengthValues(m);
				 return m;
			 } else {
				 throw new StorageException("File " + metadataFile.getName()
						 + "is not a Metadata-file");
			 }
		} finally {
			in.close();
			fileIn.close();
		}
			
	}

	@Override
	public void setRepositoryPath(File repoPath) {
		filedb = new File(repoPath, dataDirName);
		metadb = new File(repoPath, metadataDirName);
		if (!filedb.exists())
			filedb.mkdirs();
		if (!metadb.exists())
			metadb.mkdirs();
	}

	@Override
	public void repair() {
		// TODO Auto-generated method stub
		
	}

}
