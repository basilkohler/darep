package darep.repos;

import java.io.File;
import java.io.IOException;

import darep.$;
import darep.storage.DataSet;
import darep.storage.Metadata;
import darep.storage.Storage;
import darep.storage.StorageException;

/**
 * Used by {@link Repository} to hand the file that should be stored in
 * the repository to the {@link Storage}. Has copyMode flag to
 * enable copying and moving to the Storage.
 *
 */
class RepositoryDataSet implements DataSet {
	
	private Metadata metaData;
	
	private File file;
	
	private boolean copyMode;
	
	void setCopyMode(boolean copyMode) {
		this.copyMode =  copyMode;
	}
	
	void setMetadata(Metadata m) {
		this.metaData = m;
	}
	
	void setFile(File f) {
		this.file = f;
	}

	@Override
	public Metadata getMetadata() {
		return metaData;
	}

	@Override
	public void copyFileTo(File path) throws StorageException {
		if (this.copyMode) {
			try {
				$.copyRecursive(file, path);
			} catch (IOException e) {
				throw new StorageException("Could not copy file " + file +
											" to path " + path, e);
			}
		} else {
			file.renameTo(path);
		}
	}

	@Override
	public Type getType() {
		if (file.exists()) {
			return file.isDirectory() ? DataSet.Type.folder : DataSet.Type.file;
		} else {
			return null;
		}
	}

}
