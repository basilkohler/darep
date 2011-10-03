package darep.parser;

import darep.Command;
import darep.Helper;

/**
 * Defines the syntax for one action. There should be one CommandSyntax-object
 * for each action. Contains information such as allowed flags and options,
 * allowed number of arguments and the {@link Command.ActionType}.
 * 
 * See {@link Command} for nomenclature.
 */
public class CommandSyntax {

	private Command.ActionType action;
	private String[] options;
	private String[] flags;
	private int numArgs;

	public CommandSyntax(Command.ActionType action, int numArgs,
			String[] options, String[] flags) {
		this.action = action;
		this.numArgs = numArgs;
		this.options = options;
		this.flags = flags;
	}

	public boolean allowsOption(String name) {
		return Helper.arrayContains(name, options);
	}

	public boolean allowsFlag(String name) {
		return Helper.arrayContains(name, flags);
	}

	public Command.ActionType getAction() {
		return action;
	}

	public int getNumArgs() {
		return numArgs;
	}

}
