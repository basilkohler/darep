package darep.storage.nullStorage;

import java.io.File;
import java.util.HashMap;

import darep.storage.DataSet;
import darep.storage.Metadata;
import darep.storage.StorageException;

public class NullDataSet implements DataSet {

	private Metadata m;
	
	private DataSet.Type type;
	
	private HashMap<String, Integer> numCalls = new HashMap<String, Integer>();
	
	public NullDataSet(File file, Metadata m) {
		this.m = m;
		this.type = file.isDirectory() ? Type.folder : Type.file;
		
		numCalls.put("getMetaData", 0);
		numCalls.put("copyFileTo", 0);
		numCalls.put("getType", 0);
	}
	
	@Override
	public Metadata getMetadata() {
		increaseCall("getMetadata");
		return m;
	}

	@Override
	public void copyFileTo(File path) throws StorageException {
		increaseCall("copyFile");
		// do nothing
	}

	@Override
	public Type getType() {
		increaseCall("getType");
		return type;
	}

	public int getCalls(String method) {
		return numCalls.get(method);
	}
	
	private void increaseCall(String method) {
		Integer currCalls = numCalls.get(method);
		numCalls.put(method, ++currCalls);
	}

}
