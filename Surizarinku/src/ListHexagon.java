import java.util.LinkedList;

/**
 * Liste d hexagones
 */
public class ListHexagon extends LinkedList <Hexagon> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @return Position de l'hexagone s il est contenu dans la liste, -1 sinon
	 */
	public int checkPos(int o, int p) {
		Hexagon H;
		for (int i=0 ; i<this.size() ; i++) {
			H = this.get(i);
			if ((H.getPosition()[0]==o)&&(H.getPosition()[1]==p)) return i;
		}
		return -1;
	}
}