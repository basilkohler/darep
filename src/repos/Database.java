package repos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Database {
	File filedb;
	File metadb;

	public Database(String reposPath) {
		filedb = new File(reposPath + "/datasets");
		metadb = new File(reposPath + "/metadata");
		if (!filedb.exists())
			filedb.mkdir();
		if (!metadb.exists())
			metadb.mkdir();

	}

	// if copyMode=true, dataset is copied, else dataset is moved into the db
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
				destination = new FileOutputStream(new File(datasetPath)).getChannel();
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

	public boolean contains(String name) {
		File file = new File(metadb.getAbsolutePath() + "/" + name);
		if (file.exists())
			return true;
		else
			return false;
	}

}
