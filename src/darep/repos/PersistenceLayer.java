package darep.repos;

public interface PersistenceLayer {

	public Dataset getDataset(String name);
	
	public Dataset[] getAllDatasets();
	
	public boolean exists(String name);
	
	public void store(Dataset ds, int options);
	
	public void replace(String name, Dataset ds);
	
	public void delete(String name);
	
}
