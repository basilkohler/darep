package darep;

import darep.parser.CommandSyntax;
import darep.parser.ParseException;
import darep.parser.Parser;

public class DarepController {

	private static final CommandSyntax[] syntax = new CommandSyntax[] {
		new CommandSyntax(
			Command.ActionType.add,
			1,
			new String[] {"r", "n", "d"},
			new String[] {"m"}
		),
		new CommandSyntax(Command.ActionType.delete, 1, new String[] {"r"}, new String[0])
	};
		
	private Parser parser;

	public static void main(String[] args) {
		DarepController controller = new DarepController();
		controller.processCommand(args);
	}

	private void processCommand(String[] args) {
		try {
			Command command = parser.parse(args);
			System.out.println(command);
		} catch (ParseException ex) {
			System.err.println("ERROR: " + ex.getMessage());
		}
	}

	public DarepController() {
		this.parser = new Parser(DarepController.syntax);
	}

}