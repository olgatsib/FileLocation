/* Olga Tsibulevskaya, IFT1025, 21 septembre 2012 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Classe crée l'arbre qui contient les répertoires et les fichiers du système
 * 
 * @author Olga tsibulevskaya (TSIO24627906)
 */
public class FSTree extends MyTree implements Serializable {
    // pour serialisation
    private static final long serialVersionUID = -8503805597818313288L;

    /**
     * Constructeur par default appel à la super classe myTree
     */
    public FSTree()  {
	super();
    }
    /**
     * Constructeur de la classe 
     * @param start fichier à partir duquel l'arbre sera crée
     * @throws IOException
     */
    public FSTree(File start) throws IOException {
	root = new TreeNode<FileInfo>(new FileInfo(start));
	if (start.isDirectory())
	    makeTree(root, start);
	else
	    System.out.println(start + " is not a directory");
    }
    /**
     * La méthode qui crée l'arbre
     * @param tn le noeud root de l'arbre
     * @param parent fichier root de l'arbre
     */
    public void makeTree(TreeNode<FileInfo> tn, File parent) {
	for (File f : parent.listFiles()) {
	    TreeNode<FileInfo> child = new TreeNode<FileInfo>(new FileInfo(f));
	    TreeNode<FileInfo> parentTN = new TreeNode<FileInfo>(new FileInfo(parent));
	    tn.addChild(child);
	    child.setParent(tn);
	    if (f.isDirectory())
		makeTree(child, f);
	}
    }
    /**
     * La méthode retourne le root de l'arbre
     * @return root de l'arbre
     */
    public TreeNode<FileInfo> getRoot() {
	return root;
    }
    /**
     * La méthode qui affiche l'arbre
     * @return string qui contient tous les fichiers
     */
    public String toString(){
	String s = "Root: " + printTree(this.root);
    	return s;
    }
    // La méthode avec l'appel recursif pour afficher tous les enfants
    public String printTree(TreeNode<FileInfo> node) {
	String s = node.toString();
	for (TreeNode<FileInfo> child : node.getChildren())
	    s += "\nchild of " + child.getParent().getData().getNameF() + ": " + printTree(child);
	return s;
    }
}
