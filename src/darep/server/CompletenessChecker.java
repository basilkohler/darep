package darep.server;

import java.io.File;

public interface CompletenessChecker {
	public File[] getCompletedFiles(File directory) throws Exception;
	public void setProperty(String key, String value) throws IllegalArgumentException;
}
