package parser;

import java.util.ArrayList;

public class Parser {

	public Command parse(String[] args) {
		Command command = new Command();
		try {

			if (args.length == 0) {
				command.setCommandId("HELP");
			} else {
				ArrayList<String> params = new ArrayList<String>();
				command.setCommandId(args[0]); // throws exeption in case of invalid args[0]
				int currentIndex = 1;
				while (currentIndex < args.length) {
					if (args[currentIndex].charAt(0) == '-') {
						Option opt = new Option();
						opt.setOptionId(args[currentIndex].substring(1)); // throws exception
						currentIndex++;
						if (opt.hasParam()) {
							if (currentIndex < args.length) {
								opt.setParam(args[currentIndex]); // throws exception
								currentIndex++;
							} else {
								throw new DRInvalidArgumentExeption( "Missing parameter");
							}
						}
						command.addOption(opt);
					} else {
						params.add(args[currentIndex]);
						currentIndex++;
					}
				}
				command.addParams(params); // throws exception
			}

		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
		return command;
	}

}
