package darep.repos;

/*
import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
*/
public class MetadataTest {
/*	@Before
	public void setUp() throws Exception {
		testDir = new File("jUnitMetadataTestDir");
		testDir.mkdir();
		File testRepoFolders=new File(testDir, "testRepo/metadata");
		testRepoFolders.mkdirs();
		testMeta=new Metadata("TESTMETA", "originalname.test", "desrc.test", 1, 1000, testDir.getAbsolutePath()+"/testRepo");

	}

	@After
	public void tearDown() throws Exception {
		deleteFile(testDir);
	}
	
	@Test
	public void testSaveAt() throws RepositoryException {
		File metaFile=new File(testDir, "SAVEATTEST");
		assertTrue(!metaFile.exists());
		testMeta.saveAt(testDir.getAbsolutePath()+"/SAVEATTEST");
		assertTrue(metaFile.exists());		
	}

	@Test
	public void testSave() throws RepositoryException {
		File metaFile=new File(testDir, "testRepo/metadata/"+testMeta.getName());
		assertTrue(!metaFile.exists());
		testMeta.save();
		assertTrue(metaFile.exists());
	}
	
	@Test (expected = RepositoryException.class)
	public void testSaveWithoutName() throws RepositoryException {
		testMeta=new Metadata();
		testMeta.save();
	}
	
	@Test (expected = RepositoryException.class)
	public void testSaveWithoutOriginalName() throws RepositoryException {
		testMeta=new Metadata();
		testMeta.save();
	}
	
	@Test (expected = RepositoryException.class)
	public void testSaveWithoutPath() throws RepositoryException {
		testMeta=new Metadata();
		testMeta.save();
	}

	@Test
	public void testSaveInDir() throws RepositoryException {
		File metaFile=new File(testDir, testMeta.getName());
		assertTrue(!metaFile.exists());
		testMeta.saveInDir(testDir);
		assertTrue(metaFile.exists());	
	}
	
	@Test
	public void testReadFile() throws RepositoryException {
		File metaFile=new File(testDir, "testRepo/metadata/"+testMeta.getName());
		testMeta.save();
		assertTrue(metaFile.exists());
		Metadata fromDisk=Metadata.readFile(metaFile);
		assertNotNull(fromDisk);
	}
	
	@Test
	public void testContent() throws RepositoryException {
		File metaFile=new File(testDir, "testRepo/metadata/"+testMeta.getName());
		testMeta.save();
		assertTrue(metaFile.exists());
		Metadata fromDisk=Metadata.readFile(metaFile);
		assertNotNull(fromDisk);
		assertEquals(testMeta.getDescription(), fromDisk.getDescription());
		assertEquals(testMeta.getName(), fromDisk.getName());
		assertEquals(testMeta.getNumberOfFiles(), fromDisk.getNumberOfFiles());
		assertEquals(testMeta.getSize(), fromDisk.getSize());
		assertEquals(testMeta.getOriginalName(), fromDisk.getOriginalName());
	}

	@Test
	public void testDelete() throws RepositoryException {
		File metaFile=new File(testDir, "testRepo/metadata/"+testMeta.getName());
		assertTrue(!metaFile.exists());
		testMeta.save();
		assertTrue(metaFile.exists());
		testMeta.delete();
		assertTrue(!metaFile.exists());
	}

	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] content = file.listFiles();
			for (int i = 0; i < content.length; i++) {
				deleteFile(content[i]);
			}
		}
		file.delete();
	}*/
}
