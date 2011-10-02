package parser;

public class DRInvalidArgumentExeption extends Exception {
	public DRInvalidArgumentExeption() {
		super("Invalid argument format.\nTry 'data-repository [help]'");
	}
	public DRInvalidArgumentExeption(String message) {
		super(message + "\nTry 'data-repository [help]'");
	}
}
