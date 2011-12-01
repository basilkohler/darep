package darep.storage.fileStorage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import darep.$;
import darep.repos.RepositoryException;
import darep.storage.Metadata;
import darep.storage.StorageException;

public class FileDataSetTest {
	File testEnv;
	File testDir;
	File copyDir;
	static final String testEnvName = "testenv";
	static final String testDirName = "testdir";
	static final String copyDirName = "copydir";

	FileDataSet dsFile;
	FileDataSet dsFolder;
	
	@Before
	public void setUp() throws Exception {
		testEnv = new File(testEnvName);
		testEnv.mkdir();
		testDir = new File(testEnv,testDirName);
		testDir.mkdir();
		copyDir = new File(testEnv,copyDirName);
		copyDir.mkdir();
	}
	
	@After
	public void tearDown() throws Exception {
		$.deleteRecursive(testEnv);
	}
	
	@Test
	public void testCopyFileTo() throws RepositoryException, StorageException, IOException {
		File file = new File(testDir, "file");
		FileWriter wr = new FileWriter(file);
		
		try {
			wr.write("bla");
		} finally {
			wr.close();
		}
		Metadata fileMeta = new Metadata("FILE", "file", "desc file", 1, 4, file.getCanonicalPath());
		dsFile = new FileDataSet(file, fileMeta);
		
		File destination = new File(copyDir, "copied");
		assertFalse(destination.exists());
		
		dsFile.copyFileTo(destination);
		assertTrue(destination.exists());
		
		FileInputStream copiedIn = new FileInputStream(destination);
		FileInputStream fileIn = new FileInputStream(file);
		assertEquals($.streamToString(copiedIn), $.streamToString(fileIn));
	}
	
	@Test
	public void testCopyFolderTo() throws IOException, StorageException {
		File folder = new File(testDir, "folder");
		folder.mkdir();
		File subFolder = new File(folder, "subfolder");
		subFolder.mkdir();
		File subFolderFile = new File(subFolder, "file");
		subFolderFile.createNewFile();
		File subFolderFile2 = new File(subFolder, "file2");
		subFolderFile2.createNewFile();
		Metadata folderMeta = new Metadata("FOLDER", "folder", "desc folder", 3, 12, folder.getCanonicalPath());
		
		dsFolder = new FileDataSet(folder, folderMeta);
		
		File destination = new File(copyDir, "copied");
		assertFalse(destination.exists());
		
		dsFolder.copyFileTo(destination);
		assertTrue(destination.exists());
		assertTrue(destination.isDirectory());
		assertTrue($.compareFilesRecursive(folder, destination));
	}

}
