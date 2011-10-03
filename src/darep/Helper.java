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
