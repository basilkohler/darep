package darep.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import darep.Command;

/**
 * Parses an args-array like the one given to the main-method and produces
 * a {@link Command} object. Uses a {@link CommandSyntax}-array to parse it.
 */
public class Parser {

	private CommandSyntax[] allowedSyntax;
	private Map<String, ArgConstraint> constraints;

	public Parser(CommandSyntax[] allowedSyntax, Map<String, ArgConstraint> constraints) {
		this.allowedSyntax = allowedSyntax;
		this.constraints = constraints;
	}

	public Command parse(String[] args) throws ParseException {

		// determine action and get syntax for it
		CommandSyntax syntax = parseAction(args);
		Command.ActionType action = syntax.getAction();

		// Make List from Array without action-name
		List<String> argList = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));

		// Datastructures to hand over to Command-Object
		ArrayList<String> flags = new ArrayList<String>();
		HashMap<String, String> options = new HashMap<String, String>();
		ArrayList<String> arguments = new ArrayList<String>();

		// Number of arguments that are passed
		int numArgs = 0;

		// Iterate over args List
		Iterator<String> iterator = argList.iterator();
		while (iterator.hasNext()) {
			String currentArg = iterator.next();

			// If arg starts with "-" it is a flag or an option
			if (currentArg.startsWith("-")) {

				String optName = currentArg.substring(1);

				// Add Option
				if (syntax.allowsOption(optName)) {
					
					this.insertOptionToMap(optName, iterator.next(), options);

				// Add Flag
				} else if (syntax.allowsFlag(optName)) {
					flags.add(optName);

				// option/flag not allowed => throw Exception
				} else {
					throw new ParseException("Action \"" + action
							+ "\" doesn't allow option \"" + currentArg + "\"");
				}

			// String doesn't start with "-" => normal argument
			} else {
				numArgs++;
				arguments.add(currentArg);
			}
		}

		// If given arguments doesn't match syntax, throw error
		if (numArgs != syntax.getNumArgs()) {
			throw new ParseException("Action \"" + action + "\" "
					+ "takes " + syntax.getNumArgs() + " arguments");
		}

		// Make variables that can be passed to the Command-Constructor
		String[] argumentsArray = arguments.toArray(new String[0]);
		String[] flagsArray = flags.toArray(new String[0]);
		
		return new Command(action, argumentsArray, options, flagsArray);
		
	}

	private void insertOptionToMap(String optName, String value, Map<String, String> map) throws ParseException {
		
		// check for constraints
		ArgConstraint constraint = constraints.get(optName);
		if ((constraint != null)
				&& !constraint.isValid(value)) {
			throw new ParseException("Option \"" + optName + "\" was given" +
					" an invalid value (" + constraint.getDescription() + ")");
		}
		
		// check if already included
		if (map.containsKey(optName)) {
			throw new ParseException("Option \"" + optName + "\" was given" +
					" more than once");
		}
		
		map.put(optName, value);
	}

	private CommandSyntax parseAction(String[] args) 
							throws ParseException {

		if (args.length < 1) {
			throw new ParseException("No action specified");
		}
		
		// determine action name 
		String actionName = args[0];

		// get CommandSyntax
		CommandSyntax syntax = getSyntax(actionName);
		if (syntax == null) {
			throw new ParseException("Unknown action \""
						+ actionName +"\"");
		}

		return syntax;
	}

	private CommandSyntax getSyntax(String actionName) {
		Command.ActionType action = Command.ActionType.valueOf(actionName);
		for (CommandSyntax currSyntax: allowedSyntax) {
			if (currSyntax.getAction() == action) {
				return currSyntax;
			}
		}
		return null;
	}

}
