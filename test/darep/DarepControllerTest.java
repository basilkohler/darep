package darep;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import darep.logger.Logger;
import darep.parser.ParseException;
import darep.repos.RepositoryException;
import darep.server.ServerException;

public class DarepControllerTest {
	
	private DarepController controller;
	private Logger logger;
	
	@Before
	public void setUp() {
		controller = new DarepController();
		logger = controller.getLogger();
	}

	@Test
	public void testPrintHelp() throws ParseException, RepositoryException, ServerException, FileNotFoundException, IOException {
		
		InputStream is = new FileInputStream(new File("./resources/help.txt"));
		String expected = Helper.streamToString(is).trim();
		
		controller.processCommand(makeArgs("help"));
		assertEquals(logger.getContent().trim(), expected);
	}
	
	private String[] makeArgs(String cmd) {
		return cmd.split("\\s+");
	}
	
}
