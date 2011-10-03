/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package darep;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author kevin
 */
public class Helper {

	public static <T> boolean arrayContains(T search, T[] array) {
		for (T currItem: array) {
			if (currItem.equals(search)) {
				return true;
			}
		}
		return false;
	}

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
