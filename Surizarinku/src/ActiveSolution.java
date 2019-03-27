import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Version visuelle des solutions
 */
public class ActiveSolution extends Component{
	private static final long serialVersionUID = 1L;
	
	private LinkedList<ActiveLink> activeLinkList = new LinkedList<ActiveLink>();
	public final static Color COLOR = new Color(223, 255, 0); // new Color(199, 44, 72)
	
	public ActiveSolution(double rayon, double x0, double y0, ListLink solution, int width, int height) {
		Iterator<Link> ite = solution.iterator();
		while (ite.hasNext()) activeLinkList.add(new ActiveLink(rayon,x0,y0,ite.next(),width,height));
		updateActiveSolution(rayon,x0,y0,width,height);
	}
	
	/**
	 * Mise à jour de l'affichage en fonction des dimensions de la fenetre
	 */
	public void updateActiveSolution(double rayon, double x0, double y0, int width, int height) {
		Iterator<ActiveLink> iteL = activeLinkList.iterator();
		while(iteL.hasNext()) iteL.next().updateActiveLink(rayon, x0, y0, width, height);
	}
	
	public void paint(Graphics g) {
		g.setColor(COLOR);
		Iterator<ActiveLink> ite = activeLinkList.iterator();
		while (ite.hasNext()) g.fillPolygon(ite.next().getPolyDraw());
	}
}
