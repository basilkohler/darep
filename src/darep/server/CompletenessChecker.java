package darep.server;

import java.io.File;

/**
 * Implement this Interface to create a CompletenessChecker
 * for the data-repository Server.
 * 
 * The CompletenessChecker is used by the data-repository server to check
 * if files in a watched folder are "complete", meaning finished writing
 * and therefore ready to be added to the repository.
 *
 */
public interface CompletenessChecker {
	
	/**
	 * Should return an Array of Files which are considered complete
	 * according to this implementation of the CompletenessChecker.
	 * 
	 * If any kind of error occurs, an Exception should be thrown.
	 * The message of this Exception will be logged by the data-repository server.
	 * @param directory	The directory which should be checked for files
	 * @return
	 * @throws Exception
	 */
	public File[] getCompletedFiles(File directory) throws Exception;
	
	/**
	 * Set a property for this CompletenessChecker. The data-repository will
	 * call this method for each entry in the properties-file that begins with
	 * "completeness-checker.XXX", the key being just XXX.
	 * 
	 * Example: if there is a Property "completeness-checker.prefix = lock_",
	 * the server will call setProperty("prefix", "lock_") on the
	 * CompletenessChecker.
	 * @param key
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public void setProperty(String key, String value) throws IllegalArgumentException;
}
