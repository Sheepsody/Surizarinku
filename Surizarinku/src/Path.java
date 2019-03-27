import java.util.Iterator;
import java.util.LinkedList;

/**
 * Chemin de cotes du jeu
 * L interet est de pouvoir supprimer facilement un chemin invalide
 */
public class Path extends ListLink {
	private static final long serialVersionUID = 1L;
	
	private boolean loop = false;
	
	public Path(Link l) {
		super();
		this.add(l);
	}
	
	public Path (LinkedList<Link> l) {
		super();
		Iterator<Link> ite = l.iterator();
		while (ite.hasNext()) this.add(ite.next());
	}
	
	/**
	 * Extrait une sous liste de i à j (non compris)
	 */
	public LinkedList<Link> subLinkedList(int i, int j) {
		LinkedList<Link> retour = new LinkedList<Link>();
		for (int k=i ; k<j ; k++) {
			retour.addLast(this.get(k));
		}
		return retour;
	}
	
	/**
	 * Modifie le chemin de facon a ce que l indice i devienne le premier indice
	 */
	public void rematch(int i) {
		LinkedList<Link> p1 = this.subLinkedList(0, i+1);
		LinkedList<Link> p2 = this.subLinkedList(i+1, this.size());
		this.clear(); // on vide la liste
		Iterator<Link> ite = p2.iterator();
		while (ite.hasNext()) {
			this.addLast(ite.next());
		}
		ite = p1.iterator();
		while (ite.hasNext()) {
			this.addLast(ite.next());
		}
	}
	
	/**
	 * Coupe la liste en deux morceaux, tel que i soit le dernier indice de la première, et que la seconde commence à i+1
	 */
	public Path cut(int i) {
		Path p = new Path(this.subLinkedList(i+1, this.size()));
		this.resize(i);
		return p;
	}
	
	/**
	 * Coupe la chaine pour qu'elle ait pour dernier élement l'indice i
	 */
	public void resize(int i) {
		while (this.size()>i+1) {
			this.removeLast();
		}
	}
	
	public void reverse() {
		LinkedList<Link> l = new LinkedList<Link>();
		for (int i=this.size()-1 ; i>=0 ; i--) {
			l.add(this.get(i));
		}
		this.clear();
		for (int i=0 ; i<l.size() ; i++) {
			this.add(l.get(i));
		}
	}
	
	public void empty() {
		for (int i = this.size()-1 ; i>=0 ; i--) this.get(i).block();
	}
	
	public boolean isEmpty() { return (this.size()==0)?true:false; }
	public boolean isLoop() { return loop; }
	public void oppLoop() { loop = !loop ;	}
}
