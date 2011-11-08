package darep.repos.fileStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import darep.repos.DataSet;
import darep.repos.Metadata;
import darep.repos.RepositoryException;
import darep.repos.Storage;
import darep.repos.StorageException;

/*representation of the physical database.
 *a database has two folders one for the datasets, located at 'filedb' 
 *and one for the metadatafiles, located at 'metadb'
 */
public class FileStorage implements Storage {
	private File filedb;
	private File metadb;

	/*
	 * loads(/creates if 'reposPath' doesnt exits) a db in 'reposPath' doesnt
	 * open any files/folders just loads the paths
	 * 
	 * @param reposPath the location of the repository (parent folder od
	 * 'filedb' and 'metadb')
	 */
	public FileStorage(String reposPath) {
		filedb = new File(reposPath, "datasets");
		metadb = new File(reposPath, "metadata");
		if (!filedb.exists())
			filedb.mkdirs();
		if (!metadb.exists())
			metadb.mkdirs();
	}

	File getMetaDB() {
		return metadb;
	}

	File getFileDB() {
		return filedb;
	}

	@Override
	public void store(DataSet ds) throws StorageException {
		try {
			saveMetadata(ds.getMetadata());
			ds.copyFileTo(new File(filedb, ds.getMetadata().getName()));
		} catch (IOException e) {
			throw new StorageException("Could not store Dataset", e);
		}
	}
	
	
	// TODO RepositoryException;
	private void saveMetadata(Metadata m) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(new File(metadb, m.getName()));
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		try {
			out.writeObject(this);
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
	public boolean contains(String name) throws StorageException {
		return (getDataSet(name) != null);
	}

	// TODO failsafe machen
	@Override
	public void delete(String name) throws StorageException {
		File data = new File(filedb, name);
		data.delete();
		
		File metadata = new File(metadb, name);
		metadata.delete();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replace(String name, DataSet ds) throws StorageException {
		// TODO Auto-generated method stub
		
	}

}
