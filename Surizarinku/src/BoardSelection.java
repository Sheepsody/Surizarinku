import javax.swing.*;

/**
 * Fenetre de selection d un fichier
 */
public class BoardSelection extends JFileChooser {
	private static final long serialVersionUID = 1L;
	
	private SelectionFilter filter = SelectionFilter.SECURE;

	public BoardSelection() {
		setAcceptAllFileFilterUsed(false);
		addChoosableFileFilter(filter);
	}
}
