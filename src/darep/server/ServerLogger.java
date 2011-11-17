package darep.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
		
		// append timestamp
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String timeStamp = sdf.format(cal.getTime());	
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
