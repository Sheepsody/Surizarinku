import javax.swing.JOptionPane;

/**
 * Cette exception lance automatiquement une fenetre d alarme sa declaration
 */
public class GameException extends Exception{
	private static final long serialVersionUID = 1L;

	public GameException(String s) {
		super(s);
		JOptionPane.showMessageDialog(null, s,"Erreur", JOptionPane.WARNING_MESSAGE);
	}
}
