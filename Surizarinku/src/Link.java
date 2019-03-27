/**
 * Cote des hexagones
 * Il sont identifies par leur position et leur orientation
 */
public class Link {
	
	private final int[] position = new int[2];
	public final Orientation orien;
	private final Board board;
	
	private boolean blocked = false;
	private boolean locked = false;
	private boolean inPath = false;
	
	
	public Link (int p0, int p1, Orientation o, Board b) { // constructeur de base, rien n'a ï¿½tï¿½ chargï¿½
		position[0] = p0;
		position[1] = p1;
		orien = o;
		board = b;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	/**
	 * Fonction reagissant a la demande de bloquage du cote
	 */
	public void block() {
		try {
			if (isLocked()) throw new GameException("Impossible! Le coté est vérouillé!");
			
			else {
				// on recupere les hexagones de part et d'autre du cote
				int i = position[0]; int j = position[1];
				int test1; int test2;
				if (orien==Orientation.W) {
					test1 = board.grid.checkPos(i, j);
					test2 = board.grid.checkPos(i-1, j);
				} else if (orien==Orientation.N) {
					test1 = board.grid.checkPos(i, j);
					test2 = board.grid.checkPos(i-1, j-1);
				} else {
					test1 = board.grid.checkPos(i, j);
					test2 = board.grid.checkPos(i, j-1);
				}
				
				// Debloquage du cote
				if (blocked) {
					// Si il ne fait pas partie d un chemin
					blocked = false;
					if (test1!=-1) {
						board.grid.get(test1).del();
					}
					if (test2!=-1) {
						board.grid.get(test2).del();
					}
					
					// Si il fait partie d un chemin
					if (inPath) {
						Path p = board.searchPath(this);
						int pos = p.indexOf(this);
						if (p.isLoop()) {
							p.oppLoop();
							p.rematch(pos);
							p.removeLast();
							this.reverseInPath();
							return ; // Permet de sortir de la fonction
						} else {
							// Si le chemin n'est pas une boucle, on le coupe en deux
							Path newP = p.cut(pos);
							p.removeLast();
							this.reverseInPath();
							
							if (newP.size()>1) board.union.add(newP);
							else if (newP.size()==1) newP.getFirst().reverseInPath();
							
							if (p.size()<2) {
								board.union.remove(p);
								if (p.size()==1) p.getFirst().reverseInPath();
							}
							return ;
						}
					} else {
						return ;					
					}
				} else {
					// Dans ce cas il faut bloquer le cote
					
					if (test1!=-1) if (!(board.grid.get(test1).addLinkPossible())) throw new GameException("Un des hexagones ne permet pas le mouvement!");
					if (test2!=-1) if (!(board.grid.get(test2).addLinkPossible())) throw new GameException("Un des hexagones ne permet pas le mouvement!");
					
					// Recherche des cotes adjacents
					int[] testbis = new int[4];
					if (orien==Orientation.W) {
						testbis[0] = board.web.checkPos(i-1, j, Orientation.NE);
						testbis[1] = board.web.checkPos(i, j, Orientation.N);
						testbis[2] = board.web.checkPos(i, j+1, Orientation.NE);
						testbis[3] = board.web.checkPos(i, j+1, Orientation.N);
					} else if (orien==Orientation.N) {
						testbis[0] = board.web.checkPos(i, j-1, Orientation.W);
						testbis[1] = board.web.checkPos(i, j, Orientation.NE);
						testbis[2] = board.web.checkPos(i-1, j, Orientation.NE);
						testbis[3] = board.web.checkPos(i, j, Orientation.W);
					} else if (orien==Orientation.NE) {
						testbis[0] = board.web.checkPos(i, j, Orientation.N);
						testbis[1] = board.web.checkPos(i, j-1, Orientation.W);
						testbis[2] = board.web.checkPos(i+1, j, Orientation.N);
						testbis[3] = board.web.checkPos(i+1, j, Orientation.W);
					}
					
					// Condition à repecter : maximum deux cotes en un point
					if ((testbis[0]!=-1)&&(testbis[1]!=-1)) {
						if (board.web.get(testbis[0]).isBlocked()) {
							if (board.web.get(testbis[1]).isBlocked()) {
								throw new GameException("Le noeud est déjà bloqué!");
							}
						}
					}
					if ((testbis[2]!=-1)&&(testbis[3]!=-1)) {
						if (board.web.get(testbis[2]).isBlocked()) {
							if (board.web.get(testbis[3]).isBlocked()) {
								throw new GameException("Le noeud est déjà bloqué!");
							}
						}
					}
					
					
					// Dans ce cas il est possible de bloquer le cote
					blocked = true;
					if (test1!=-1) {
						board.grid.get(test1).add();
					}
					if (test2!=-1) {
						board.grid.get(test2).add();
					}
					
					// on regarde si un des cotÃ©s autour est bloquÃ© ou non, si oui, deux cas
					boolean b0 = (testbis[0]!=-1)?board.web.get(testbis[0]).isBlocked():false;
					boolean b1 = (testbis[1]!=-1)?board.web.get(testbis[1]).isBlocked():false;
					
					// Ajout eventuel du cote dans un chemin
					if (b1||b0) {
						Link kote;
						this.reverseInPath();
						if (b0) {
							kote=board.web.get(testbis[0]);
						} else {
							kote=board.web.get(testbis[1]);
						}
						Path p;
						if (kote.isInPath()) {
							p = board.searchPath(kote);
							int posKote = p.indexOf(kote);
							if (posKote==0) {
								p.addFirst(this);
							} else {
								p.addLast(this);
							}
						} else {
							kote.reverseInPath();
							p = new Path(this);
							p.addLast(kote);
							board.union.add(p);
						}
					}
					
					boolean b2 = (testbis[2]!=-1)?board.web.get(testbis[2]).isBlocked():false;
					boolean b3 = (testbis[3]!=-1)?board.web.get(testbis[3]).isBlocked():false;
					
					if (b3||b2) {
						Link kote;
						if (b2) {
							kote=board.web.get(testbis[2]);
						} else {
							kote=board.web.get(testbis[3]);
						}
						if (inPath) {
							if (kote.isInPath()) {
								Path pThis = board.searchPath(this);
								Path pKote = board.searchPath(kote);
								if (pKote==pThis) {
									pThis.oppLoop();
								} else {
									int posThis = pThis.indexOf(this);
									int posKote = pKote.indexOf(kote);
									if (posThis==0) pThis.reverse();
									if (posKote!=0) pKote.reverse();
									pThis.addAll(pKote);
									board.union.remove(pKote);
								}
							} else {
								kote.reverseInPath();
								Path p = board.searchPath(this);
								int posThis = p.indexOf(this);
								if (posThis==p.size()-1) p.addLast(kote);
								else p.addFirst(kote);
							}
						} else {
							this.reverseInPath();
							Path p;
							if (kote.isInPath()) {
								p = board.searchPath(kote);
								int posKote = p.indexOf(kote);
								if (posKote==0) {
									p.addFirst(this);
								} else {
									p.addLast(this);
								}
							} else {
								kote.reverseInPath();
								p = new Path(this);
								p.addLast(kote);
								board.union.add(p);
							}
						}
					}
				}
			}
		} catch (GameException e) {
			System.out.println(e.getMessage());
		}
	}
	
	 
	
	public void lock() {
		try {
			if (locked) locked=false;
			else {
				if (blocked) throw new GameException("Impossible! Le coté est déjà bloqué!");
				else locked = true;
			}
		} catch(GameException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public int[] getPosition() {
		int[] p;
		p = position.clone();
		return p;
	}
	
	public void reverseInPath() {
		inPath = !inPath;
	}
	
	public boolean isInPath() {
		return inPath;
	}
	
	/**
	 * Renvoie les hexagones adjacents au cote
	 */
	public Hexagon[] getTouch() {
		Hexagon h1 = null; Hexagon h2 = null;
		int x = this.getPosition()[0]; int y = this.getPosition()[1];
		int i; int j;
		if (this.orien==Orientation.W) {
			i = board.grid.checkPos(x, y);
			j = board.grid.checkPos(x-1, y);
		} else if (this.orien==Orientation.NE) {
			i = board.grid.checkPos(x, y);
			j = board.grid.checkPos(x, y-1);
		} else {
			i = board.grid.checkPos(x, y);
			j = board.grid.checkPos(x-1, y-1);
		}
		if (i!=-1) h1=board.grid.get(i);
		if (j!=-1) h2=board.grid.get(j);
		Hexagon[] retour;
		if ((i!=-1)&&(j!=-1)) {
			retour = new Hexagon[2];
			retour[0]=h1 ; retour[1]=h2;
		} else {
			retour = new Hexagon[1];
			if (i!=-1) retour[0]=h1;
			else retour[0]=h2;
		}
		return retour;
	}
	
	/**
	 * Donne les positions des hexagones adjacents, y compris ceux qui n existent pas
	 */
	public int[][] getTouchPosAll() {
		int[][] retour = new int[2][2];
		int x = this.getPosition()[0]; int y = this.getPosition()[1];
		if (this.orien==Orientation.W) {
			retour[0][0]=x; retour[0][1]=y;
			retour[1][0]=x-1; retour[1][1]=y;
		} else if (this.orien==Orientation.NE) {
			retour[0][0]=x; retour[0][1]=y;
			retour[1][0]=x; retour[1][1]=y-1;
		} else {
			retour[0][0]=x; retour[0][1]=y;
			retour[1][0]=x-1; retour[1][1]=y-1;
		}
		return retour;
	}
	
	public Link[][] getTouchLink() {
		// on fait en sorte qu'il soit classé deux par deux par adjaçance
		Link[][] retour = new Link[2][2];
		int i = this.getPosition()[0];
		int j = this.getPosition()[1];
		if (orien==Orientation.W) {
			retour[0][0] = (board.web.checkPos(i-1, j, Orientation.NE)==-1)?null:board.web.get(board.web.checkPos(i-1, j, Orientation.NE));
			retour[0][1] = (board.web.checkPos(i, j, Orientation.N)==-1)?null:board.web.get(board.web.checkPos(i, j, Orientation.N));
			retour[1][0] = (board.web.checkPos(i, j+1, Orientation.NE)==-1)?null:board.web.get(board.web.checkPos(i, j+1, Orientation.NE));
			retour[1][1] = (board.web.checkPos(i, j+1, Orientation.N)==-1)?null:board.web.get(board.web.checkPos(i, j+1, Orientation.N));
		} else if (orien==Orientation.N) {
			retour[0][0] = (board.web.checkPos(i, j-1, Orientation.W)==-1)?null:board.web.get(board.web.checkPos(i, j-1, Orientation.W));
			retour[0][1] = (board.web.checkPos(i, j, Orientation.NE)==-1)?null:board.web.get(board.web.checkPos(i, j, Orientation.NE));
			retour[1][0] = (board.web.checkPos(i-1, j, Orientation.NE)==-1)?null:board.web.get(board.web.checkPos(i-1, j, Orientation.NE));
			retour[1][1] = (board.web.checkPos(i, j, Orientation.W)==-1)?null:board.web.get(board.web.checkPos(i, j, Orientation.W));
		} else if (orien==Orientation.NE) {
			retour[0][0] = (board.web.checkPos(i, j, Orientation.N)==-1)?null:board.web.get(board.web.checkPos(i, j, Orientation.N));
			retour[0][1] = (board.web.checkPos(i, j-1, Orientation.W)==-1)?null:board.web.get(board.web.checkPos(i, j-1, Orientation.W));
			retour[1][0] = (board.web.checkPos(i+1, j, Orientation.N)==-1)?null:board.web.get(board.web.checkPos(i+1, j, Orientation.N));
			retour[1][1] = (board.web.checkPos(i+1, j, Orientation.W)==-1)?null:board.web.get(board.web.checkPos(i+1, j, Orientation.W));
		}
		return retour;
	}
}
