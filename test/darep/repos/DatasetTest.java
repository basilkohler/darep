package darep.repos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import darep.DarepController;
import darep.Helper;

public class DatasetTest {
	
	private DarepController darep = new DarepController();
	private Database database;
	private File testdir = new File("testdir");
	private File testFile;
	private String repoName = "testrepo";
	
	private final String testFileName = "testfile.txt";

	@Before
	public void setUp() throws Exception {
		createTestFiles();
		darep.processCommand(getArgs("add " + testFile.getPath()));
	}
	
	@After
	public void tearDown() throws Exception {
		Helper.deleteDir(testdir);
		Helper.deleteDir(new File(repoName));
	}

	@Test
	public void testReadGetDataset() throws RepositoryException {
		database = new Database(repoName);
		database.getDataSet("TESTFILE.TXT");
	}
	
	private String[] getArgs(String str) {
		return (str + " -r " + repoName).split(" "); 
	}
	
	private void createTestFiles() {
		testdir.mkdirs();
		testFile = new File(testdir, testFileName);
		try {
			testFile.createNewFile();
			FileWriter writer = new FileWriter(testFile);
			try {
				writer.write("FU METADATA");
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
