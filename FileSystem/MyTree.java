/* Olga Tsibulevskaya, IFT1025, 21 september 2012 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Classe d'arbre n-aire
 *
 * @author Olga Tsibulevskaya (TSIO24627906)
 */
public class MyTree<T> implements Serializable {

    protected TreeNode<T> root;
    /**
     * Constructeur par default
     */
    public MyTree()  {
	root = null;
    }
    /**
     * Constructeur
     * @param start du type Generic, le root d'arbre
     * @throws IOException
     */
    public MyTree(T start) throws IOException {
	root = new TreeNode<T>(start);
    }
    /**
     * La méthode retournt le root d'arbre
     * @return root
     */
    public TreeNode<T> getRoot() {
	return root;
    }
    /**
     * La méthode qui permet de changer le root
     * @param new root
     */
    public void setRoot(TreeNode<T> newRoot) {
	root = newRoot;
    }
   
    public Iterator<TreeNode<T>> iterator() {
	return new FSTreeIterator();
    }
   
    class FSTreeIterator implements Iterator<TreeNode<T>> {
	private TreeNode<T> cursor;
	private boolean visited = false;
	
	public FSTreeIterator() {
	    visited = false;
	}
    	public TreeNode<T> next() {
	    if (root == null) {
		return null;
	    }
	    if (cursor == null && ! visited) {
		cursor = root;
		visited = true;
		cursor.setNumber(cursor.numberOfChildren());
	    }
	   
	    else if (cursor.getNumber() > 0) {
		cursor = cursor.getChild(0);
		cursor.setNumber(cursor.numberOfChildren());
		int number = cursor.getParent().numberOfChildren() - 1;
		cursor.getParent().setNumber(number);
	    }

	    else {
		cursor = cursor.getParent();
		while (cursor.getNumber() ==  0 && cursor != root) {
		    cursor = cursor.getParent();
		    
		}
		if (! (cursor == root && cursor.getNumber() == 0)) {
		    int index = cursor.numberOfChildren() - cursor.getNumber();
		    cursor = cursor.getChild(index);
		    cursor.setNumber(cursor.numberOfChildren());
		
		    int number = cursor.getParent().getNumber()-1;
		    cursor.getParent().setNumber(number);
		}
	    }
	    return cursor;
	}
	public boolean hasNext() {
	    TreeNode<T>temp = cursor;
	    if (visited) {
		if (cursor.getNumber() > 0)
		    return true;
		else {
		    cursor = cursor.getParent();
		    while (cursor.getNumber() == 0 && cursor != root) {
			cursor = cursor.getParent();
		    }
		    if (cursor.getNumber() > 0) {
			cursor = temp;
			return true;
		    }
		    else
			return false;
		}
	    }
	    return true;
	}
	public void remove() {
	} 
    }
}
