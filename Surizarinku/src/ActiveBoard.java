import java.awt.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.awt.event.*;

/**
 * Version visuelle du pavage du jeu
 * Cette classe réunit l'ensemble des composants du jeu gère leur affichage, notamment suite au redimentionnement
 */
public class ActiveBoard extends Panel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private Board board;
	private double marge = 2; // gestion de la marge lors de l'affichage, comptée en nombre d'hexagones
	public final static Color COLOR_BACKGROUND = Color.BLACK;
	private boolean solved = false;
	private long time = System.currentTimeMillis(); // temps de référence
	private int nbClick=0; // nombre de clicks effectués
	
	public ActiveBoard(Board b) {
		super();
		setLayout(null);
		setBackground(COLOR_BACKGROUND);
		board = b;
		this.setVisible(true);
		this.addMouseListener(this);
		// initialisation des composants immuables de la partie
		Iterator<Link> iteL = board.web.iterator();
		ActiveLink L;
		while(iteL.hasNext()) {
			L = new ActiveLink(0,0,0,iteL.next(),0,0); // il n'est pas nécéssaire de mettre des dimensions correctes, elles sont de toute façon recalculées lors de l'appel de la fonction paint()
			add(L);
		}
		Iterator<Hexagon> ite = board.grid.iterator();
		ActiveHexagon A;
		while (ite.hasNext()){
			A = new ActiveHexagon(0, 0, 0, ite.next(),0,0);
			add(A);
		}
		validate();
	}
	
	public void paint(Graphics g) {
		int height=this.getSize().height;
		int width=this.getSize().width;
		
		// Calcul des dimensions de bases, utilisée par la suite pour adapter les composants
		double rayon; double x0; double y0;
		double rh = height/(4.0*marge + board.getSize()[1]*1.5 + 1);
		double rw = width/(4.0*marge + Math.sqrt(3)*board.getSize()[0]);
		if (rh<rw) rayon = rh;
		else rayon = rw;
		x0 = width/2 - rayon*Math.sqrt(3)/2*board.getSize()[0];
		y0 = height/2 - rayon*(1.5*board.getSize()[1]+1)/2;
		
		// Cette liste permet de suivre les ActivePath contenus, pour éviter les duplicats
		boolean[] check = new boolean[board.union.size()];
		for (int i=0 ; i<check.length ; i++) check[i]=false;
		
		// On fait évoluer chacun des composants en fonction de leur type
		Component[] plop = this.getComponents();
		for (int i=0 ; i<plop.length ; i++) {
			Component plip = plop[i];
			if (plip instanceof ActiveHexagon) {
				ActiveHexagon A = (ActiveHexagon) plip;
				A.updateActiveHexagon(rayon,x0,y0,width,height);
			} else if (plip instanceof ActiveLink) {
				ActiveLink A = (ActiveLink) plip;
				A.updateActiveLink(rayon,x0,y0,width,height);
			} else if (plip instanceof ActivePath) {
				ActivePath A = (ActivePath) plip;
				if (board.union.contains(A.path)) {
					check[board.union.indexOf(A.path)]=true; // true -> ActivePath mis à jour
					A.updateActivePath(rayon,x0,y0,width,height);
				} else {
					this.remove(plip);
				}
			} else if (plip instanceof ActiveSolution) {
				ActiveSolution s = (ActiveSolution) plip;
				s.updateActiveSolution(rayon, x0, y0, width, height);
				s.paint(g);
			}
		}
		// Maintenant on ajoutte ou en enlève les ActivePath
		for (int i=0 ; i<check.length ; i++) {
			if (check[i]==false) {
				this.add(new ActivePath(rayon, x0, y0, board.union.get(i), width, height));
			}
		}

		super.paint(g);
	}
	
	public void mouseClicked(MouseEvent e) {
		// le click droit permet de blocker
		if ((e.getButton()==MouseEvent.BUTTON1)&&(!solved)) {
			Component[] plop = this.getComponents();
			for (int i=0 ; i<plop.length ; i++) {
				Component plip = plop[i];
				if (plip instanceof ActiveLink) {
					ActiveLink L = (ActiveLink) plip;
					if (L.contains(e.getPoint())) {
						L.click();
						nbClick++;
					}
				} else if (plip instanceof ActivePath) {
					ActivePath P = (ActivePath) plip;
					if (P.contains(e.getPoint())) {
						P.click();
						remove(P);
					}
				}
			}
			if (board.isSolved()) {
				solved=true;
				time=System.currentTimeMillis()-time; // temps effectivement passé en jeu
				this.victory();
			}
		// le click gauche permet de vérouiller
		} else if ((e.getButton()==MouseEvent.BUTTON3)&&(!solved)) {
			Component[] plop = this.getComponents();
			for (int i=0 ; i<plop.length ; i++) {
				Component plip = plop[i];
				if (plip instanceof ActiveHexagon) {
					ActiveHexagon A = (ActiveHexagon) plip;
					if (A.contains(e.getPoint())) A.lock();
				} else if (plip instanceof ActiveLink) {
					ActiveLink L = (ActiveLink) plip;
					if (L.contains(e.getPoint())) L.lock();
				}
			}
		}
		
		this.repaint();
	}
	
	/**
	 * Fonction appelée en cas de victoire
	 * Ouvre une fenètre pop up avec les performances
	 */
	private void victory() {
		ImageIcon img = new ImageIcon("img/victory.png");
		JOptionPane.showMessageDialog(null, new String[] {"Vous avez résolu le problème!", "","Temps de résolution  "+(int) (time/1000) + " sec", "Nombre de coups  "+nbClick, "", "Ecran d'acceuil ?"}, "Félicitations!", JOptionPane.INFORMATION_MESSAGE,img);
		this.removeMouseListener(this);
	}
	
	/**
	 * Affiche la solution du problème
	 * Il faut retirer les cotés qui empèchent de cacherait le dessin de la solution
	 */
	public void showSolution() {
		add(new ActiveSolution(0,0,0,board.solution,0,0));
		for(Component c : this.getComponents()) {
			if (c instanceof ActiveLink) {
				ActiveLink a = (ActiveLink) c;
				if (board.solution.contains(a.link)) {
					this.remove(c);
				}
			}
		}
		this.removeMouseListener(this);
		validate();
		repaint();
	}
	
	public String toString() {return board.toString();}
	
	public void newName(String s) {
		board.newName(s);
	}

	public void mousePressed(MouseEvent e) {}
	//Invoked when a mouse button has been pressed on a component.

	public void mouseReleased(MouseEvent e) {}
	//Invoked when a mouse button has been released on a component.

	public void mouseEntered(MouseEvent e) {}
	//Invoked when the mouse enters a component.

	public void mouseExited(MouseEvent e) {}
	//Invoked when the mouse exits a component.
	
	public boolean isSolved() {return solved;}
}
