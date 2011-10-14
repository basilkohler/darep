package darep.repos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/*representation of the physical database.
 *a database has two folders one for the datasets, located at 'filedb' 
 *and one for the metadatafiles, located at 'metadb'
 */
public class Database {
	private File filedb;
	private File metadb;

	/*
	 * loads(/creates if 'reposPath' doesnt exits) a db in 'reposPath' doesnt
	 * open any files/folders just loads the paths
	 * 
	 * @param reposPath the location of the repository (parent folder od
	 * 'filedb' and 'metadb')
	 */
	public Database(String reposPath) {
		filedb = new File(reposPath, "datasets");
		metadb = new File(reposPath, "metadata");
		if (!filedb.exists())
			filedb.mkdirs();
		if (!metadb.exists())
			metadb.mkdirs();
	}
	
	public File getMetaDB() {
		return metadb;
	}
	
	public File getFileDB() {
		return filedb;
	}

	/*
	 * if 'copyMode'=true, dataset is copied, else dataset is moved into the db
	 * does not check if 'name' is already used, this happens in
	 * Repository.add()
	 * 
	 * @param dataset the (path to) the dataset to be added
	 * 
	 * @param meta the metada corresponig to 'dataset'
	 * 
	 * @param copyMode true if dataset should be copied to the db, else dataset
	 * is moved into the db
	 */
	public boolean add(File file, Metadata meta, boolean copyMode) throws RepositoryException {
		Dataset ds = Dataset.createNewDataset(file, meta, this);
		if (ds.saveToRepository(copyMode)) {
			System.out.println("The file/folder " + meta.getOriginalName()
					+ " has been successfully added to the repository"
					+ " as data set named " + meta.getName());
			return true;
		} else {
			return false;
		}
	}
	
	void copyFile(File dataset, File datasetDest) throws RepositoryException {
		if (dataset.isDirectory()) {
				datasetDest.mkdir();
			File[] content=dataset.listFiles();
			for (int i=0;i<content.length;i++) {
				copyFile(content[i], new File(datasetDest.getAbsolutePath()+"/"+content[i].getName()));
			}
		} else {
			FileChannel source = null;
			FileChannel destination = null;
			try {
				source = new FileInputStream(dataset).getChannel();
				destination = new FileOutputStream(datasetDest).getChannel();
				try {
					destination.transferFrom(source, 0, source.size());
				} finally {
					source.close();
					destination.close();
				}
			} catch (IOException e) {
				throw new RepositoryException("could not copy file "
						+ dataset.getAbsolutePath() + " to "
						+ datasetDest.getAbsolutePath());
			}
		}
	}

	/*
	 * return true if the repo/db contains a dataset named 'name', else false
	 * 
	 * @ param name name of the file you are searching for
	 */
	public boolean contains(String name) {
		return (getDataSet(name) != null);
	}

	public boolean delete(String name) throws RepositoryException {
		Dataset ds = getDataSet(name);
		if (ds == null) {
			return false;
		}
		return ds.delete();
	}
	
	public Dataset[] getAllDatasets() throws RepositoryException {
		String[] filenames = filedb.list();
		ArrayList<Dataset> datasets = new ArrayList<Dataset>();
		for (String name: filenames) {
			datasets.add(getDataSet(name));
		}
		return datasets.toArray(new Dataset[0]);
	}








	public File export(String sourceName, String destFolder) throws RepositoryException {
		Dataset dataset = getDataSet(sourceName);

		
		File sourceFile = dataset.getFile();
		if(!sourceFile.exists())
			throw new RepositoryException("There is no File with name \'" + sourceName +"\' in this Repository");





		String originalName = dataset.getMetadata().getOriginalName();
		File destFile = new File(destFolder, originalName);

		if (!(new File(destFolder).exists()))
			throw new RepositoryException("Destination folder \'"+destFolder+"\' does not exist.");
		if (!(new File(destFolder).isDirectory()))
			throw new RepositoryException("Destination folder \'"+destFolder+"\' is a file and not a folder.");
		if (destFile.exists())
			throw new RepositoryException("There is already a file/folder" +
					" named \"" + originalName + "\"" +
					" in destination folder \"" + destFolder + "\"");



		copyFile(sourceFile, destFile);
		return destFile;

	}

	public Dataset getDataSet(String string) {
		try {
			return Dataset.readDataset(string, this);
		} catch (RepositoryException e) {
			return null;
		}

	}




}
