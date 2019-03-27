import javax.swing.*;
import java.awt.*;

/**
 * Gestionnaire du jeu a proprement parler
 */
public class Surizarinku extends JApplet {
	private static final long serialVersionUID = 1L;
	private ActiveBoard aBoard;
	private Intro title = new Intro();
	private Menu menu = new Menu(this);
	private BorderLayout Layout = new BorderLayout();
	
	public void init() {
		setBackground(Color.BLACK);
		setSize(680,500);
		setLayout(new GridLayout(1,1));
	}
	
	public void start() {
		this.remove(title);
		setLayout(Layout);
		this.add(title, BorderLayout.CENTER);
		this.add(menu, BorderLayout.EAST);
		this.validate();
	}
	
	/**
	 * Generation d'une grille au hasard
	 */
	public void generate() {
		CreaDialog dial = new CreaDialog(null, "Cahier des charges", true);
		dial.showDialog();
		if (dial.data) {
			try {
				int[] dif = dial.getDif();
				Dimension d = dial.getTaille();
				String nom = dial.getNom();
				String forme = dial.getForme();
				Board b = Board.GENERATE(d, dif, nom, forme);
				this.remove(title);
				if (aBoard!=null) this.remove(this.aBoard);
				aBoard = new ActiveBoard(b);
				add(aBoard, BorderLayout.CENTER);
				validate();
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Vous n'avez pas rentré un entier!","Erreur", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/**
	 * Construction d une grille a partir d un fichier
	 */
	public void selection() {
		BoardSelection choix = new BoardSelection();
		int retour=choix.showOpenDialog(this);
		if(retour==JFileChooser.APPROVE_OPTION){
		   String path = choix.getSelectedFile().getAbsolutePath();
		   try {
			   Board b = Board.BUILD(path);
			   this.remove(title);
			   if (aBoard!=null) this.remove(this.aBoard);
			   aBoard = new ActiveBoard(b);
			   add(aBoard, BorderLayout.CENTER);
			   validate();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),"Erreur", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * Sauvegarde de la grille
	 */
	public void save() {
		if (aBoard==null) {
			JOptionPane.showMessageDialog(null, "Aucune carte n'a été chargée!","Erreur", JOptionPane.WARNING_MESSAGE);
		} else {
			String save = aBoard.toString();
			JTextArea ta = new JTextArea(Math.min(20, save.split("\n").length), 30);
			ta.setText(save);
			JOptionPane.showMessageDialog(null, new JScrollPane(ta));
		}
	}
	
	public void showSolution() {
		if (aBoard!=null) aBoard.showSolution();
	}
	
	public void stop() {
	}
	
	public void update(Graphics g) {	
		paint(g);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
	}
}