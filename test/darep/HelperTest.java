package darep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HelperTest {

	File testEnv;
	@Before
	public void setUp() {
		testEnv = new File("testEnv");
		testEnv.mkdir();
	}
	@After
	public void tearDown() {
		$.deleteRecursive(testEnv);
	}
	@Test
	public void testArrayContains() {
		String[] test1 = new String[] {"hallo", "omg", "wtf", "sehr langer string"};
		for (String element: test1) {
			assertTrue($.arrayContains(element, test1));
		}
		String[] notIncluded = new String[] {"no", "", null, "not included long string"};
		for (String element: notIncluded) {
			assertFalse($.arrayContains(element, test1));
		}
	}
	
	@Test
	public void testArrayIsPermutation() {
		String[] test1 = new String[] {"this", "is", "a", "test"};
		String[] permutation = new String[] {"a", "this", "test", "is"};
		String[] toFew = new String[] {"test", "this", "is"};
		String[] toMany = new String[] {"test", "is", "PWN3D!", "this", "a"};
		String[] notPermutation = new String[] {"haha", "not", "same"};
		String[] empty = new String[0];
		String[] theSame = test1.clone();
		
		assertTrue($.arrayIsPermutation(test1, permutation));
		assertTrue($.arrayIsPermutation(test1, theSame));
		assertFalse($.arrayIsPermutation(test1, toFew));
		assertFalse($.arrayIsPermutation(test1, toMany));
		assertFalse($.arrayIsPermutation(test1, notPermutation));
		assertFalse($.arrayIsPermutation(test1, empty));
	}
	
	@Test
	public void testRecursiveDelete() {
		String[] files = null;
		
		// border cases
		assertTrue($.deleteRecursive(null));
		
		// delete directory with sub Directories
		String subDirName = "subdir";
		String subSubDirName = "subsubdir";
		File subDir = new File(testEnv,subDirName);
		subDir.mkdir();
		File subSubDir = new File(subDir, subSubDirName);
		subSubDir.mkdir();
		assertTrue($.deleteRecursive(subDir));
		files = testEnv.list();
		for(String f : files) {
			assertFalse(f.equals(subDirName));
		}
		
		// delete deleted Directory
		String dirName = "testdir";
		File dir = new File(testEnv, dirName);
		dir.mkdir();
		$.deleteRecursive(dir);		
		assertTrue($.deleteRecursive(dir));
		files = testEnv.list();
		for(String f : files) {
			assertFalse(f.equals(dirName));
		}
		
		// delete a file
		File file = new File(testEnv,"file");
		assertTrue($.deleteRecursive(file));
		files = testEnv.list();
		for(String f : files) {
			assertFalse(f.equals("file"));
		}
	}
	
	@Test
	public void testCopyRecursive() throws IOException {
		String subDirName = "subdir";
		String subSubDirName = "subsubdir";
		File subDir = new File(testEnv,subDirName);
		subDir.mkdir();
		File subSubDir = new File(subDir, subSubDirName);
		subSubDir.mkdir();
		
		File copyDir = new File(testEnv, "copyDir");
		copyDir.mkdir();
		
		File copy = new File(copyDir, "copy");
		
		System.out.println(copy.toString());
		
		$.copyRecursive(subDir, copy);
		assertTrue($.compareFilesRecursive(subDir, copy));		
	}
	
	@Test
	public void testCompareFilesRecursive() throws IOException {
		File original = new File(testEnv, "testOrig");
		original.mkdirs();
		File copy = new File(testEnv, "testCopy");
		copy.mkdirs();
		
		assertTrue($.compareFilesRecursive(original, original));
		assertTrue($.compareFilesRecursive(original, copy));
		
		// put some files in original folder
		File subFile = new File(original, "test");
		$.stringToFile("Hallo 42", subFile);
		File subFile2 = new File(original, "test2");
		$.stringToFile("Hallo 42", subFile2);
		// those two files should be equal
		assertTrue($.compareFilesRecursive(subFile, subFile2));
		
		// original and copy now have different content
		assertFalse($.compareFilesRecursive(original, copy));
		// file should not be equal to a folder
		assertFalse($.compareFilesRecursive(copy, subFile));
		
		// put some files in copied folder
		File copySub = new File(copy, "test");
		$.stringToFile("Hallo 42", copySub);
		File copySub2 = new File(copy, "test2");
		$.stringToFile("Hallo 43", copySub2);
		
		// slightly different file content, should NOT be equal
		assertFalse($.compareFilesRecursive(original, copy));
		
		copySub2.delete();
		File copySub3 = new File(copy, "blabla");
		$.stringToFile("Hallo 42", copySub3);
		
		// different filenames, should NOT be equal
		assertFalse($.compareFilesRecursive(original, copy));
		
		copySub3.delete();
		$.stringToFile("Hallo 42", copySub2);
		// dirs have same content, should be equal now
		$.compareFilesRecursive(original, copy);
		
		// create subdir
		File subdir = new File(original, "subdir");
		subdir.mkdirs();
		assertFalse($.compareFilesRecursive(original, copy));
		
		File copySubdir = new File(copy, "subdir");
		copySubdir.mkdirs();
		assertTrue($.compareFilesRecursive(original, copy));
		
	}
	
	@Test
	public void testStringToLength() {
		assertEquals($.stringToLength("0123456789", 10), "0123456789");
		assertEquals($.stringToLength("0123456789", 5), "01234");
		assertEquals($.stringToLength("", 10).length(), 10);
		assertEquals($.stringToLength("abc",5, $.ALIGN_LEFT), "abc  ");
		assertEquals($.stringToLength("abc",5, $.ALIGN_RIGHT), "  abc");
		assertEquals($.stringToLength("", 3), "   ");
		assertEquals($.stringToLength(null, 10), null);
		assertEquals($.stringToLength("bla", -10), null);
	}

}
