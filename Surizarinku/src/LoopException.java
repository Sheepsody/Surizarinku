/**
 * Erreur utilisee dans la generation recursive
 */
public class LoopException extends Exception {
	private static final long serialVersionUID = 1L;

	public LoopException(String s) {
		super(s);
	}
}
