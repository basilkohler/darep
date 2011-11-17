package darep;

import org.junit.Before;
import org.junit.Test;

import darep.logger.Logger;

public class DarepControllerTest {
	
	private DarepController controller;
	private Logger logger;
	
	@Before
	public void setUp() {
		controller = new DarepController();
		logger = controller.getLogger();
	}

	@Test
	public void testPrintHelp() {
		
	}
	
}
