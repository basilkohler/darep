package darep.parser;

import darep.DarepException;

/**
 * Thrown by the Parser when a syntax error occurs
 */
public class ParseException extends DarepException {

	private static final long serialVersionUID = 1L;

	public ParseException(String string) {
		super("ERROR: " + string);
	}
	
}
