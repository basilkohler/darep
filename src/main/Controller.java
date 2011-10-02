package main;
import parser.Command;
import parser.OptionId;
import parser.Parser;


public class Controller {
	private Parser parser;
	private Command  command;
	
	public Controller() {
		parser=new Parser();
	}
	
	public void process(String[] args) {
		command=parser.parse(args);
		System.out.println(command.getId());
	//		System.out.println(command.getOptions().get(OptionId.N).getId());
	//		System.out.println(command.getOptions().get(OptionId.N).getParam());
	//	System.out.println(command.getParams().get(0));
		
	}

}
