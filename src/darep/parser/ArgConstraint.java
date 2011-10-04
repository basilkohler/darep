package darep.parser;

public interface ArgConstraint {
	
	public boolean isValid(String arg);
	public String getDescription();

}
