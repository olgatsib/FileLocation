/* Olga Tsibulevskaya, IFT1025, 21 september 2012 */

import java.io.File; 
import java.util.Date;
import java.io.Serializable;

/**
 * Classe qui contient l'information sur le fichier (ou le noeud de l'arbre) 
 * @author Olga Tsibulevskaya (TSIO24627906)
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -1291855694094127351L;
    private String name;
    private long size;
    private Date date;
    /**
     * Constructeur
     */
    public FileInfo(File f) {
	name = f.getName();
	size = f.length();
	date = new Date(f.lastModified());
    }
    /**
     * La méthode qui retourne le nom du noeud
     * @return nom du noeud
     */
    public String getNameF() {
	return name;
    }
    /**
     * La méthode qui retourne la taille du noeud (de fichier)
     * @return taille du noeud
     */
    public long getSizeF() {
	return size;
    }
    /**
     * La méthode qui retournt la date de modification du fichier
     * @return date de dernière modification
     */
    public Date getDateF() {
	return date;
    }
    /**
     * La méthode retourne string pour afficher le noeud
     * @return string
     */
    public String toString() {
	return name + " " + size + " " + date;
    }
    // méthode redéfini
    public boolean equals(Object o) {
	if (!(o instanceof FileInfo))
	    return false;
	else 
	    if (this == o)
		return true;
	    else {
		FileInfo another = (FileInfo) o;
		return name.equals(another.name);
	    }
    }
}
