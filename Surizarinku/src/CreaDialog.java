import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;

/**
 * Fenetre de dialogue appelee lors de la generation d un pavage
 */
public class CreaDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public boolean data; // L utilisateur a clicke sur OK ?
	private JPanel panNom, panForme, panTaille, panDif;
	private JComboBox<String> forme = new JComboBox<String>();
	private JRadioButton tranche1, tranche2, tranche3, tranche4;
	private JTextField nom, abcisse, ordonnee;
	
	public CreaDialog(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
	    this.setSize(550, 270);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.setNom();
	    this.setTaille();
	    this.setDifficulte();
	    this.setForme();
	    this.initComponent();
	}
	
	public boolean showDialog() {
		this.setVisible(true);
		return data;
	}
  
  private void setNom() {
	  	panNom = new JPanel();
	    panNom.setBackground(Color.white);
	    panNom.setPreferredSize(new Dimension(220, 60));
	    nom = new JTextField();
	    nom.setPreferredSize(new Dimension(100, 25));
	    panNom.setBorder(BorderFactory.createTitledBorder("Nom du terrain :"));
	    JLabel nomLabel = new JLabel("Saisir un nom :");
	    panNom.add(nomLabel);
	    panNom.add(nom);
  }
  
  public void setTaille() {
	  panTaille = new JPanel();
	  panTaille.setBackground(Color.white);
	  panTaille.setPreferredSize(new Dimension(220, 60));
	  panTaille.setBorder(BorderFactory.createTitledBorder("Taille du terrain"));
	  JLabel tailleLabel = new JLabel("Abscisse : ");
	  JLabel taille2Label = new JLabel("Ordonnée : ");
	  abcisse = new JTextField("10");
	  ordonnee = new JTextField("10");
	  abcisse.setPreferredSize(new Dimension(50, 25));
	  ordonnee.setPreferredSize(new Dimension(50, 25));
	  panTaille.setLayout(new GridLayout(2,2));
	  panTaille.add(tailleLabel);
	  panTaille.add(abcisse);
	  panTaille.add(taille2Label);
	  panTaille.add(ordonnee);
  }
  
  public void setDifficulte() {
	  panDif = new JPanel();
	  panDif.setBackground(Color.white);
	  panDif.setBorder(BorderFactory.createTitledBorder("Difficulté du terrain :"));
	  panDif.setPreferredSize(new Dimension(440, 60));
	  tranche1 = new JRadioButton("Très facile");
	  tranche2 = new JRadioButton("Facile");
	  tranche3 = new JRadioButton("Moyen");
	  tranche4 = new JRadioButton("Difficile");
	  tranche3.setSelected(true);
	  ButtonGroup bg = new ButtonGroup();
	  bg.add(tranche1);
	  bg.add(tranche2);
	  bg.add(tranche3);
	  bg.add(tranche4);
	  panDif.add(tranche1);
	  panDif.add(tranche2);
	  panDif.add(tranche3);
	  panDif.add(tranche4);
  }
  
  public void setForme() {
	  panForme = new JPanel();
	  panForme.setBackground(Color.white);
	  panForme.setPreferredSize(new Dimension(220, 60));
	  panForme.setBorder(BorderFactory.createTitledBorder("Forme du terrain :"));
	  forme.addItem("Carre");
	  forme.addItem("Rond");
	  forme.addItem("Triangulaire");
	  JLabel formeLabel = new JLabel("Forme : ");
	  panForme.add(formeLabel);
	  panForme.add(forme);
  }
  
  private void initComponent(){       

    JPanel content = new JPanel();
    content.setBackground(Color.white);
    content.add(panNom);
    content.add(panForme);
    content.add(panDif);
    content.add(panTaille);

    JPanel control = new JPanel();
    JButton okBouton = new JButton("OK");
    
    okBouton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {
          data=true;
          setVisible(false);
      }
    });

    JButton cancelBouton = new JButton("Annuler");
    cancelBouton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {
    	  setVisible(false);
    	  data=false;
      }      
    });

    control.add(okBouton);
    control.add(cancelBouton);
    
    this.getContentPane().add(content, BorderLayout.CENTER);
    this.getContentPane().add(control, BorderLayout.SOUTH);
  }
  
  	public int[] getDif(){
      return (tranche1.isSelected()) ? new int[] {1,4} : 
             (tranche2.isSelected()) ? new int[] {5,7} : 
             (tranche3.isSelected()) ? new int[] {8,9} : 
             (tranche4.isSelected()) ? new int[] {10,10000} :
              new int[] {1,10};
    }
  	
  	public Dimension getTaille(){
  		Dimension d = new Dimension();
  		d.width = (abcisse.getText().equals("")) ? 1 : Integer.parseInt(abcisse.getText());
  		if (d.width<1) d.width=1;
  		d.height = (ordonnee.getText().equals("")) ? 1 : Integer.parseInt(ordonnee.getText());
  		if (d.height<1) d.height=1;
  		return d;
    }
  	
  	public String getNom() {
  		return nom.getText();
  	}
  	
  	public String getForme() {
  		return forme.getSelectedItem().toString();
  	}
}