package darep.repos;

import java.io.File;



public interface Storage {
	
	public void setRepositoryPath(File repoPath);

	public DataSet getDataSet(String name) throws StorageException;
	
	public DataSet[] getAllDataSets() throws StorageException;
	
	public boolean contains(String name) throws StorageException;
	
	public void store(DataSet ds) throws StorageException;
	
	public void replace(String name, DataSet ds) throws StorageException;
	
	public void delete(String name) throws StorageException;
	
}
