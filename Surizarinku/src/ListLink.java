import java.util.LinkedList;

/**
 * Liste de cotés
 */
public class ListLink extends LinkedList <Link> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @return Position de l'hexagone s il est contenu dans la liste, -1 sinon
	 */
	public int checkPos(int o, int p, Orientation ori) {
		Link l;
		for (int i=0 ; i<this.size() ; i++) {
			l = this.get(i);
			if ((l.getPosition()[0]==o)&&(l.getPosition()[1]==p)&&(l.orien==ori)) return i;
		}
		return -1;
	}
}
