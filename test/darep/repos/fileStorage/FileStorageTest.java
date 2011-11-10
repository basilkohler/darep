package darep.repos.fileStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import darep.Helper;
import darep.repos.Metadata;
import darep.repos.RepositoryException;
import darep.repos.StorageException;
import darep.repos.fileStorage.FileStorage;

public class FileStorageTest {
	FileStorage db;
	File testDir;
	File testRepo;
	File testData;
	FileDataSet testDataSet;
	Metadata meta;
	
	File filedb;
	File metadb;

	@Before
	public void setUp() throws Exception {
		testDir = new File("jUnitDatabaseTestDir");
		testDir.mkdir();
		testData = new File(testDir.getAbsolutePath(), "/testDataSet");
		testData.createNewFile();
		fillWithTestContent(testData);
		
		testDataSet = new FileDataSet();
		
		testRepo = new File(testDir.getAbsolutePath(), "/testRepo");
		testRepo.mkdir();
		
		filedb = new File(testRepo, "datasets");
		metadb = new File(testRepo, "metadata");
		
		meta = new Metadata("TESTDATASET", "testDataSet", "", 0, 0,
				testRepo.getAbsolutePath());
		
		testDataSet.setFile(testData);
		testDataSet.setMetaData(meta);
		
		db = new FileStorage(testRepo.getAbsolutePath());
	}

	@After
	public void tearDown() throws Exception {
		Helper.deleteRecursive(testDir);
	}

	@Test
	public void testAddCopyFile() throws RepositoryException, IOException, StorageException {
		
		db.store(testDataSet);
		
		File expectedDataset = new File(filedb, "TESTDATASET");
		File expectedMeta = new File(metadb, "TESTDATASET");
	
		assertTrue(expectedDataset.exists());
		assertTrue(expectedMeta.exists());
		assertTrue(testData.exists());
		compareContents(expectedDataset, testData);
	}
	
	private void compareContents(File expectedDataset, File testDataSet) throws IOException {
		FileReader frA = new FileReader(expectedDataset);
		FileReader frB = new FileReader(testDataSet);
        BufferedReader readerA = new BufferedReader(frA);
        BufferedReader readerB = new BufferedReader(frB);

        try {
			String expectedContent = getReaderContent(readerA);
			String testContent = getReaderContent(readerB);
	        
	        assertEquals(expectedContent, testContent);
        } finally {
        	readerA.close();
        	readerB.close();
        	frA.close();
        	frB.close();
        }
	}

	private String getReaderContent(BufferedReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		String str = reader.readLine();
		while (str != null) {
			sb.append(str + "\n");
			str = reader.readLine();
		}
		return sb.toString();
	}

	@Test
	public void testAddCopyFolder() throws IOException, RepositoryException, StorageException {
		File expectedData = new File(filedb, "TESTDATADIR");
		File expectedMeta = new File(metadb, "TESTDATADIR");
		File sampleFolder = createSampleFolder();
		meta = new Metadata("TESTDATADIR", sampleFolder.getName(), "", 0, 0,
				testRepo.getAbsolutePath());

		db.store(testDataSet);
	
		assertTrue(expectedData.exists());
		assertTrue(expectedMeta.exists());
		assertTrue(testData.exists());
		checkFolderContents(expectedData);
	}
	
	private void checkFolderContents(File dirToCheck) {
		File fileToCheck;
		for (int i=0;i<4;i++) {
			dirToCheck=new File(dirToCheck.getAbsolutePath()+"/"+i);
			assertEquals(true, dirToCheck.exists());
			for (int j=0;j<4;j++) {
				fileToCheck=new File(dirToCheck.getAbsolutePath()+"/file"+j);
				assertEquals(true, fileToCheck.exists());
			}
		}
	}

	private File createSampleFolder() throws IOException {
		File sampleFolder=new File(testDir.getAbsolutePath()+"/testDataDir");
		sampleFolder.mkdir();
		File sampleFile;
		for (int i=0;i<4;i++) {
			sampleFolder=new File(sampleFolder.getAbsolutePath()+"/"+i);
			sampleFolder.mkdir();
			for (int j=0;j<4;j++) {
				sampleFile=new File(sampleFolder.getAbsolutePath()+"/file"+j);
				sampleFile.createNewFile();
			}
		}
		return new File(testDir.getAbsolutePath()+"/testDataDir");
	}
	
	private void fillWithTestContent(File testDataSet) throws IOException {
		  BufferedWriter out = new BufferedWriter(new FileWriter(testDataSet));
		    out.write("lorem ipsum");
		    out.newLine();
		    out.write("http://www.youtube.com/watch?v=gy5g33S0Gzo&feature=player_embedded#!");
		    out.close();
	}

	@Test
	public void testAddMove() throws RepositoryException, StorageException {
		File expectedDataset = new File(testRepo + "/datasets/TESTDATASET");
		File expectedMeta = new File(testRepo + "/metadata/TESTDATASET");

		db.store(testDataSet);
		
		assertTrue(expectedDataset.exists());
		assertTrue(expectedMeta.exists());
		assertFalse(testData.exists());
	}

	@Test
	public void testAddMultiple() throws RepositoryException, StorageException {
		File expectedDataset = null;
		File expectedMeta = null;
		for (int i = 0; i < 4; i++) {
			meta.setName("TESTDATASET" + i);
			
			db.store(testDataSet);
		
			for (int j = 0; j <= i; j++) {
				expectedDataset = new File(filedb, "TESTDATASET" + j);
				expectedMeta = new File(metadb, "TESTDATASET" + j);
				assertTrue(expectedDataset.exists());
				assertTrue(expectedMeta.exists());
			}
		}
	}

	@Test
	public void testDelete() throws IOException, RepositoryException, StorageException {
		db.store(testDataSet);
		
		File existingDataset = new File(testRepo + "/datasets/TESTDATASET");
		File existingMetadata = new File(testRepo + "/metadata/TESTDATASET");

		assertTrue(existingDataset.exists());
		assertTrue(existingMetadata.exists());

		db.delete("TESTDATASET");
	
		assertFalse(existingDataset.exists());
		assertFalse(existingMetadata.exists());
	}

}
