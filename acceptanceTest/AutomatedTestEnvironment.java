import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import darep.Helper;

public class AutomatedTestEnvironment {
	private File playground;
	private File dist;
	private File report;
	private boolean failed;

	public AutomatedTestEnvironment(File dist, File playground, File report) {
		this.dist = dist;
		this.playground = playground;
		this.report = report;
	}

	public void openTest(int testId) {
		print("test " + testId);
		failed = false;
	}

	public ArrayList<String> check(String cmd, int expExitVal, String expOutput) {
		Result expected = new Result(expExitVal, expOutput);
		Result actual = run(cmd);
		return compare(actual, expected);
	}

	public Process checkWhileRunning(String cmd, int expExitVal, String expOutput) {
		Result expected = new Result(expExitVal, expOutput);
		Process p = getProcess(cmd); 
		//TODO: check if process still running
		return p;
	}

	public void closeTest() {
		if (!failed) {
			print("test passed");
		} else {
			failed = false;
		}
		print("");
	}

	public void print(String msg) {
		System.out.println(msg);
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(report, true);
			out = new BufferedWriter(fstream);
			out.write(msg + "\n");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		} finally {
			try {
				out.close();
				fstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private ArrayList<String> compare(Result actual, Result expected) {
		ArrayList<String> extractedVariables;
		if (actual.getExitVal() != expected.getExitVal()) {
			fail();
			print("expected exit value: " + expected.getExitVal()
					+ " actual exit value: " + actual.getExitVal());
		}
		if (expected.getExitVal() == 0) {
			extractedVariables = compareOutput(actual.getSysOut(),
					expected.getSysOut(), actual.getErrOut(), "std", "err");
		} else {
			extractedVariables = compareOutput(actual.getErrOut(),
					expected.getErrOut(), actual.getSysOut(), "err", "std");
		}
		return extractedVariables;
	}

	private ArrayList<String> compareOutput(String actOut, String expOut,
			String otherChannelOut, String channnelId, String otherChannelId) {
		ArrayList<String> extractedVariables = new ArrayList<String>();
		if (expOut.equals("-")) {
			if (actOut.length() < 1) {
				fail();
				print("expected some output on " + channnelId
						+ "out, no output found");
				if (otherChannelOut.length() > 0) {
					print("actual output on " + otherChannelId + "out:");
					print(otherChannelOut);
				}
			}
		} else {
			String[] splittedExpOut = expOut.split("<:>");
			boolean compareFailed = false;
			int i = 0;
			int ind = 0;
			while (!compareFailed && i < splittedExpOut.length) {
				int nextMatch = actOut.indexOf(splittedExpOut[i], ind);
	//			if (nextMatch < 0 || (i==0 && nextMatch>0)) {
				if (nextMatch < 0 ) {
					fail();
					compareFailed = true;
					print("expected output on " + channnelId + "out:");
					print(expOut);
					print("actual output on " + channnelId + "out:");
					print(actOut);
					if (otherChannelOut.length() > 0) {
						print("actual output on " + otherChannelId + "out:");
						print(otherChannelOut);
					}
				} else {
	//				if (nextMatch > ind) {
					if (nextMatch > ind && i>0) {
						extractedVariables.add(actOut.substring(ind, nextMatch));
					}
					ind = nextMatch + splittedExpOut[i].length();
				}
				i++;
			}
			
		}
		return extractedVariables;
	}

	public void fail() {
		if (!failed) {
			print("test failed");
		}
		failed = true;
	}

	private Result run(String cmd) {
		Process test = null;
		cmd = dist + "/data-repository " + cmd;

		try {
			test = Runtime.getRuntime().exec(cmd, null, playground);
			test.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String errOut = "";
		int c;
		try {
			while ((c = test.getErrorStream().read()) != -1) {
				errOut = errOut + (char) c;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sysOut = "";
		c = 0;
		try {
			while ((c = test.getInputStream().read()) != -1) {
				sysOut = sysOut + (char) c;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Result(test.exitValue(), sysOut, errOut);
	}
	
	private Process getProcess(String cmd) {
		Process test = null;
		cmd = dist + "/data-repository " + cmd;

		try {
			test = Runtime.getRuntime().exec(cmd, null, playground);
		}  catch (IOException e) {
			e.printStackTrace();
		}	
		return test;
	}

	public File getPlayground() {
		return playground;
	}

	public void checkExists(File file) {
		if (!file.exists())	{
			fail();
			print("The file "+file.getAbsolutePath()+" should exist.");
		}
	}

	public File makeFile(File dir, String name, String content) {
		File file=new File(dir,name);
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			file.createNewFile();
			fstream = new FileWriter(file, true);
			out = new BufferedWriter(fstream);
			out.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				fstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public void notExists(File file) {
		if (file.exists())	{
			fail();
			print("The file "+file.getAbsolutePath()+"should not exist any more.");
		}		
	}

	public File makeDir(File parentDir, String name) {
		File dir=new File(parentDir,name);
		dir.mkdir();
		return dir;
	}

	public void compareFilesRecursive(File f1, File f2) {
		try {
			if (!Helper.compareFilesRecursive(f1, f2)) {
				fail();
				print(f1.getAbsolutePath()+" should have the same content as "+f2.getAbsolutePath());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}

}
