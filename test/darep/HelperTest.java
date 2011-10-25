package darep;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

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
		testEnv.delete();
	}
	@Test
	public void testArrayContains() {
		String[] test1 = new String[] {"hallo", "omg", "wtf", "sehr langer string"};
		for (String element: test1) {
			assertTrue(Helper.arrayContains(element, test1));
		}
		String[] notIncluded = new String[] {"no", "", null, "not included long string"};
		for (String element: notIncluded) {
			assertFalse(Helper.arrayContains(element, test1));
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
		
		assertTrue(Helper.arrayIsPermutation(test1, permutation));
		assertTrue(Helper.arrayIsPermutation(test1, theSame));
		assertFalse(Helper.arrayIsPermutation(test1, toFew));
		assertFalse(Helper.arrayIsPermutation(test1, toMany));
		assertFalse(Helper.arrayIsPermutation(test1, notPermutation));
		assertFalse(Helper.arrayIsPermutation(test1, empty));
	}
	
	// TODO test Helper.deleteDir()
	@Test
	public void testDeleteDir() {
		assertEquals(Helper.deleteDir(null), true);
		assertEquals(Helper.deleteDir(new File(testEnv, "nodir")), true);
		String dirName = "testdir";
		File dir = new File(testEnv, dirName);
		dir.mkdir();
		Helper.deleteDir(dir);
		assertEquals(Helper.deleteDir(dir), true);
		String[] files = testEnv.list();
		for(String f : files) {
			assertFalse(f.equals(dirName));
		}
	}
	@Test
	public void testStringToLength() {
		assertEquals(Helper.stringToLength("0123456789", 10), "0123456789");
		assertEquals(Helper.stringToLength("0123456789", 5), "01234");
		assertEquals(Helper.stringToLength("", 10).length(), 10);
		assertEquals(Helper.stringToLength("abc",5), "  abc");
		assertEquals(Helper.stringToLength("", 3), "   ");
		assertEquals(Helper.stringToLength(null, 10), null);
		assertEquals(Helper.stringToLength("bla", -10), null);
	}

}
