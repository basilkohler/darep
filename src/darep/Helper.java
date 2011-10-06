package darep;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Contains some useful static methods that don't specifically belong
 * to a package.
 */
public class Helper {

	/**
	 * Returns true, if the given array contains the object "search".
	 * Uses the .equals() method for comparison.
	 * @param search
	 * @param array
	 * @return
	 */
	public static <T> boolean arrayContains(T search, T[] array) {
		for (T currItem: array) {
			if (currItem.equals(search)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tests if two arrays are permutations of each other
	 * (same length and same values, but maybe in a different order)
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static <T> boolean arrayIsPermutation(T[] a1, T[] a2) {
		boolean sameLength = (a1.length == a2.length);
		
		// create array with booleans which items are in the other array
		boolean[] cmp = new boolean[a1.length];
		for (int i = 0; i < a1.length; i++) {
			cmp[i] = arrayContains(a1[i], a2);
		}
		
		// if one value of cmp-array is false return false
		for (boolean curr: cmp) {
			if (!curr) return false;
		}
		
		// if all values of cmp-array are true and arrays have the same length,
		// return true
		return (true && sameLength);
	}

	/**
	 * Returns a pretty String with key-value pairs form a map.
	 * @param map
	 * @return
	 */
	public static String mapToString(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");

		for (Entry<?,?> entry: map.entrySet()) {
			sb.append(entry.getKey().toString());
			sb.append(": ");
			sb.append(entry.getValue().toString());
			sb.append(", ");
		}

		sb.append(" }");

		return sb.toString();
	}
	
}
