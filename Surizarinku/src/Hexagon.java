import java.util.Iterator;
import java.util.LinkedList;

/**
 * Pave hexagonal
 * Il est identifie par sa position
 */
public class Hexagon implements Comparable <Hexagon> {
	
	public final int maxLink; // -1 si aucune contrainte
	private int[] position = new int[2];
	private int linkSum;
	private boolean locked = false;
	private final Board board;
	
	public final static int LINK_LIMIT = 5;
	
	public Hexagon(int mL, int p0, int p1, Board b) { // constructeur de base, aucune donnï¿½e n'a ï¿½tï¿½ chargï¿½e
		maxLink = mL;
		linkSum = 0;
		position[0] = p0;
		position [1] = p1;
		board = b;
	}
	
	public int[] getPosition() {
		int[] p;
		p = position.clone(); // aucune modification de p n'influe sur position
		return p;
	}
	
	public int getLinkSum() {
		return linkSum;
	}
	
	public void add() {
		linkSum+=1;
	}
	
	public void del() {
		linkSum-=1;
	}
	
	public boolean remove() {
		if (linkSum>0) {
			linkSum+=1;
			return true;
		}
		return false;
	}
	
	public int compareTo(Hexagon c) {
		if (this.position[0] != c.position[0]) {
			return this.position[0]-c.position[0];
		} else {
			return this.position[1] - this.position[1];
		}
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void lock() {
		if (locked) locked=false;
		else {
			try {
				Iterator<Link> ite = this.getLinkTouch().iterator();
				while (ite.hasNext()) if (ite.next().isBlocked()) throw new GameException("Verrouillage impossible! Un des cotés est bloqué!");
				locked=true;
			} catch (GameException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public boolean addLinkPossible() {
		if ((linkSum==maxLink)||(locked)||(linkSum>=LINK_LIMIT)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return linkSum + "/" + maxLink;
	}
	
	/**
	 * @return Cotes autour de l'hexagone
	 */
	public LinkedList<Link> getLinkTouch() {
		LinkedList<Link> retour = new LinkedList<Link>();
		retour.add(board.web.get(board.web.checkPos(position[0], position[1], Orientation.N)));
		retour.add(board.web.get(board.web.checkPos(position[0], position[1], Orientation.NE)));
		retour.add(board.web.get(board.web.checkPos(position[0], position[1], Orientation.W)));
		retour.add(board.web.get(board.web.checkPos(position[0]+1, position[1], Orientation.W)));
		retour.add(board.web.get(board.web.checkPos(position[0]+1, position[1]+1, Orientation.N)));
		retour.add(board.web.get(board.web.checkPos(position[0], position[1]+1, Orientation.NE)));
		return retour;
	}
	
	public boolean equals(Hexagon h) {
		if ((h.getPosition()[0]==this.getPosition()[0])&&(h.getPosition()[0]==this.getPosition()[0])) return true;
		return false;
	}
}
