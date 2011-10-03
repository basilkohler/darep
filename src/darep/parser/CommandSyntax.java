/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package darep.parser;

import darep.Command;
import darep.Helper;

/**
 *
 * @author kevin
 */
public class CommandSyntax {

	Command.ActionType action;
	String[] options;
	String[] flags;
	int numArgs;

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
