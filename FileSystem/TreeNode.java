/* Olga Tsibulevskaya, IFT1025, 21 september 2012 */

import java.util.ArrayList;
import java.io.File;
import java.io.Serializable;

/**
 * Classe du noeud d'arbre
 *
 * @author Olga Tsibulevskaya (TSIO24627906)
 */
public class TreeNode<T> implements Serializable {

    private T data;
    private ArrayList<TreeNode<T>> children;
    private TreeNode<T> parent;
    private int number = 0; // variable pour l'iterateur
    
    /**
     * Constructeur par default
     */
    public TreeNode() {
	data = null;
	children = null;
	parent = null;
    }
    /**
     * Constructeur
     * @param data du type Generic 
     */
    public TreeNode(T data) {
	this.data = data;
	children = new ArrayList<TreeNode<T>>();
	parent = null;
	number = numberOfChildren();
    }
    /**
     * La méthode retourne parent du noeud
     * @return parent
     */
    public TreeNode<T> getParent() {
	return parent;
    }
    /**
     * La méthode fixe le parent
     * @param parent à rétablir
     */
    public void setParent(TreeNode<T> parent) {
	this.parent = parent;
    }
    /**
     * la méthode retourne true si le noeud a un parent et faux sinon
     * @return boolean
     */
    public boolean hasParent(){
	return parent != null;
    }
    /**
     * la méthode ajoute les enfants
     * @param child l'enfant à ajouter
     */
    public void addChild(TreeNode<T> child) {
	children.add(child);
    }
    /**
     * La méthode ajoute l'enfant dans la position specifiée
     * @param index la position de l'enfant à ajouter
     * @param child le noeud 'à ajouter
     */
    public void addChild(int index, TreeNode<T> child) {
	children.add(index, child);
	child.setParent(this);
    }
    /**
     * La méthode supprime l'enfant
     * @param index la position de l'enfant à supprimer
     */
    public void removeChild(int index) {
	children.remove(index);
    }
    /**
     * La méthode retourn l'information sur le noeud
     * @return data
     */
    public T getData() {
	return data;
    }
    /**
     * La méthode vérifie si le noeud a des enfants
     * @return boolean
     */
    public boolean hasChildren() {
	return children != null;
    }
    /**
     * La méthode qui retourne le tableau des enfants
     * @return tableau des enfants
     */
    public ArrayList<TreeNode<T>> getChildren() {
	return children;
    }
    /**
     * La méthode retourne l'enfant du noeud sur la position specifiée
     * @return enfant du noeud
     */
    public TreeNode<T> getChild(int index) {
	return children.get(index);
    }
    /**
     * La méthode retourne le nombre des enfants
     * @return nombre des emfants
     */
    public int numberOfChildren() {
	return children.size();
    }
    /*
     * la méthode fixe le nombre des enfants pour l'iterator
     */
    public void setNumber(int number) {
	this.number = number;
    }
    /*
      La méthode retourne le nombre des enfants restes pour l'iterator
     */
    public int getNumber() {
	return number;
    }
    /**
     * La méthode affiche le noeud
     * @return string
     */
    public String toString() {
	return data.toString();
    }
    // méthode redéfini
     public boolean equals(Object o) {
	if (!(o instanceof TreeNode))
	    return false;
	if (this == o)
	    return true;
	else {
	    TreeNode<T> another = (TreeNode<T>) o;
	    return data.equals(another.data);
	}
    }
}
