package darep.repos;

import java.io.File;

public interface DataSet {
	
	public Metadata getMetadata();
	
	public void copyFileTo(File path) throws StorageException;

}
