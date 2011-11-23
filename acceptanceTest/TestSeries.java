import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import darep.Helper;

public class TestSeries {

	private AutomatedTestEnvironment env;
	private File playground;
	private ArrayList<String> extracted;

	public TestSeries(AutomatedTestEnvironment env) {
		this.playground = env.getPlayground();
		this.env = env;
	}

	public void runTests() {

		env.openTest(2);
		env.check("blabla", 1, "-");
		env.closeTest();

		env.openTest(3);
		env.check("list", 0,
				"Name\tOriginal Name\tTimestamp\tNumber of Files\tSize\tDescription\n");
		env.closeTest();

		env.openTest(4);
		env.check(
				"list -p",
				0,
				" Name | Original Name | Timestamp | Number of Files | Size | Description |\n------+---------------+-----------+-----------------+------+-------------+\n(0 data sets, 0 bytes in total)\n");
		env.closeTest();

		env.openTest(5);
		File hello = env.makeFile(playground, "hello.txt", "hello world");
		File copies=env.makeDir(playground, "copies");
		File helloCopy = env.makeFile(copies, "hello.txt", "hello world");
		extracted = env.check("add hello.txt",
						0,
						"The file 'hello.txt' has been successfully added to the repository as data set named <:>.\n");
		String name1 = "";
		if (extracted.size() > 0)
			name1 = extracted.get(0); //TODO: check uppercase ASCII, etc
		env.checkExists(hello);
		env.checkExists(new File(System.getProperty("user.home")
				+ "/.data-repository"));
		env.closeTest();

		env.openTest(6);
		extracted = env.check("add -d this_is_a_test_dat_set hello.txt",
				0,
				"The file 'hello.txt' has been successfully added to the repository as data set named <:>.\n");
		String name2 = "";
		if (extracted.size() > 0)
			name2 = extracted.get(0); //TODO: check uppercase ASCII, etc
		if (name1.equals(name2)) { env.fail(); env.print("failed to create a different name on 2nd add of same dataset"); }
		env.checkExists(hello);
		env.closeTest();
		
		env.openTest(7);
		env.check("add -n DS-42 -m hello.txt", 
				0, 
				"The file 'hello.txt' has been successfully added to the repository as data set named DS-42.\n");
		env.notExists(hello);
		env.closeTest();
		
		env.openTest(8);
		env.check("list -p", 
				0, 
				" Name    | Original Name | Timestamp    | Number of Files | Size | Description             |\n"+
		"---------+---------------+--------------+-----------------+------+-------------------------+\n" +
		name1+" | hello.txt     | <:> |               1 |  11 |                         |\n" +
		name2+"| hello.txt     | <:> |               1 |  11 | this_is_a_test_data_set |\n" +
		"DS-42    | hello.txt     | <:> |               1 |  11 |                         |\n" +
		"(3 data sets, 33 bytes in total)\n");
		env.closeTest();
		
		env.openTest(9);
		File exports=env.makeDir(playground,"exports");
		env.check("export DS-42 exports", 
				0, 
				"The data set DS-42 (original name: hello.txt) has been successfully exported to 'exports'.\n");
		env.notExists(hello);
		env.checkExists(new File(exports,"hello.txt"));
		env.compareFilesRecursive(helloCopy, new File(exports,"hello.txt"));
		env.closeTest();
		
		env.openTest(10);
		env.check("export DS-42 exports", 
				1, 
				"ERROR: There is already a file named 'hello.txt' in destination folder 'exports'.\n");
		env.closeTest();
		
		env.openTest(11);
		File stupid=env.makeDir(playground,"my_27._data_set_with_a_really_long_&_and_stupid_name");
		File stupidCopy=env.makeDir(copies,"my_27._data_set_with_a_really_long_&_and_stupid_name");
		env.makeFile(stupid, "hi.txt", "hi universe");
		env.makeFile(stupidCopy, "hi.txt", "hi universe");
		File data=env.makeDir(stupid,"data");
		File dataCopy=env.makeDir(stupidCopy,"data");
		env.makeFile(data, "readme.txt", "nothing to read!");
		env.makeFile(dataCopy, "readme.txt", "nothing to read!");
		extracted = env.check("add -r my-repos my_27._data_set_with_a_really_long_&_and_stupid_name",
				0,
				"The folder 'my_27._data_set_with_a_really_long_&_and_stupid_name' has been successfully added to the repository as data set named <:>.\n");
		String stupidName = "";
		if (extracted.size() > 0)
			stupidName = extracted.get(0); //TODO: check uppercase ASCII, etc
		env.checkExists(new File(playground,"my-repos"));
		env.closeTest();
		
		env.openTest(12);
		extracted = env.check("add -r my-repos my_27._data_set_with_a_really_long_&_and_stupid_name",
				0,
				"The folder 'my_27._data_set_with_a_really_long_&_and_stupid_name' has been successfully added to the repository as data set named <:>.\n");
		String stupidName2 = "";
		if (extracted.size() > 0)
			stupidName2 = extracted.get(0); //TODO: check uppercase ASCII, etc		
		if (stupidName.equals(stupidName2)) { env.fail(); env.print("failed to create a different name on 2nd add of same dataset"); }
		env.closeTest();

		env.openTest(13);
		env.check("add -r my-repos -n DATA my_27._data_set_with_a_really_long_&_and_stupid_name/data",
				 0,
				 "The folder 'data' has been successfully added to the repository as data set named DATA.\n");
		env.checkExists(stupid);
		env.checkExists(data);
		env.closeTest();
		
		env.openTest(14);
		env.check("add -r my-repos -n DS-42 -m -d my_lovely_data my_27._data_set_with_a_really_long_&_and_stupid_name",
				0,
				"The folder 'my_27._data_set_with_a_really_long_&_and_stupid_name' has been successfully added to the repository as data set named DS-42.\n");
		env.notExists(stupid);
		env.closeTest();
		
		
		env.openTest(15);
		env.check("list -r my-repos",
				0,
				"Name\tOriginal Name\tTimestamp\tNumber of Files\tSize\tDescription\n"+
stupidName+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
stupidName2+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
"DATA\tdata\t<:>\t2\t16\t\n"+
"DS-42\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\tmy_lovely_data\n");
		env.closeTest(); //TODO: check list format
		
		env.openTest(16);
		env.check("export -r my-repos DS-42 exports",
				0,
				"The data set DS-42 (original name: my_27._data_set_with_a_really_long_&_and_stupid_name) has been successfully exported to 'exports'.\n");
		env.checkExists(new File(exports,"my_27._data_set_with_a_really_long_&_and_stupid_name"));
		env.compareFilesRecursive(stupidCopy, new File(exports,"my_27._data_set_with_a_really_long_&_and_stupid_name"));
		env.closeTest();
		
		env.openTest(17);
		env.check("export -r my-repos DS-42 exports", 
				1,
				"ERROR: There is already a folder named 'my_27._data_set_with_a_really_long_&_and_stupid_name' in destination folder 'exports'.\n");
		env.closeTest();
		
		env.openTest(18);
		env.check("export -r my-repos DS-42 my-repos",
				1,
				"-");
		env.closeTest();
		
		env.openTest(19);
		hello=env.makeFile(playground, "hello.txt", "");
		env.check("add -n DS-42 hello.txt",
				1,
				"ERROR: There is already a data set named DS-42 in the repository.\n");
		env.closeTest();
		
		env.openTest(20);
		env.check("add -n ds-42 hello.txt", 1, "-");
		env.closeTest();
		
		env.openTest(21);
		env.check("add -n 12345678901234567890123456789012345678901 hello.txt", 1, "-");
		env.closeTest();
		
		env.openTest(22);
		env.check("add", 1, "-");
		env.closeTest();
		
		env.openTest(23);
		env.check("add -name HELLO hello.txt", 1, "-");
		env.closeTest();
		
		env.openTest(24);
		env.check("add my-repos hello.txt", 1, "-");
		env.closeTest();
		
		env.openTest(25);
		env.check("add non-existing-file", 1, "-");
		env.closeTest();
		
		env.openTest(26);
		env.check("data-repository add -r my-repos .", 1, "-");
		env.closeTest();
		
		env.openTest(27);
		env.check("export MY-DATA exports", 
				1, 
				"ERROR: Unknown data set MY-DATA.\n");
		env.closeTest();
		
		env.openTest(28);
		env.check("export DS-42 someFolder", 
				1,
				"ERROR: Destination folder 'someFolder' does not exist.\n");
		env.closeTest();
		
		env.openTest(29);
		env.check("export DS-42 text.txt", 
				1, 
				"ERROR: Destination folder 'text.txt' is a file and not a folder.\n");
		env.closeTest();
		
		env.openTest(30);
		env.check("export DS-42", 1, "-");
		env.closeTest();
		
		env.openTest(31);
		env.check("export", 1, "-");
		env.closeTest();
		
		env.openTest(32);
		env.check("data-repository export DS-42 exports blabla", 1, "-");
		env.closeTest();
		
		env.openTest(33);
		env.check("export -r unknown DS-42 exports", 1, "-");
		env.closeTest();
		
		env.openTest(34);
		hello=env.makeFile(playground, "hello.txt", "hello universe");
		Helper.deleteRecursive(helloCopy);
		helloCopy=env.makeFile(copies, "hello.txt", "hello universe");		
		env.check("replace -d data-replaced DS-42 hello.txt", 
				0, 
				"The data set named DS-42 has been successfully replaced by the file 'hello.txt'.\n");
		env.check("list -p", 
				0, 
				" Name    | Original Name | Timestamp    | Number of Files | Size | Description             |\n"+
						"---------+---------------+--------------+-----------------+------+-------------------------+\n" +
						name1+" | hello.txt     | <:> |               1 |  11 |                         |\n" +
						name2+"| hello.txt     | <:> |               1 |  11 | this_is_a_test_data_set |\n" +
						"DS-42    | hello.txt     | <:> |               1 |  14 | data-replaced           |\n" +
						"(3 data sets, 36 bytes in total)\n");
		Helper.deleteRecursive(new File(exports,"hello.txt"));
		env.check("export DS-42 exports", 0, "-");
		env.checkExists(new File(exports,"hello.txt")); 
		env.compareFilesRecursive(helloCopy, new File(exports,"hello.txt"));
		env.closeTest();
		
		env.openTest(35);
		File mydata=env.makeDir(playground, "my-data");
		env.makeFile(mydata, "data.txt", "3.14159");
		env.check("replace -m DS-42 my-data", 
				0,
				"The data set named DS-42 has been successfully replaced by the folder 'my-data'.\n");
		env.notExists(mydata);
		env.check("list -p", 
				0, 
				" Name    | Original Name | Timestamp    | Number of Files | Size | Description             |\n"+
						"---------+---------------+--------------+-----------------+------+-------------------------+\n" +
						name1+" | hello.txt     | <:> |               1 |  11 |                         |\n" +
						name2+"| hello.txt     | <:> |               1 |  11 | this_is_a_test_data_set |\n" +
						"DS-42    | hello.txt     | <:> |               1 |  7 | data-replaced           |\n" +
						"(3 data sets, 29 bytes in total)\n");
		// TODO: pt 8 of test 35 in acceptance-tests6.pdf
		env.closeTest();
		
		env.openTest(36);
		env.check("replace DS-43 hello.txt", 1, "ERROR: Unknown data set data DS-43.\n");
		env.closeTest();
		
		env.openTest(37);
		File somedata=env.makeDir(playground, "somedata");
		File somedataCopy=env.makeDir(copies, "somedata");
		File subdata=env.makeDir(somedata, "subdata");
		File subdataCopy=env.makeDir(somedataCopy, "subdata");
		env.makeFile(subdata, "data.txt", "a b c");
		env.makeFile(subdataCopy, "data.txt", "a b c");
		env.check("replace -r my-repos DS-42 somedata", 
				0, 
				"The data set named DS-42 has been successfully replaced by the folder 'somedata'.\n");
		env.check("list -r my-repos",
				0,
				"Name\tOriginal Name\tTimestamp\tNumber of Files\tSize\tDescription\n"+
stupidName+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
stupidName2+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
"DATA\tdata\t<:>\t2\t16\t\n"+
"DS-42\tsomedata\t<:>\t3\t5\tmy_lovely_data\n");
		env.check("export -r my-repos DS-42 exports", 0, "-");
		env.checkExists(new File(exports,"somedata")); 
		env.compareFilesRecursive(somedataCopy, new File(exports,"somedata"));
		env.closeTest();
		
		env.openTest(38);
		env.check("replace -r my-repos -m DS-42 hello.txt", 
				0,
				"The data set named DS-42 has been successfully replaced by the file 'hello.txt'.\n");
		env.notExists(hello);
		env.check("list -r my-repos", 
				0, 
				"Name\tOriginal Name\tTimestamp\tNumber of Files\tSize\tDescription\n"+
						stupidName+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
						stupidName2+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
						"DATA\tdata\t<:>\t2\t16\t\n"+
						"DS-42\thello.txt\t<:>\t1\t14\tmy_lovely_data\n");
		env.closeTest();
		
		env.openTest(39);
		env.check("delete DS-42", 
				0, 
				"The data set DS-42 (original name: my-data) has been successfully removed from the repository.\n");
		env.check("list -p", 
				0, 
				" Name    | Original Name | Timestamp    | Number of Files | Size | Description             |\n"+
						"---------+---------------+--------------+-----------------+------+-------------------------+\n" +
						name1+" | hello.txt     | <:> |               1 |  11 |                         |\n" +
						name2+"| hello.txt     | <:> |               1 |  11 | this_is_a_test_data_set |\n" +
						"(2 data sets, 22 bytes in total)\n"); 
		env.closeTest();
		
		env.openTest(40);
		env.check("delete -r my-repos DS-42", 
				0, 
				"The data set DS-42 (original name: hello.txt) has been successfully removed from the repository.\n");
		env.check("list -r my-repos", 
				0, 
				"Name\tOriginal Name\tTimestamp\tNumber of Files\tSize\tDescription\n"+
						stupidName+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
						stupidName2+"\tmy_27._data_set_with_a_really_long_&_and_stupid_name\t<:>\t4\t27\t\n"+
						"DATA\tdata\t<:>\t2\t16\t\n");
		env.closeTest();
		
		env.openTest(41);
		env.check("delete DS-42", 1, "ERROR: Unknown data set DS-42.");
		env.closeTest();
		
		env.openTest(42);
		env.check("help", 0, "-"); //TODO: see pts 2, 3 in test 42 , accepatancetests6.pdf 
		env.check("", 0, "-"); //TODO: see pts 6, 7 in test 42 , accepatancetests6.pdf 
		env.closeTest();
		
		env.openTest(43);
		env.check("server", 1, "-");
		env.closeTest();
		
		env.openTest(44);
		env.check("server non-existing.properties", 1, "-");
		env.closeTest();
		
		env.openTest(45);
		env.makeFile(playground, "server.properties", "");
		env.check("server server.properties", 1, "-");
		env.closeTest();
		
		env.openTest(46);
		File properties=env.makeFile(playground, "server.properties", 
				"incoming-directory = incoming\n"+
		"html-overview = my-webserver/public-stuff/overview.html\n"+
		"log-file = server-log.txt\n"+
		"checking-interval-in-seconds = 30\n");
		env.check("server server.properties", 1, "-");
		env.closeTest();
		
		env.openTest(47);
		File incoming=env.makeDir(playground, "incoming");
		Process server=env.checkWhileRunning("server server.properties", 0, "-"); //TODO: checkWhileRunning() not fully implemented, doesnt check if process still running
		env.checkExists(new File(playground,"server-log.txt")); //TODO: check content of server-log.txt
		env.closeTest();
		
		env.openTest(48);
		env.makeFile(playground, "some.txt", "something");
		File someCopy=env.makeFile(copies, "some.txt", "something");
		env.makeFile(incoming,"some.txt", "something"); // the pdf says "copy some.txt", here it is created a 2nd time, should not make a difference?
		env.check("list -p", 
				0, 
				" Name    | Original Name | Timestamp    | Number of Files | Size | Description             |\n"+
						"---------+---------------+--------------+-----------------+------+-------------------------+\n" +
						name1+" | hello.txt     | <:> |               1 |  11 |                         |\n" +
						name2+"| hello.txt     | <:> |               1 |  11 | this_is_a_test_data_set |\n" +
						"(2 data sets, 22 bytes in total)\n"); 	
		try {
			System.out.println("waiting 30.01 seconds...");
			Thread.sleep(30010);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		extracted=env.check("list -p", 
				0, 
				" Name    | Original Name | Timestamp    | Number of Files | Size | Description             |\n"+
						"---------+---------------+--------------+-----------------+------+-------------------------+\n" +
						name1+" | hello.txt     | <:> |               1 |  11 |                         |\n" +
						name2+"| hello.txt     | <:> |               1 |  11 | this_is_a_test_data_set |\n" +
						"<:>| some.txt      | <:> |               1 |  9 |                         |\n" +
						"(3 data sets, 31 bytes in total)\n");
		if (extracted.size()>3) {
		String name3=extracted.get(extracted.size()-2);
		env.check("export "+name3+" exports", 0, "-");
		System.out.println(name3);
		//TODO: check incoming is empty, server running, server-log.txt has a new entry
				env.checkExists(new File(exports,"some.txt"));
				env.compareFilesRecursive(someCopy, new File(exports,"some.txt"));
		} else {
			env.print("this test is not yet fully implemented"); //TODO: bugfix: extracted doenst hold name3??
		}
		
		env.closeTest();
		
		env.openTest(49);
		server.destroy();
		Helper.deleteRecursive(properties);
		properties=env.makeFile(playground, "server.properties", 
				"incoming-directory = incoming\n"+
		"html-overview = my-webserver/public-stuff/overview.html\n"+
		"log-file = server-log.txt\n"+
		"checking-interval-in-seconds = blabla\n");
		env.check("server server.properties", 1, "-");
		env.closeTest();
		
		env.openTest(50);
				Helper.deleteRecursive(properties);
				properties=env.makeFile(playground, "server.properties", 
						"incoming-directory = incoming\n"+
				"html-overview = my-webserver/public-stuff/overview.html\n"+
				"log-file = server-log.txt\n"+
				"#checking-interval-in-seconds = 30\n");
				env.check("server server.properties", 1, "-");
				env.closeTest();
				
				env.openTest(51);
				Helper.deleteRecursive(properties);
				properties=env.makeFile(playground, "server.properties", 
						"incoming-directory = incoming\n"+
				"html-overview = my-webserver/public-stuff/overview.html\n"+
				"#log-file = server-log.txt\n"+
				"checking-interval-in-seconds = 30\n");
				server=env.checkWhileRunning("server server.properties", 1, "-");
				server.destroy(); //pdf doesnt say this, but it would never be killed otherwise
				//pdf says to look for 0 exitVal, but srver should not exit???
				env.checkExists(new File(System.getProperty("user.home")
						+ "/.data-repository/server.log"));
				env.closeTest();
		
				env.openTest(52);
				Helper.deleteRecursive(properties);
				properties=env.makeFile(playground, "server.properties", 
						"incoming-directory = incoming\n"+
				"#html-overview = my-webserver/public-stuff/overview.html\n"+
				"log-file = server-log.txt\n"+
				"checking-interval-in-seconds = 30\n");
				env.check("server server.properties", 1, "-");
				env.closeTest();
				
				env.openTest(53);
				Helper.deleteRecursive(properties);
				properties=env.makeFile(playground, "server.properties", 
						"#incoming-directory = incoming\n"+
				"html-overview = my-webserver/public-stuff/overview.html\n"+
				"log-file = server-log.txt\n"+
				"checking-interval-in-seconds = 30\n");
				env.check("server server.properties", 1, "-");
				env.closeTest();
			
	}

}
