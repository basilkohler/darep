/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package darep;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class Command {

	public enum ActionType {
		add,
		replace,
		delete,
		export,
		list
	}

	private ActionType action;

	private Map<String, String> options;

	private String[] flags;

	private String[] parameters;

	public Command(ActionType action, String[] arguments, 
			Map<String, String> options, String[] flags) {

		this.action = action;
		this.options = options;
		this.flags = flags;
		this.parameters = arguments;
	}

	public ActionType getAction() {
		return action;
	}

	public String[] getParams() {
		return parameters;
	}

	public boolean hasFlag(String name) {
		return Helper.arrayContains(name, flags);
	}
	
	public boolean isSet(String key) {
		return (hasFlag(key)
				|| options.containsKey(key));
	}

	public String getOptionParam(String key) {
		return options.get(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Action:\t\t");
		sb.append(action.toString());

		sb.append("\n");
		
		sb.append("Arguments:\t");
		sb.append(Arrays.toString(parameters));
		
		sb.append("\n");

		sb.append("flags:\t\t");
		sb.append(Arrays.toString(flags));

		sb.append("\n");
		
		sb.append("options:\t");
		sb.append(Helper.mapToString(options));

		return sb.toString();
	}
	
}
