package parser;


public class Option {
	private OptionId id;
	private String param;

	public void setOptionId(String string) throws Exception {
			try {
				id=OptionId.valueOf(string.toUpperCase());
			} catch (Exception e) {
				//throw e; 
				throw new DRInvalidArgumentExeption("Invalid option.");
			}
	}

	public boolean hasParam() {
		switch (id) {
		case M:
		case P:	
			return false;
		default:
			return true;
		}
	}

	public void setParam(String string) throws DRInvalidArgumentExeption {
		switch (id) {
		case M:
		case P:	
			throw new DRInvalidArgumentExeption();
		case N:
			if (string.length() < 40 && string.matches("[[A-Z]  [0-9] [-]  _ ]+")) {
				param=string;
			} else {
				throw new DRInvalidArgumentExeption(id + ": Invalid Parameter");				
			}
		case D:	
			if (string.length() >= 1000 || string.matches(".*[\t\n\r].*")) {
				throw new DRInvalidArgumentExeption(id + ": Invalid Parameter");				
			} else {
				param=string;
			}
		case R:
			param=string;
		}
	}
	
	public OptionId getId() {
		return id;		
	}
	public String getParam() {
		return param;		
	}
}
