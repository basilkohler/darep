package main;

import parser.Command;
import parser.CommandId;
import parser.OptionId;
import parser.Parser;
import repos.Repository;

public class Controller {
	private Parser parser;
	private Command command;
	private Repository repository;

	public Controller() {
		parser = new Parser();
	}

	public void process(String[] args) {
		command = parser.parse(args);
		if (command.IsSet(OptionId.R))
			repository = new Repository(command.getOptionParam(OptionId.R));
		else
			repository = new Repository();
		switch (command.getId()) {
		case HELP:
			printHelp();
			System.exit(0);
			break;
		case ADD:
			repository.add(command);
			break;
		case DELETE:
		case REPLACE:
		case LIST:
		case EXPORT:
			System.out.println(command.getId() + ": not yet implemented");
		}
	}

	private void printHelp() {
		System.out.println("sorry i cant help you yet, see specifications.pdf");
	}

}
