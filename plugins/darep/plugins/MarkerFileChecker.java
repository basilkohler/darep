package darep.plugins;

import java.io.File;
import java.util.ArrayList;

import darep.server.CompletenessChecker;

public class MarkerFileChecker implements CompletenessChecker {
	
	private String prefix;

	@Override
	public File[] getCompletedFiles(File directory) {
		File[] files = directory.listFiles();
		ArrayList<File> complete = new ArrayList<File>();
	}

	@Override
	public void setProperty(String key, String value)
			throws IllegalArgumentException {
		if (value == null)
			throw new IllegalArgumentException("value for " + key + " can not be null");
		if (key != "prefix")
			throw new IllegalArgumentException("MarkerFileChecker only accepts the property 'prefix'");
		if (value.contains("/"))
			throw new IllegalArgumentException("Prefix can not contain '/'");
		
		this.prefix = value;
	}

}
