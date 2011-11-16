package darep.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import darep.logger.*;


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
		
		// TODO replace SUCCESS with INFO
		// TODO append timestamp
		sb.append("[" + logLevel + "] ");
		sb.append(message);

		writeLog(sb.toString());
		// TODO close file
		try {
			logWriter.flush();
		} catch (IOException e) {
			System.err.println("could not flush to logfile in serverlog");
		}
	}
	
	private void writeLog(String message)  {
		try {
			this.logWriter.write(message);
		} catch (IOException e) {
			System.err.println("could not write " + message + " to the logfile " + logFilePath);
		}
	}

}
