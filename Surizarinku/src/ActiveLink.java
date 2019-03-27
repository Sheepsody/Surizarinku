import java.awt.*;

/**
 * Version visuelle des cotés des Héxagones
 */
public class ActiveLink extends Component {
	private static final long serialVersionUID = 1L;
	
	private Polygon polyDraw; // On distingue le polygone de trace et celui de selection
	private Polygon polyClick;
	private Color color = COLOR;
	public final Link link;
	
	public final static Color COLOR = ActiveBoard.COLOR_BACKGROUND;
	public final static Color COLOR_BLOCKED = new Color(203,253,204);
	public final static Color COLOR_LOCKED = new Color(1,3,236);
	public final static Color COLOR_PATH = new Color(223, 115, 255);
	public final static double DILLATATION = 0.5;
	
	public ActiveLink(double rayon, double x0, double y0, Link link, int width, int height) {
		this.link = link;
		updateActiveLink(rayon,x0,y0,width,height);
	}
	
	/**
	 * Mise à jour des polygones de l'instance
	 */
	public void updateActiveLink(double rayon, double x0, double y0, int width, int height) {
		double px = x0 + Math.sqrt(3)*rayon*link.getPosition()[0] - Math.sqrt(3)/2*rayon*link.getPosition()[1];
		double py = y0 + 1.5*rayon*link.getPosition()[1];
		polyDraw = new Polygon(COORD_X(rayon, px,this.link.orien),COORD_Y(rayon, py, this.link.orien),4);
		polyClick = new Polygon(COORD_X_CLICK(rayon, px,this.link.orien),COORD_Y_CLICK(rayon, py, this.link.orien),4);
		setBounds(0,0,width,height);
	}
	
	public void paint(Graphics g) {
		if (link.isInPath()) color=COLOR_PATH;
		else if (link.isBlocked()) color = COLOR_BLOCKED;
		else if(link.isLocked()) color = COLOR_LOCKED;
		else color=COLOR;
		g.setColor(color);
		g.fillPolygon(polyDraw);
	}
	
	public boolean contains(double x, double y) {return polyClick.contains(x, y);}
	public boolean contains(Point p) {return polyClick.contains(p);}
	
	public void click() {
		link.block();
	}
	
	public static int[] COORD_X(double rayon, double px, Orientation o) {
		int[] x = new int[5];
		if (o == Orientation.W) {
			double m = px - rayon*Math.sqrt(3)/2;
			double add = DILLATATION*(1-ActiveHexagon.DILLATATION)*rayon*Math.sqrt(3)/2;
			x[0] = x[3] = (int) (m+add);
			x[2] = x[1] = (int) (m-add);
		} else if (o == Orientation.N) {
			double m = px - rayon*Math.sqrt(3)/4;
			double add1 = DILLATATION*(1-ActiveHexagon.DILLATATION)*rayon*Math.sqrt(3)/4;
			double add2 = rayon*Math.sqrt(3)/4*ActiveHexagon.DILLATATION;
			x[0] = (int) (m+add2+add1);
			x[1] = (int) (m+add2-add1);
			x[2] = (int) (m-add2-add1);
			x[3] = (int) (m-add2+add1);
		} else if (o == Orientation.NE) {
			double m = px + rayon*Math.sqrt(3)/4;
			double add1 = - DILLATATION*(1-ActiveHexagon.DILLATATION)*rayon*Math.sqrt(3)/4;
			double add2 = - rayon*Math.sqrt(3)/4*ActiveHexagon.DILLATATION;
			x[0] = (int) (m+add2+add1);
			x[1] = (int) (m+add2-add1);
			x[2] = (int) (m-add2-add1);
			x[3] = (int) (m-add2+add1);
		}
		return x;
	}
	
	public static int[] COORD_Y(double rayon, double py, Orientation o) {
		int[] x = new int[5]; // en fait il s'agit de y
		if (o == Orientation.W) {
			x[0] = x[1] = (int) (py-rayon/2*ActiveHexagon.DILLATATION);
			x[2] = x[3] = (int) (py+rayon/2*ActiveHexagon.DILLATATION);
		} else {
			double m = py - rayon*3/4;
			double add1 = DILLATATION*(1-ActiveHexagon.DILLATATION)*rayon*3/4;
			double add2 = rayon/4*ActiveHexagon.DILLATATION;
			x[0] = (int) (m-add2+add1);
			x[1] = (int) (m-add2-add1);
			x[2] = (int) (m+add2-add1);
			x[3] = (int) (m+add2+add1);
		}
		return x;
	}
	
	public static int[] COORD_X_CLICK(double rayon, double px, Orientation o) {
		int[] x = new int[5];
		if (o == Orientation.W) {
			double m = px - rayon*Math.sqrt(3)/2; // milieu du rectagle
			double add = (1-ActiveHexagon.DILLATATION)*rayon*Math.sqrt(3)/2; // ecratement par rapport au milieu
			x[0] = x[3] = (int) (m+add);
			x[2] = x[1] = (int) (m-add);
		} else if (o == Orientation.N) {
			double m = px - rayon*Math.sqrt(3)/4;
			double add1 = (1-ActiveHexagon.DILLATATION)*rayon*Math.sqrt(3)/4;
			double add2 = rayon*Math.sqrt(3)/4*ActiveHexagon.DILLATATION;
			x[0] = (int) (m+add2+add1);
			x[1] = (int) (m+add2-add1);
			x[2] = (int) (m-add2-add1);
			x[3] = (int) (m-add2+add1);
		} else if (o == Orientation.NE) {
			double m = px + rayon*Math.sqrt(3)/4;
			double add1 = - (1-ActiveHexagon.DILLATATION)*rayon*Math.sqrt(3)/4;
			double add2 = - rayon*Math.sqrt(3)/4*ActiveHexagon.DILLATATION;
			x[0] = (int) (m+add2+add1);
			x[1] = (int) (m+add2-add1);
			x[2] = (int) (m-add2-add1);
			x[3] = (int) (m-add2+add1);
		}
		return x;
	}
	
	public static int[] COORD_Y_CLICK(double rayon, double py, Orientation o) {
		int[] x = new int[5]; // en fait il s'agit de y
		if (o == Orientation.W) {
			x[0] = x[1] = (int) (py-rayon/2*ActiveHexagon.DILLATATION);
			x[2] = x[3] = (int) (py+rayon/2*ActiveHexagon.DILLATATION);
		} else {
			double m = py - rayon*3/4;
			double add1 = (1-ActiveHexagon.DILLATATION)*rayon*3/4;
			double add2 = rayon/4*ActiveHexagon.DILLATATION;
			x[0] = (int) (m-add2+add1);
			x[1] = (int) (m-add2-add1);
			x[2] = (int) (m+add2-add1);
			x[3] = (int) (m+add2+add1);
		}
		return x;
	}
	
	public Polygon getPolyDraw() {
		return new Polygon(this.polyDraw.xpoints,this.polyDraw.ypoints,this.polyDraw.npoints);
	}
	
	public void lock() { link.lock(); }
}
