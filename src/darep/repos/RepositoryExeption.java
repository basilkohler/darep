package darep.repos;

public class RepositoryExeption extends Exception {

	public RepositoryExeption(String message) {
		super("ERROR: "+message);
	}
}
