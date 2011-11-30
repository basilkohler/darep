package darep.server;

import java.io.File;

public interface CompletenessChecker {
	public File[] getCompletedFiles(File directory);
}
