package darep;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelperTest {

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

}
