package darep.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import darep.Helper;
import darep.logger.Logger;


public class ServerLogger extends Logger{
	
	String logFilePath;
	FileWriter logWriter;
	
	public ServerLogger(String logFilePath){
		this.logFilePath = logFilePath;
		try {
			logWriter = new FileWriter(new File(logFilePath));
		} catch (IOException e) {
			System.err.println("could not create log file writer " + logFilePath);
		}
	}
	
	@Override
	public void log(String message, LOGLEVEL logLevel) {
		
		StringBuilder sb = new StringBuilder();
		
		// append timestamp
		String timeStamp = Helper.formatDate(new Date());	
		sb.append("[" + timeStamp + "] ");
		
		// append loglevel
		String status = logLevel.toString();
		if(logLevel == LOGLEVEL.SUCCESS) {
			status = "INFO";
		}
		sb.append("[" + status + "] ");
	
		// write mesage
		sb.append(message);
		sb.append("\n");
		
		// flush to file
		writeLog(sb.toString());
	}
	
	private void writeLog(String message)  {
		try {
			this.logWriter.write(message);
			this.logWriter.flush();
		} catch (IOException e) {
			System.err.println("could not write " + message + " to the logfile " + logFilePath);
		}
	}

	@Override
	public String getContent() throws IOException {
		InputStream is = new FileInputStream(new File(logFilePath));
		String result;
		try {
			result = Helper.streamToString(is);
		} finally {
			is.close();
		}
		return result;
	}
	
	@Override
	public void finalize() {
		try {
			this.logWriter.close();
		} catch (IOException e) {
			// Do nothing
		}
	}

}
