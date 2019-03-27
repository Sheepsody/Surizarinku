import java.awt.*;

/**
 * Image affichee avant qu un pavage soit charge
 */
public class Intro extends Panel {
	private static final long serialVersionUID = 1L;
	
	float size;
	float marge;
	
	public final static String TITLE_KANJI = "六角形";
	public final static String TITLE_KATAKANA = "スリザーリンク";
	public final static String TITLE_ENGLISH = "Hexagonal Slither Link";
	public final static Font TEXT_FONT = new Font(" TimesRoman ",Font.BOLD,0);
	public final static Color BACKGROUND_COLOR = Color.black;
	
	public Intro() {
		super();
		this.setVisible(true);
		setBackground(BACKGROUND_COLOR);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		this.calSize();
		g.setColor(Color.WHITE);
		marge = size/6;
		this.paintKanji(g);
		this.paintKatakana(g);
		this.paintEnglish(g);
	}
	
	public void paintKanji(Graphics g) {
		float textSizeKanji = size;
		g.setFont(TEXT_FONT.deriveFont(textSizeKanji));
		int posX; int posY;
		posX = (int) (this.getSize().width-3*textSizeKanji)/2;
		posY = (int) (this.getSize().height/2-marge*2);
		g.drawString(TITLE_KANJI, posX, posY);
	}
	
	public void paintKatakana(Graphics g) {
		float textSizeKatakana = size/3*2;
		g.setFont(TEXT_FONT.deriveFont(textSizeKatakana));
		int posX; int posY;
		posX = (int) (this.getSize().width-7*textSizeKatakana)/2;
		posY = (int) (this.getSize().height/2+textSizeKatakana);
		g.drawString(TITLE_KATAKANA, posX, posY);
	}
	
	public void paintEnglish(Graphics g) {
		float textSizeEnglish = size/3;
		g.setFont(TEXT_FONT.deriveFont(textSizeEnglish));
		int posX; int posY;
		posX = (int) (this.getSize().width-11*textSizeEnglish)/2;
		posY = (int) (this.getSize().height/2+textSizeEnglish*3.5);
		g.drawString(TITLE_ENGLISH, posX, posY);
	}
	
	public void calSize() {
		int widthSize = this.getSize().width/8;
		int heightSize = this.getSize().height/4;
		if (heightSize<widthSize) size = heightSize;
		else size = widthSize;
	}
}
