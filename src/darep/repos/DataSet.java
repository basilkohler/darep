package darep.repos;

import java.io.File;

public interface DataSet {
	
	public enum Type {
		folder,
		file
	}
	
	public Metadata getMetadata();
	
	public void copyFileTo(File path) throws StorageException;
	
	public Type getType();

}
