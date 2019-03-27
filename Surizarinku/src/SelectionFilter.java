import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * Filtre applique lors de la selection d un fichier
 */
public class SelectionFilter extends FileFilter {
	public final static String texte = "txt";
    public final static String doc = "odt";
    private static String[] ACCEPTED = {texte,doc};
    public static SelectionFilter SECURE = new SelectionFilter(ACCEPTED);
    
    private String[] accepted;
	
    public SelectionFilter(String[] s) {
    	accepted = s.clone();
    }
    
	public String getDescription() {
		return "Fichier texte (.txt)";
	}
	
	public boolean accept(File f) {
		if (f.isDirectory()) {
	        return true;
	    }

	    String extension = getExtension(f);
	    if (extension != null) {
	    	for (int i=0 ; i<accepted.length ; i++) {
	    		if (extension.equals(accepted[i])) return true;
	    	}
	    }
	    return false;
	}
	
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
