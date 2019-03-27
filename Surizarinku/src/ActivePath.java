import java.awt.*;
import java.util.*;

/**
 * Version visuelle des chemins générés par l'utilisateur
 * Les chemis sont des suites de cotés
 */
public class ActivePath extends Component {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Polygon> polyListDraw = new LinkedList<Polygon>();
	private LinkedList<Polygon> polyListClick = new LinkedList<Polygon>();
	public final Path path;

	public final static Color COLOR = new Color(223, 115, 255);
	
	public ActivePath(double rayon, double x0, double y0, Path p, int width, int height) {
		this.path = p;
		updateActivePath(rayon,x0,y0,width,height);
	}
	
	/**
	 * Mise à jour du chemin en fonction des dimensions de la fenetre
	 */
	public void updateActivePath(double rayon, double x0, double y0, int width, int height) {
		// On supprime les polygones
		this.polyListDraw.clear(); this.polyListDraw.clear();
		
		// Il faut refaire les triangles, et pour cela récupérer des informations sur les cotes adjacants
		Iterator<Link> ite = path.iterator();
		if (path.size()>1) {
			Link p0 = ite.next(); Link p1; double px; double py; boolean b = path.isLoop();
			while (ite.hasNext()) {
				// exception pour tracer une belle boucle
				if (b) {
					p1 = path.get(path.size()-1);
					b = false;
				} else {
					p1 = p0;
					p0 = ite.next();
				}
				
				// On statue en fonction des cas
				int[][] h0 = p0.getTouchPosAll();
				int[][] h1 = p1.getTouchPosAll();
				int i; int j;
				
				// on recherche l'hexagone commu aux deux cotés
				if (this.VERIF(h0[0], h1[0])) {
					i = h0[0][0];
					j = h0[0][1];
				} else if (this.VERIF(h0[1], h1[1])) {
					i = h0[1][0];
					j = h0[1][1];
				} else if (this.VERIF(h0[0], h1[1])) {
					i = h0[0][0];
					j = h0[0][1];
				} else {
					i = h0[1][0];
					j = h0[1][1];
				}
				px = x0 + Math.sqrt(3)*rayon*i - Math.sqrt(3)/2*rayon*j;
				py = y0 + 1.5*rayon*j;
				
				// on statue sur la position du triangle par rapport à l'hexagone retenu
				if (p0.getPosition()[0]!=p1.getPosition()[0]) {
					if (j<p0.getPosition()[1]) {
						i = 4;
					} else {
						i = 0;
					}
				} else if (p1.getPosition()[1]!=p0.getPosition()[1]) {
					if (i<p0.getPosition()[0]) {
						i = 5;
					} else {
						i = 3;
					}
				} else if ((p0.orien==Orientation.W)||(p1.orien==Orientation.W)) i = 2;
				else i = 1;
				px += rayon*Math.cos(Math.PI/6+i*Math.PI/3);
				py -= rayon*Math.sin(Math.PI/6+i*Math.PI/3);
				double deca = (i%2==0)?0:Math.PI/3;
				
				this.polyListDraw.add(new Polygon(ActivePath.COORD_X(rayon, px, py,deca),ActivePath.COORD_Y(rayon, px, py,deca),3));
				this.polyListClick.add(new Polygon(ActivePath.COORD_X_CLICK(rayon, px, py,deca),ActivePath.COORD_Y_CLICK(rayon, px, py,deca),3));
			}
		}
		setBounds(0,0,width,height);
	}
	
	public void paint(Graphics g) {
		g.setColor(COLOR);
		Iterator<Polygon> ite = this.polyListDraw.iterator();
		while (ite.hasNext()) {
			g.fillPolygon(ite.next());
		}
	}
	
	public boolean contains(double x, double y) {
		return this.contains(x, y);
	}
	
	public boolean contains(Point p) {
		Iterator<Polygon> ite = this.polyListClick.iterator();
		while (ite.hasNext()) {
			if (ite.next().contains(p)) return true;
		}
		return false;
	}
	
	/**
	 * Les triangles permettent seuleument de supprimer l'intégralité du chemin
	 * C'est à classe ActiveBoard qui s'occupe de l'effacement visuel
	 */
	public void click() {
		path.empty();
	}
	
	public static int[] COORD_X(double rayon, double px, double py, double dec) {
		int[] x = new int[3];
		for (int i=0 ; i<3 ; i++) {
			x[i] = (int) (px+rayon*(1-ActiveHexagon.DILLATATION)*ActiveLink.DILLATATION*Math.cos(i*2*Math.PI/3+Math.PI/6+dec));
		}
		return x;
	}
	
	public static int[] COORD_Y(double rayon, double px, double py, double dec) {
		int[] y = new int[3];
		for (int j=0 ; j<3 ; j++) {
			y[j] = (int) (py+(1-ActiveHexagon.DILLATATION)*ActiveLink.DILLATATION*rayon*Math.sin(j*2*Math.PI/3+Math.PI/6+dec));
		}
		return y;
	}
	
	public static int[] COORD_X_CLICK(double rayon, double px, double py, double dec) {
		int[] x = new int[3];
		for (int i=0 ; i<3 ; i++) {
			x[i] = (int) (px+rayon*(1-ActiveHexagon.DILLATATION)*Math.cos(i*2*Math.PI/3+Math.PI/6+dec));
		}
		return x;
	}
	
	public static int[] COORD_Y_CLICK(double rayon, double px, double py, double dec) {
		int[] y = new int[3];
		for (int j=0 ; j<3 ; j++) {
			y[j] = (int) (py+(1-ActiveHexagon.DILLATATION)*rayon*Math.sin(j*2*Math.PI/3+Math.PI/6+dec));
		}
		return y;
	}
	
	/**
	 * Fonction de comparaison de deux tableaux
	 * L'égalité correspond à l'égalité de chacun des éléments
	 */
	public boolean VERIF(int[] t1, int[] t2) {
		if (t1.length==t2.length) {
			for (int i=0 ; i<t1.length ; i++) {
				if (t1[i]!=t2[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
