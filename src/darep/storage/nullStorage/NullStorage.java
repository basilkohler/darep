package darep.storage.nullStorage;

import java.io.File;
import java.util.HashMap;

import darep.storage.DataSet;
import darep.storage.Storage;
import darep.storage.StorageException;

/**
 * Doesn't actually store content on disk, but in memory.
 * Counts number of calls to each method.
 * Might be useful for JUnitTesting and Debugging.
 *
 */
public class NullStorage implements Storage {
	
	private HashMap<String, Integer> numCalls = new HashMap<String, Integer>();
	
	private HashMap<String, DataSet> storage = new HashMap<String, DataSet>();
	
	public NullStorage() {
		this.numCalls.put("setRepositoryPath", 0);
		this.numCalls.put("getDataSet", 0);
		this.numCalls.put("getAllDataSets", 0);
		this.numCalls.put("contains", 0);
		this.numCalls.put("store", 0);
		this.numCalls.put("delete", 0);
		this.numCalls.put("repair", 0);
	}

	@Override
	public void setRepositoryPath(File repoPath) {
		increaseCall("setRepositoryPath");
		// do nothing
	}

	@Override
	public DataSet getDataSet(String name) throws StorageException {
		increaseCall("getDataSet");
		return storage.get(name);
	}

	@Override
	public DataSet[] getAllDataSets() throws StorageException {
		increaseCall("getAllDataSets");
		return storage.values().toArray(new DataSet[0]);
	}

	@Override
	public boolean contains(String name) {
		increaseCall("contains");
		return storage.containsKey(name);
	}

	@Override
	public void store(DataSet ds) throws StorageException {
		increaseCall("store");
		storage.put(ds.getMetadata().getName(), ds);
	}

	@Override
	public void delete(String name) throws StorageException {
		increaseCall("delete");
		storage.remove(name);
	}

	@Override
	public void repair() {
		increaseCall("repair");
		// do nothing
	}
	
	public int getCalls(String method) {
		return numCalls.get(method);
	}
	
	private void increaseCall(String method) {
		Integer currCalls = numCalls.get(method);
		numCalls.put(method, ++currCalls);
	}

}
