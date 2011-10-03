package parser;

import java.util.ArrayList;
import java.util.Hashtable;

public class Command {
	private CommandId id;
	private Hashtable<OptionId, Option> options;
	private ArrayList<String> params;
	
	public Command() {
		options =new Hashtable<OptionId, Option>();
		params =new ArrayList<String>();
	}
	
	public void setCommandId(String str) throws Exception  {
		try {
			id=CommandId.valueOf(str.toUpperCase());
		} catch (Exception e) {
			//throw e;
			throw new DRInvalidArgumentExeption("Invalid command.");
		}
	}

	public void addOption(Option opt) throws DRInvalidArgumentExeption {
		switch (id) {
		case ADD:
			switch (opt.getId()) {
			case D:
			case M:
			case N:
				options.put(opt.getId(), opt);
				break;
			case P:
				throw new DRInvalidArgumentExeption(id + ": Illegal option");
			}
			break;
		case REPLACE:
			switch (opt.getId()) {
			case D:
			case M:
			case R:	
				options.put(opt.getId(), opt);
				break;
			case N:
			case P:
				throw new DRInvalidArgumentExeption(id + ": Illegal option");
			}
			break;
		case HELP:
			throw new DRInvalidArgumentExeption(id + ": Illegal option");
		}
	}

	public void addParams(ArrayList<String> params) throws DRInvalidArgumentExeption {
		switch (id) {
		case ADD:
		case DELETE:	
			if (params.size() !=1)
				throw new DRInvalidArgumentExeption(id + ": Wrong number of parameters. One required");
			else  
				this.params=params;
			break;
		case REPLACE:
		case EXPORT:	
			if (params.size() !=2)
				throw new DRInvalidArgumentExeption(id + ": Wrong number of parameters. Two required.");
			else  
				this.params=params;
			break;
		case LIST:	
		case HELP:
			if (params.size() !=0)
			throw new DRInvalidArgumentExeption(id + ": No parameter allowed.");
		}
	}
	
	public CommandId getId() {
		return id;
	}
	public Hashtable<OptionId, Option> getOptions() {
		return options;
	}
	public ArrayList<String> getParams() {
		return params;
	}

	public boolean IsSet(OptionId r) {
		if (options.containsKey(r)) 
			return true;
		else
			return false;
	}

	public String getOptionParam(OptionId r) {
		return options.get(r).getParam();
	}
}
