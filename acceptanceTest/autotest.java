import java.io.File;
import java.io.IOException;
import java.util.Date;

import darep.$;


public class autotest {

	private static File playground;
	private static File dist;
	private static File report;

	/**
	 * @param args[0] path to parentDir of wrapper-script
	 */
	public static void main(String[] args) {
		if (args.length<1) {
			System.out.println("USAGE: java autotest <path to parentDir of wrapper-script>");
			System.exit(0);
		}
		
		prepare(args[0]);
		
		AutomatedTestEnvironment env=new AutomatedTestEnvironment(dist,playground,report);
		TestSeries series=new TestSeries(env);
		series.runTests();
		
		cleanUp();
		System.out.println("txt-file containing test-results saved at "+report.getAbsolutePath());

	}

	private static void cleanUp() {
		$.deleteRecursive(playground);	
		String defaultRep=System.getProperty("user.home")+"/.data-repository";
		$.deleteRecursive(new File(defaultRep));	
	}

	private static void prepare(String distPath) {
		report=new File("Report"+(new Date()).toString()+".txt");
		playground=new File("playground");
		playground.mkdir();
		dist = new File(distPath);
		try {
			report.createNewFile();
			report=report.getCanonicalFile();
			playground=playground.getCanonicalFile();
			dist=dist.getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String oldRep=System.getProperty("user.home")+"/.data-repository";
		$.deleteRecursive(new File(oldRep));		
	}

}
