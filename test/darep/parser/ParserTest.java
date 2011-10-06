package darep.parser;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import darep.Command;
import darep.Command.ActionType;
import darep.DarepController;
import darep.parser.ParseException;
import darep.parser.Parser;

/**
 * tests the Parser.parse() function with given args.
 * if the parse function succeeds the correct actual Command gets created and 
 * compared with the expected Command from parse(). Helper functions simplify this implementation.
 * if the parse function should fail it gets tested for a thrown ParseException.
 * @author basil
 *
 */
public class ParserTest {
	private static final Parser parser = new Parser(DarepController.syntax, DarepController.constraints, ActionType.help);

	private ArrayList<String> args;
	private ActionType action;
	private ArrayList<String> parameters;
	private HashMap<String, String> options;
	private ArrayList<String> flags;

	public ParserTest() {
		action = null;
		args = new ArrayList<String>();
		options = new HashMap<String, String>();
		parameters = new ArrayList<String>();
		flags = new ArrayList<String>();
	}
	
	// general and special cases
	@Test (expected = ParseException.class)
	public void testParseAddNoParameter() throws ParseException {
		String[] args = {"add"};
		parser.parse(args);
	}
	
	@Test (expected = ParseException.class)
	public void testParseAddTooManyParameters() throws ParseException {
		String[] args = {"add", "path", "notallowed"};
		parser.parse(args);
	}
	
	@Test (expected = ParseException.class)
	public void testParseNotAllowedAction() throws ParseException {
		String[] args = {"notallowed"};
		parser.parse(args);
	}
	
	@Test (expected = ParseException.class)
	public void testParseOptionTooManyArguments() throws ParseException {
		String[] args = {"add", "-d", "description", "notallwoed", "path"};
		parser.parse(args);
	}
	
	@Test
	public void testParseNoArgs() throws ParseException {
		String[] args = {};
		assertEquals(ActionType.help, parser.parse(args).getAction());
	}

	// default cases for add command
	@Test
	public void testParseAdd() throws ParseException {
		setAction(ActionType.add);
		addParameter("path");
		
		test();
	}

	@Test
	public void testParseAddDescription() throws ParseException {
		setAction(ActionType.add);
		addOption("d", "bla");
		addParameter("path");
		
		test();
	}
	
	@Test
	public void testParseAddMove() throws ParseException {
		setAction(ActionType.add);
		addFlag("m");
		addParameter("path");
		
		test();		
	}

	@Test
	public void testParseAddRepository() throws ParseException {
		setAction(ActionType.add);
		addOption("r", "repository");
		addParameter("path");
		
		test();		
	}	

	@Test
	public void testParseAddName() throws ParseException {
		setAction(ActionType.add);
		addOption("n", "DATASETNAME");
		addParameter("path");
		
		test();		
	}
	
	@Test
	public void testParseAddFullDefaultOrder() throws ParseException {
		setAction(ActionType.add);
		addOption("n", "DATASETNAME");
		addOption("r", "repository");
		addOption("d", "description");
		addFlag("m");
		addParameter("path");
		
		test();			
	}
	
	@Test
	public void testParseAddFullOtherOrder() throws ParseException {
		setAction(ActionType.add);
		addParameter("path");
		addOption("n", "DATASETNAME");
		addOption("r", "repository");
		addOption("d", "description");
		addFlag("m");
		
		test();			
	}
	
	// error cases for add
	@Test (expected = ParseException.class)
	public void testParseAddTooLongDescription() throws ParseException{
		setAction(ActionType.add);
		addOption("d", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feug");
		addParameter("path");
		
		test();
	}
	
	@Test (expected = ParseException.class)
	public void testParseAddLowerCaseDatasetName() throws ParseException{
		setAction(ActionType.add);
		addOption("n", "datasetname");
		addParameter("path");
		
		test();
	}
	
	@Test (expected = ParseException.class)
	public void testParseAddSystemSymbolInDatasetName() throws ParseException{
		setAction(ActionType.add);
		addOption("n", "name_with_&_not_allowed");
		addParameter("path");
		
		test();
	}
	
	@Test (expected = ParseException.class)
	public void testParseAddSystemSymbolInDescription() throws ParseException{
		setAction(ActionType.add);
		addOption("d", "\tdescription_not_allowed");
		addParameter("path");
		
		test();
	}
	
	@Test (expected = ParseException.class)
	public void testParseTooManyArguments() throws ParseException {
		String[] args = {"add", "-d", "description", "not_allowed", "path"};
		parser.parse(args);
	}
	
	// default test cases for help
	@Test
	public void testParseHelp() throws ParseException {
		setAction(ActionType.help);
		test();
	}
	
	// helper functions for default cases
	private Command getExpectedCommand() throws ParseException {
		String[] args = this.args.toArray(new String[0]);
		return parser.parse(args);
	}
	
	private Command getActualCommand(){
		return new Command(action, parameters.toArray(new String[0]), options, flags.toArray(new String[0]));
	}
	
	private void test() throws ParseException {
		Command actual = getActualCommand();
		Command expected = getExpectedCommand();
		
		assertEquals(expected, actual);
	}
	
	private void setAction(ActionType action) {
		this.action = action;
		if(action != null)
			args.add(action.toString());
	}
	
	private void addOption(String arg0, String arg1) {
		options.put(arg0, arg1);
		args.add("-" + arg0);
		args.add(arg1);
		
	}
	
	private void addFlag(String flag) {
		this.flags.add(flag);
		args.add("-" + flag);
	}
	
	private void addParameter(String parameter) {
		this.parameters.add(parameter);
		args.add(parameter);
	}
}