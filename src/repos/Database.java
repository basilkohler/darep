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
	 * @param dataset the path to the dataset to be added
	 * 
	 * @param meta the metada corresponig to 'dataset'
	 * 
	 * @param copyMode true if dataset should be copied to the db, else dataset
	 * is moved into the db
	 */
	public void add(File dataset, Metadata meta, boolean copyMode) {
		String datasetPath = filedb.getAbsolutePath() + "/" + meta.getName();
		String metadataPath = metadb.getAbsolutePath() + "/" + meta.getName();
		
		meta.saveAt(metadataPath);
		
		if (copyMode) {
			FileChannel source = null;
			FileChannel destination = null;
			try {
				dataset.createNewFile();
				source = new FileInputStream(dataset).getChannel();
				destination = new FileOutputStream(new File(datasetPath))
						.getChannel();
				destination.transferFrom(source, 0, source.size());
				source.close();
				source.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			dataset.renameTo(new File(datasetPath));
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

}
