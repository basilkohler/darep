package darep.parser;

/**
 * Interface that controls the validity of an argument given to an option.
 * Used by the parser to determine whether the option is valid or not.
 */
public interface ArgConstraint {
	
	/**
	 * Should return true, if given arg is valid according to the constraint,
	 * and false if it violates it.
	 * @param arg
	 * @return
	 */
	public boolean isValid(String arg);
	
	/**
	 * Should return a String describing the constraint.
	 * Used for Exception messages by the parser
	 * @return
	 */
	public String getDescription();

}
