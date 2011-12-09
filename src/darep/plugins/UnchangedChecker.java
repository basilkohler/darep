package darep.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import darep.server.CompletenessChecker;

public class UnchangedChecker implements CompletenessChecker{

	private int quietPeriodInSeconds; 
	@Override
	public File[] getCompletedFiles(File directory) {
		File files[] = directory.listFiles();
		ArrayList<File> completedFiles = new ArrayList<File>(files.length);
		long time = new Date().getTime();
		for(File f:files) {
			if(getYoungestLastModified(f) <= time - quietPeriodInSeconds * 1000) {
				completedFiles.add(f);
			}
		}
		return completedFiles.toArray(new File[0]);
	}
	

	@Override
	public void setProperty(String key, String value)
			throws IllegalArgumentException {
		String expectedKey = "quiet-period-in-seconds";
		if(key.equals(expectedKey) == false) {
			throw new IllegalArgumentException("UnchangedChecker only accepts the property " + expectedKey);
		}
		if(value == null || value.trim().length() == 0) {
			throw new IllegalArgumentException("value for " + key + " can not be null or empty");
		}	
		try {
			quietPeriodInSeconds = Integer.parseInt(value);
		} catch(Exception e) {
			throw new IllegalArgumentException(key + " must be a integer", e);
		}
		if(quietPeriodInSeconds <= 0) {
			throw new IllegalArgumentException(key + " must be greater than 0");
		}
	}
	
	private long getYoungestLastModified(File f) {
		return getYoungestLastModified(f, 0);
	}
	
	private long getYoungestLastModified(File file, long timestamp) {
		timestamp = Math.max(timestamp, file.lastModified());
		
		if (file.isDirectory()) {
			for (File f: file.listFiles()) {
				timestamp = getYoungestLastModified(f, timestamp);
			}
			return timestamp;
		}
		
		return timestamp;
	}

}
