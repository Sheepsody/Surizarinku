import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Menu se situant a droite de la fenetre du jeu
 */
public class Menu extends JPanel implements ActionListener {
	public static final long serialVersionUID = 1;
	
	private Surizarinku slither;
	private JButton load = new JButton("Charger une partie");
	private JButton settings = new JButton("Paramètres");
	private JButton create = new JButton("Créer une nouvelle grille");
	private JButton close = new JButton("Quitter");
	private JButton show = new JButton("Montrer la solution");
	private JButton save = new JButton("Sauvegarder la grille");
	
	public Menu(Surizarinku s) {
		super();
		this.setLayout(new GridLayout(5,1));
		this.slither=s;
		this.home();
	}
	
	public void home() {
		this.add(this.load); load.addActionListener(this);
		this.add(this.create); create.addActionListener(this);
		this.add(this.show); show.addActionListener(this);
		this.add(this.save); save.addActionListener(this);
		this.add(this.close); close.addActionListener(this);
		this.validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object c = e.getSource();
		if (c instanceof JButton) {
			JButton j = (JButton) c;
			if (j==load) {
				slither.selection();
			} else if (j==create) {
				slither.generate();
			} else if (j==settings) {
				this.setBackground(Color.YELLOW);
			} else if (j==close) {
				slither.stop();
			} else if (j==show) {
				slither.showSolution();
			} else if (j==save) {
				slither.save();
			}
		}
	}
}
