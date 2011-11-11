package darep.repos.fileStorage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import darep.DarepController;
import darep.Helper;
import darep.repos.Metadata;
import darep.repos.RepositoryException;
import darep.repos.StorageException;
import darep.repos.fileStorage.FileStorage;

public class FileDataSetTest {
	File testDir;
	File copyDir;
	static final String testDirName = "testdir";
	static final String copyDirName = "copydir";

	FileDataSet dsFile;
	FileDataSet dsFolder;
	
	@Before
	public void setUp() throws Exception {
		testDir = new File(testDirName);
		testDir.mkdir();
		copyDir = new File(copyDirName);
		copyDir.mkdir();
	}
	
	@After
	public void tearDown() throws Exception {
		Helper.deleteRecursive(testDir);
	}
	
	@Test
	public void testCopyFileTo() throws RepositoryException, StorageException, IOException {
		File file = new File(testDir, "file");
		FileWriter wr = new FileWriter(file);
		wr.write("bla");
		Metadata fileMeta = new Metadata("FILE", "file", "desc file", 1, 4, file.getCanonicalPath());
		dsFile = new FileDataSet(file, fileMeta);
		dsFile.copyFileTo(copyDir);
		
		assertTrue(new File(copyDir, "file").exists());
	}
	
	@Test
	public void testCopyFolderTo() throws IOException, StorageException {
		File folder = new File(testDir, "folder");
		folder.mkdir();
		File subFolder = new File(folder, "subfolder");
		subFolder.mkdir();
		File subFolderFile = new File(subFolder, "file");
		File subFolderFile2 = new File(subFolder, "file2");
		Metadata folderMeta = new Metadata("FOLDER", "folder", "desc folder", 3, 12, folder.getCanonicalPath());
		
		dsFolder = new FileDataSet(folder, folderMeta);
		
		dsFolder.copyFileTo(copyDir);
	}

}
