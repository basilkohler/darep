package repos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/*representation of the physical database.
 *a database has two folders one for the datasets, located at 'filedb' 
 *and one for the metadatafiles, located at 'metadb'
 */
public class Database {
	File filedb;
	File metadb;

	/*
	 * loads(/creates if 'reposPath' doesnt exits) a db in 'reposPath' doesnt
	 * open any files/folders just loads the paths
	 * 
	 * @param reposPath the location of the repository (parent folder od
	 * 'filedb' and 'metadb')
	 */
	public Database(String reposPath) {
		filedb = new File(reposPath + "/datasets");
		metadb = new File(reposPath + "/metadata");
		if (!filedb.exists())
			filedb.mkdir();
		if (!metadb.exists())
			metadb.mkdir();

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
	public void add(File dataset, Metadata meta, boolean copyMode) {
		String datasetDest = filedb.getAbsolutePath() + "/" + meta.getName();
		String metadataDest = metadb.getAbsolutePath() + "/" + meta.getName();
		
		meta.saveAt(metadataDest);
		
		if (copyMode) {
			copyDataset(dataset, new File(datasetDest));
		} else {
			dataset.renameTo(new File(datasetDest));
		}
	}

	private void copyDataset(File dataset, File datasetDest) {
		if (dataset.isDirectory()) {
				datasetDest.mkdir();
			File[] content=dataset.listFiles();
			for (int i=0;i<content.length;i++) {
				copyDataset(content[i], new File(datasetDest.getAbsolutePath()+"/"+content[i].getName()));
			}
		} else {
			FileChannel source = null;
			FileChannel destination = null;
			try {
				source = new FileInputStream(dataset).getChannel();
				destination = new FileOutputStream(datasetDest)
						.getChannel();
				destination.transferFrom(source, 0, source.size());
				source.close();
				source.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * return true if the db contains a dataset named 'name', else false
	 * 
	 * @ param name name of the file you are searching for
	 */
	public boolean contains(String name) {
		File file = new File(metadb.getAbsolutePath() + "/" + name);
		if (file.exists())
			return true;
		else
			return false;
	}

	public boolean delete(String name) {
		if (contains(name)) {
			File meta=new File(metadb.getAbsolutePath()+"/"+name);
			boolean success1=meta.delete();
			boolean success2=deleteDataset(new File(filedb.getAbsolutePath()+"/"+name));
			if(success1 && success2) {
				return true;
			} else {
				System.out.println("error deleting dataset "+name);
				return false;
			}
		}else
			return false;
	}

	private boolean deleteDataset(File dataset) {
		if (dataset.isDirectory()) {
			File[] content=dataset.listFiles();
			for (int i=0;i<content.length;i++) {
				deleteDataset(content[i]);
			}
		}
		return dataset.delete();
	}

}
