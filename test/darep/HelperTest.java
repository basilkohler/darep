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

}
