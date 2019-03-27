import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Version visuelle des pavés héxagonaux
 */
public class ActiveHexagon extends Component {
	private static final long serialVersionUID = 1L;
	
	public final Hexagon hexa;
	private Polygon poly; // le polygone de selection coïncide avec le polygone du dessin
	private Color color = ActiveHexagon.PICK_COLOR(); // couleur aléatoire pour l'esthétique
	
	public final static Color LOCKED_COLOR = new Color(1,3,236);
	public final static double DILLATATION = 0.75;
	public final static double TEXT_SIZING = 0.7;
	public final static Font TEXT_FONT = new Font(" TimesRoman ",Font.BOLD,0); // Police
	
	public ActiveHexagon(double rayon, double x0, double y0, Hexagon hex, int width, int height) {
		hexa = hex;
		updateActiveHexagon(rayon,x0,y0,width,height);
	}
	
	/**
	 * Mise à jour des polygones de tracé
	 */
	public void updateActiveHexagon(double rayon, double x0, double y0, int width, int height) {
		double px = x0 + Math.sqrt(3)*rayon*this.hexa.getPosition()[0] - Math.sqrt(3)/2*rayon*this.hexa.getPosition()[1];
		double py = y0 + 1.5*rayon*this.hexa.getPosition()[1];
		
		poly = new Polygon(COORD_X(rayon, px, py),COORD_Y(rayon, px, py),6);
		
		setBounds(0,0,width,height);
	}
	
	public void paint(Graphics g) {
		if (hexa.isLocked()) g.setColor(LOCKED_COLOR);
		else g.setColor(color);
		
		g.fillPolygon(poly);
		
		if (hexa.maxLink>-1) {
			float size = (float) (Math.abs(poly.xpoints[0]-poly.xpoints[2])*TEXT_SIZING);
			g.setFont(TEXT_FONT.deriveFont(size)); // nouvelle police
			g.setColor(Color.WHITE);
			// calcul des coordonnï¿½es pour centrer le texte
			Rectangle2D rect = g.getFontMetrics().getStringBounds(String.valueOf(hexa.maxLink), g);
			g.drawString(String.valueOf(hexa.maxLink), (int) ((poly.xpoints[0]+poly.xpoints[2]-rect.getWidth())/2), (int) ((poly.ypoints[0]+poly.ypoints[2])/2));
		}
	}
	
	public boolean contains(double x, double y) {
		return poly.contains(x, y);
	}
	
	public boolean contains(Point p) {
		return poly.contains(p);
	}
	
	public static int[] COORD_X(double rayon, double px, double py) {
		int[] x = new int[6];
		for (int i=0 ; i<6 ; i++) {
			x[i] = (int) (px + DILLATATION*rayon*Math.cos(i*Math.PI/3 + Math.PI/6));
		}
		return x;
	}
	
	public static int[] COORD_Y(double rayon, double px, double py) {
		int[] x = new int[6];
		for (int i=0 ; i<6 ; i++) {
			x[i] = (int) (py + DILLATATION*rayon*Math.sin(i*Math.PI/3 + Math.PI/6));
		}
		return x;
	}
	
	/**
	 * Generation d'une couleur aléatoire
	 */
	public static Color PICK_COLOR() {
		int[][] listColor = {{0,73,80},{0,147,154},{0,134,135},{0,122,135},{0,91,92},{0,163,172},{0,169,166}};
		int i = (int) (Math.random()*listColor.length);
		return new Color(listColor[i][0],listColor[i][1],listColor[i][2]);
	}
	
	public void lock() { hexa.lock(); }
}
