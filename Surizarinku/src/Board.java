// ï¿½ modifier : ne pas partir du principe que tous de que je lis est "bien fait!"
// changer Path en LinkedList pour plus de simplcicitï¿½

import java.util.*;

import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.io.*;

/**
 * Pavage d'un jeu
 * Il s'agit de la classe de base du jeu, où sont sauvegardés les informations essentielles
 * @author vvial
 *
 */
public class Board {
	
	//private Path solution;
	public LinkedList<Path> union = new LinkedList<Path>();
	public ListHexagon grid = new ListHexagon();
	public ListLink web = new ListLink();
	public ListLink solution = new ListLink();
	private String name;
	private int[] size = new int[2];
	private int difficulty;
	
	public Board(String n, int d0, int d1, int d) {
		name = n;
		size[0] = d0; size[1] = d1;
		difficulty = d;
	}
	
	/**
	 * Generation d un pavage a partir d un fichier source
	 * @param Chemin d'acces au fichier
	 * @return Le chemin construit
	 * @throws Exception
	 */
	public static Board BUILD(String chemin) throws Exception {
		Scanner sc;
		try {
			FileReader in = new FileReader(chemin);
			sc = new Scanner(in);
			
			// recupetation du titre
			String name = sc.nextLine();
			
			// recuperation de la difficulte
			String s = sc.nextLine();
			int dif = Integer.parseInt(s);
			
			// recuperation des dimensions
			s = sc.nextLine();
			String[] tab = s.split("\t");
			int d0 = Integer.parseInt(tab[0]);
			int d1 = Integer.parseInt(tab[1]);
			
			// recuperation des valeur des hexagones
			int[][] grid = new int[d0][d1];
			for (int j=0; j<d1; j++) {
				s = sc.nextLine();
				tab = s.split("\t");
				for (int i=0 ; i<d0 ; i++) {
					if (!(tab[i].equals("X"))) grid[i][j] = Integer.parseInt(tab[i]);
					else grid[i][j] = -2 ;
				}
			}
			Board b = new Board(name, d0, d1, dif);
			
			// construction du maillage de base du projet
			b.buildListHexagon(grid);
			b.buildListLink(b.grid);
			
			// recuperation de la solution
			try {
				ListLink sol = new ListLink();
				while (sc.hasNextLine()) {
					s = sc.nextLine();
					tab = s.split("\t");
					int x = Integer.parseInt(tab[1]);
					int y = Integer.parseInt(tab[2]);
					char o = tab[0].charAt(0);
					Orientation orien;
					if (o=='W') orien=Orientation.W;
					else if (o=='N') orien = Orientation.N;
					else orien=Orientation.NE;
					int i = b.web.checkPos(x, y, orien);
					if (i==-1) throw new Exception("Impossible de générer la solution\n"+s); // J'ai trouvé qu'il n'était pas nécéssaire de creer une classe spécifique
					else {
						sol.add(b.web.get(i));
					}
				}
				b.solution=sol;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, s,"Erreur !", JOptionPane.WARNING_MESSAGE);
			}
			
			// affichage de verification
			b.afficherTest();
			sc.close();
			return b;
		}
		/*
		 * Les catchs qui viennent ensuite me permettent juste de réécrire correctement les messages d'erreur
		 */
		catch(FileNotFoundException e) {
			throw new FileNotFoundException("Le fichier spécifié n'éxiste pas!");
		} catch(NoSuchElementException e) {
			throw new NoSuchElementException("Le fichier n'est pas de dimension suffisante");
		} catch(NumberFormatException e) {
			throw new NumberFormatException("Il manque un entier pour une generation correcte");
		}
	}
	
	/**
	 * Algorithme de generation d un jeu
	 * 
	 * Principe de base de l'algorithme
	 * 1. Generation recursive d un chemin au hasard
	 * 2. Remplissage d'un nombre fini d hexagones
	 * 
	 * @param Dimensions
	 * @param Intervalle de difficulte
	 * @param nom
	 * @param forme
	 * @return Le jeu correspondant
	 */
	public static Board GENERATE(Dimension d, int[] dif, String nom, String forme) {
		
		// Creation du pavage selon les dimensions et la forme souhaitée
		Board board = new Board(nom, d.width, d.height, 0);
		for (int i=0 ; i<d.width ; i++) {
			for (int j=0 ; j<d.height ; j++) {
				if (forme.equals("Rond")) {
					if (board.posInRound(i, j)) board.grid.add(new Hexagon(-1,i+((j%2)==0?j/2:j/2+1),j,board));
				} else if (forme.equals("Triangulaire")) {
					if (board.posInTriangle(i, j)) board.grid.add(new Hexagon(-1,i+((j%2)==0?j/2:j/2+1),j,board));
				} else board.grid.add(new Hexagon(-1,i+((j%2)==0?j/2:j/2+1),j,board));
			}
		}
		board.buildListLink(board.grid);
		
		/*
		 * Generation aleatoire du chemin
		 * 
		 * La difficulte sert ici a controler le nombre d iteration effectuees dans la partie recursive, puiqu il faut necessairement une variable globale
		 * Dans le cas où l'on a atteint la longueur maximale autorisee recursivement, une erreur est envoyee et conduit a un nouvel essai.
		 */
		LinkedList<Link> loop = new LinkedList<Link>(); Iterator<Link> iteL;
		board.difficulty=0;
		while((board.difficulty>dif[1])||(board.difficulty<dif[0])) { // on veut un pavage de difficulte correcte
			try {
				// Intialisation des variables qui auraient pu etre utilise lors de l iteration precedente
				iteL = loop.iterator(); while (iteL.hasNext()) iteL.next().lock();
				
				// On prend un des coté (i.e. Link) au hazard et on s en sert comme premier element
				Link first = board.web.get((int) (board.web.size()*Math.random()));
				first.lock(); // l attribut locked permet ici de controler si un cote est utilise dans le chemin
				loop.clear(); loop.add(first);
				
				board.difficulty = board.grid.size()*2; // nombre d'iteration maximum dans la partie recursive
				
				board.loopFirst(loop); // partie recursive
				
				/*
				 * Evaluation de la difficulte
				 * Le facteur 2,5 a ete obtenu en effectuant des tests successifs
				 */
				board.difficulty = (int) (10.0*loop.size()/board.web.size()*2.5); // 
				System.out.println(board.difficulty);
			} catch (LoopException e) {
				System.err.println(e.getMessage());
			}
		}
		if (board.difficulty>10) board.difficulty=10;
		
		// Remplissage de certains hexagones
		Hexagon h; int nb;
		for (int i=0 ; i<(int)(d.getWidth()*d.getHeight()*0.7) ; i++) {
			h = board.grid.get((int) (board.grid.size()*Math.random()));
			if (h.maxLink==-1) {
				Iterator<Link> iteLink = h.getLinkTouch().iterator();
				nb = 0;
				while (iteLink.hasNext()) {
					if (iteLink.next().isLocked()) nb++;
				}
				board.grid.remove(h);
				board.grid.add(new Hexagon(nb, h.getPosition()[0], h.getPosition()[1], board));
			}
			
		}
		
		// On sauvegarde en mémoire la solution
		Iterator<Link> ite = loop.iterator();
		while(ite.hasNext()) board.solution.add(ite.next());
		
		// On remet à zero les cotés pour ne pas qu'ils soient vérouillé lors de l'affichage graphique!
		ite = loop.iterator();
		while (ite.hasNext()) ite.next().lock();
		return board;
	}
	
	/**
	 * Algorithme recursif de la generation d une solution
	 * 
	 * Terminaison :
	 * LoopException est l'exception lancee quand on atteint le nombre maximum d iterations
	 * 
	 * Booleen renvoye a chaque iteration
	 * true -> le chemin s est boucle
	 * false -> le cote ne permet pas de continuer le chemin
	 */
	private boolean loopFirst(LinkedList<Link> loop) throws LoopException {
		// Gestion de l erreur
		if (--this.difficulty<=0) throw new LoopException("Chemin bloqué");
		
		//Indice correspond au dernier coté vérouillé
		Link[][] voisins = loop.getLast().getTouchLink();
		
		// Le chemin est il valide ? (forme il une boucle ?)
		if (loop.size()>10) for (int i=0 ; i<2 ; i++) for (int j=0 ; j<2 ; j++) if (voisins[i][j]==loop.getFirst()&&(voisins[i][(j+1)%2]==null?true:!voisins[i][(j+1)%2].isLocked())) return true;
		
		/*
		 * On essaye de continuer le chemin a partir d un cote adjacent pris au hazard
		 * Si la tentative ne marche pas, on essaye l autre cote adjacent quand cela est possible
		 */
		for (int i=0 ; i<2 ; i++) {
			if (voisins[i][0]==null) {
				if (!(voisins[i][1]==null)) if (!voisins[i][1].isLocked()){
					voisins[i][1].lock();
					loop.add(voisins[i][1]);
					if (this.loopFirst(loop)) return true;
					voisins[i][1].lock();
					loop.removeLast();
				}
			} else if (voisins[i][1]==null) {
				if (!(voisins[i][0]==null)) if (!voisins[i][0].isLocked()){
					loop.add(voisins[i][0]);
					voisins[i][0].lock();
					if (this.loopFirst(loop)) return true;
					voisins[i][0].lock();
					loop.removeLast();
				}
			}else if (!voisins[i][0].isLocked()&&!voisins[i][1].isLocked()) {
				int e1 = (int) (2*Math.random()); int e2 = (e1+1)%2; // Un cote est selectionne au hasard
				
				voisins[i][e1].lock();
				loop.add(voisins[i][e1]);
				if (this.loopFirst(loop)) return true;
				voisins[i][e1].lock();
				loop.removeLast();
				
				voisins[i][e2].lock();
				loop.add(voisins[i][e2]);
				if (this.loopFirst(loop)) return true;
				voisins[i][e2].lock();
				loop.removeLast();
			}
		}
		return false;
	}
	
	private void buildListHexagon(int[][] list) {
		for (int i=0 ; i<list.length ; i++) {
			for (int j=0 ; j<list[0].length ; j++) {
				if ((list[i][j]>=-1)&&(list[i][j]<=5)) grid.add(new Hexagon(list[i][j],i+((j%2==0)?j/2:(j+1)/2),j,this));
			}
		}
	}
	
	private void buildListLink(ListHexagon H) {
		Iterator<Hexagon> ite = H.listIterator();
		Hexagon hex; int[] pos; int x,y;
		while (ite.hasNext()) {
			hex = ite.next();
			pos = hex.getPosition();
			x = pos[0] ; y = pos[1] ;
			if (web.checkPos(x, y, Orientation.N)==-1) web.add(new Link(x,y,Orientation.N,this));
			if (web.checkPos(x, y, Orientation.NE)==-1) web.add(new Link(x,y,Orientation.NE,this));
			if (web.checkPos(x, y, Orientation.W)==-1) web.add(new Link(x,y,Orientation.W,this));
			if (web.checkPos(x, y+1, Orientation.NE)==-1) web.add(new Link(x,y+1,Orientation.NE,this));
			if (web.checkPos(x+1, y, Orientation.W)==-1) web.add(new Link(x+1,y,Orientation.W,this));
			if (web.checkPos(x+1, y+1, Orientation.N)==-1) web.add(new Link(x+1,y+1,Orientation.N,this));
		}
	}
	
	/**
	 * Verifie si l utilisateur a trouve la solution
	 */
	public boolean isSolved() {
		// L'utilsateur n a construit qu un seul chemin ?
		if (union.size()==1) {
			Path p = union.get(0);
			// le chemin forme t il une boucle ?
			if (p.isLoop()) {
				Iterator<Hexagon> ite = grid.iterator();
				Hexagon h;
				// Tous les hexagones ont ils le bon nombre de cotes selectionnes ?
				while (ite.hasNext()) {
					h = ite.next();
					if ((h.maxLink!=h.getLinkSum())&&(h.maxLink>=0)) {
						return false;
					}
					if (h.maxLink>0) {
						LinkedList<Link> list = h.getLinkTouch();
						Iterator<Link> iteLink = list.iterator();
						Link l;
						while (iteLink.hasNext()) {
							l = iteLink.next();
							if (l.isBlocked()) {
								if (p.indexOf(l)==-1) return false;
							}
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Fonction utilisee au debut du projet par soucis visuel
	 */
	public void afficherTest() {
		System.out.println(name);
		System.out.println("Cette grille a une difficultï¿½ de " + difficulty);
		int pos;
		for (int j = 0 ; j<size[0] ; j++) {
			if (j%2==1) System.out.print("\t");
			for (int i = 0 ; i<size[1] ; i++) {
				pos = grid.checkPos(i + ((j%2==0)?j/2:(j+1)/2), j);
				if (pos!=-1) {
					System.out.print(grid.get(pos).toString());
					System.out.print("\t" + (grid.get(pos).isLocked()?1:0) + "\t");
				} else {
					System.out.print("\t\t");
				}
			}
			System.out.println("\t\t " + j + "ï¿½me ligne");
			System.out.println();
		}
	}
	
	public void blocker(int i, int j, Orientation o) {
		Link l = web.get(web.checkPos(i, j, o));
		l.block();
	}
	
	public int[] getSize() {
		return size.clone();
	}
	
	public Path searchPath(Link l) {
		Iterator<Path> ite = union.iterator();
		Path p;
		while(ite.hasNext()) {
			p = ite.next();
			if (p.indexOf(l) != -1) return p;
		}
		return null;
	}
	
	public void printUnion() {
		System.out.println("Liste des chemins");
		Iterator<Path> ite = union.iterator();
		Path pi;
		
		while (ite.hasNext()) {
			pi = ite.next();
			System.out.println(pi.size() + " de longueur");
		}
	}
	
	/**
	 * Affichage textuel du pavage
	 */
	public String toString() {
		if (this.isSolved()) this.solution=this.union.get(0);
		String s = name+"\n";
		s+=this.difficulty+"\n";
		s+=this.size[0]+"\t"+this.size[1]+"\n";
		for (int j=0 ; j<this.size[1] ; j++) {
			for (int iprime=0 ; iprime<this.size[0] ; iprime++) {
				int i = iprime+((j%2==0)?j/2:(j+1)/2);
				int o = this.grid.checkPos(i, j);
				if (o==-1) o = -2;
				else o = this.grid.get(o).maxLink;
				if (iprime!=0) s+="\t";
				s+=o;
			}
 			s+="\n";
		}
		Iterator<Link> ite = this.solution.iterator();
		while (ite.hasNext()) {
			Link l = ite.next();
			if (l.orien==Orientation.W) s+='W';
			else if (l.orien==Orientation.N) s+='N';
			else s+='E';
			s+="\t"+l.getPosition()[0];	s+="\t"+l.getPosition()[1];
 			if (ite.hasNext()) s+="\n";
		}
		return s;
	}
	
	public void newName(String s) {
		this.name = s;
	}
	
	private boolean posInRound(int x, int y) {
		Dimension d = new Dimension(size[0], size[1]);
		double i = Math.pow((x-d.width/2.0)/d.width*2,2);
		i+=Math.pow((y-d.height/2.0)/d.height*2,2);
		i--;
		if (i>0) return false;
		return true;
	}
	
	private boolean posInTriangle(int x, int y) {
		Dimension d = new Dimension(size[0], size[1]);
		if (Math.floor(x-d.width/2.0-d.width/2.0*(d.height-y-1)/d.height)>0) return false; // floor correspond à la troncature
		if (Math.floor(x-d.width/2.0+d.width/2.0*(d.height-y-1)/d.height)<0) return false;
		return true;
	}
}
