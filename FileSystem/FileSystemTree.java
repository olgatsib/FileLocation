/* Olga Tsibulevskaya, IFT1025, 21 septembre 2012 

Le programme va chercher le répertoire spécifié dans la ligne de commande
non seulement dans le répertoire courant, mais dans tous ses 
sous-répertoires et sous-répertoires de sous-répertoires 
et ainsi de suite (donc, à partir du répertoir courant).
*/

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.lang.ClassNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.text.*;

/** 
 * Classe qui execute le programme  
 * 
 * @author Olga Tsibulevskaya (TSIO24627906) 
 * 
 */
public class FileSystemTree implements Iterable<TreeNode<FileInfo>> {
    public static File dir = null;
    // pour détecter une erreur si le fichier n'est pas trouvé
    public static boolean fileFound = false; 
    // les variables pour la statistique
    public static int count = 0;
    public static long sizeTotal = 0;
    public static long sizeMin = Long.MAX_VALUE;
    public static long sizeMax = Long.MIN_VALUE;

       
    public void execute(String[] args) {
	String start = new String();
	String goal = new String();
		
	int task = 0;
	// les commandes 1,2,3,5
	if (args.length == 3) {
	    // les commandes 1,2,3
	    if ((args[0].equalsIgnoreCase("locate") && ! args[1].equalsIgnoreCase("mytree.fstree")) || args[0].equalsIgnoreCase("create")) {
		// la commande 1
		if (args[0].equalsIgnoreCase("locate"))  {
		    start = args[1]; // directory to start searching files from
		    goal = args[2]; // file to find
		    task = 1;
		}
		// la commande 2
		else if (args[0].equalsIgnoreCase("create")) {
		    start = args[2];
		    task = 2;
		}
		File directory = new File(start);
		// le répertoire est donné par le chemin absolu  
		if (directory.isDirectory() && ! directory.getName().equals(".")) {
		    dir = directory;
		}
		// le répertoire est donné soit par le nom et il faut le retrouver dans les sous-répertoires du répertoire courant, soit c'est le répertoire courant et il faut obtenir son chemin absolu
		else {
		    findDir(start);
		}
		if (dir != null) {
		    if (dir.isDirectory()) {
			if (task == 1) {
			    findFile(dir, goal, null); //TODO
			    if (! fileFound)
				System.out.println("Le fichier " + goal + " n'est pas rétrouvés dans le répertoire " + start);
			}
			else if (task == 2) {
			    FSTree fst = new FSTree();
			    // écrire l'arbre dans le fichier
			    try {
				fst = new FSTree(dir);
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("mytree.fstree"));
				out.writeObject(fst);
				out.close();
			    }
			    catch(IOException e) {
				e.printStackTrace();
			    }
			    System.out.println("Écriture du fichier est terminée");
			}
		    }
		    else
			System.out.println(dir + " n'est pas le répertoire");
		}
		else 
		    System.out.println("Le répertoire " + start + " n'exist pas");
	    }
	    // la commande no.3
	    else if (args[0].equalsIgnoreCase("locate") && args[1].equalsIgnoreCase("mytree.fstree")) {
		goal = args[2];
		task = 3;
		FSTree myTree = new FSTree();
		myTree = getTree(); // créer l'arbre à partir du fichier;
		TreeNode<FileInfo> root = myTree.getRoot();
		// rechercher les fichiers
		traverse(myTree, root, goal, task);
		if (! fileFound)
		    System.out.println("Le fichier " + goal + " n'est pas rétrouvé");
	    }
	    // la commande 5
	    else if (args[0].equalsIgnoreCase("stat")) {
		goal = args[2];
		task = 5;
		FSTree tree = getTree();
		TreeNode<FileInfo> root = tree.getRoot();
		// rechercher les fichiers pour obtenir l'information pour la statistique
		traverse(tree, root, goal, task);
		if (! fileFound)
		    System.out.println("Le fichier " + goal + " n'est pas retrouvé");
		else {
		    DecimalFormat df = new DecimalFormat("#.##");
		    System.out.println("Nombre de fichier trouvés: " + count);
		    System.out.println("Taille moyenne: " + df.format(sizeTotal*1.0/count/1024) + " Kb");
		    System.out.println("Taille du plus petit: " + df.format(sizeMin*1.0/1024) + " Kb");
		    System.out.println("Taille du plus gros: " + df.format(sizeMax*1.0/1024) + " Kb");
		    System.out.println("Taille totale: " + df.format(sizeTotal*1.0/1024) + " Kb");
		}
	    }
	    else
		errorMessage();
	}
	// la commande no. 4 
	else if (args.length == 2) {
	    if (args[0].equalsIgnoreCase("update")) {
		// créer l'arbre à partir du fichier 
		FSTree tree = getTree();
		update(tree);
	    }
	    else
		errorMessage();
	}
	else {
	    errorMessage();
	}
    }
    /**
     * La méthode pour écrire le message en cas d'erreur 
     * dans la ligne de commande
     */
    public static void errorMessage() {
	System.out.println("La commande est incorrecte ou manquante, utilisez les formes suivantes:");
	System.out.println("java FileSystemTree locate directory_path file_name");
	System.out.println("java FileSystemTree create mytree.fstree directory_path");
	System.out.println("java FileSystemTree locate mytree.fstree file_name");
	System.out.println("java FileSystemTree stat mytree.fstree file_name");
	System.out.println("java FileSystemTree update mytree.fstree");
	System.exit(0);
    }
    /**
     * La méthode pour retrouver le répertoire 
     * @param start nom de répertoire à trouver
     */
    public static void findDir(String start) {
	try {
	    String s = new File(".").getCanonicalPath();
	    File curDir = new File(s);
	    String nameCurrentDir = curDir.getName();
	    // si le répertoire recherché est donné par "." 
	    if (nameCurrentDir.equals(start) || start.equals(".")) {
		dir = curDir;
	    }
	    // si le répertoire donné par le nom, il faut le rétrouver récursivement
	    else {
		findDirToStart(curDir, start);
	    }
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }
    /**
     * La méthode recursive qui cherche le répertoire par le nom dans 
     * tous les sous-répertoires à partir de répertoire courant
     * @param file le premier répertoir à commencer les recherches
     * @param start le nom de répertoire à retrouver 
     */
    public static void findDirToStart(File file, String start) {
	try {
	    // vérifier si le répertoire contient les liens symboliques
	    if (! isSymlink(file)) {
		try {
		    for (File f : file.listFiles()) {
			if (! f.getName().equals(start)) 
			    findDirToStart(f, start);
			else 
			    dir = f;
		    }	
		}
		catch (NullPointerException e) {
		}
	    }
	}
	catch (IOException ex) {
	    ex.printStackTrace();
	}
    }
    // La variable de classe pour utiliser la méthode iterator()
    private FSTree m = new FSTree();
    /**
     *  La méthode qui redéfinit la méthode iterator()
     * @return iterator de la classe MyTree
     */
    public Iterator iterator() {
	return m.iterator();
    }
    /**
     * La méthode vérifie si la structure des fichier souvgardée 
     * correcpond à l'état actuel du system
     * @param tree l'arbre des fichies souvgardé
     */
    public static void update(FSTree tree) {
	// créer l'arbre qui correspond à l'état actuel pour le comparer avec 
	// l'arbre souvgardé
	FSTree treeFS = new FSTree();
	try {
	    String s = tree.getRoot().getData().getNameF();
	    File root = new File(s);
	    // rétrouver le chemin vers le répertoire qui est le root dans l'arbre souvgardé
	    if (! root.isDirectory()) {
		findDir(s);
		if (dir == null)
		    System.out.println("Le répertoire " + s + " n'est pas retrouvé");
		else {
		    root = dir;
	    	    treeFS = new FSTree(root);
		}
	    }
	    else {
		treeFS = new FSTree(root);
	    }
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
	// iteratorFS et nodeFS pour l'arbre actuel
	// iterator et node pour l'arbre souvgardé
	Iterator<TreeNode<FileInfo>> iteratorFS = treeFS.iterator();
	Iterator<TreeNode<FileInfo>> iterator = tree.iterator();
	// root de la system actuelle est vide, mais dans le fichier 
	// l'arbre a des enfants, il faut faire l'update de l'arbre completement
	if (treeFS.getRoot().numberOfChildren() == 0 && tree.getRoot().numberOfChildren() != 0) {
	    printDeletedChildren(tree.getRoot());
	    tree = treeFS;
	    System.out.println("here");
	}
	// root de l'arbre souvgardé est vide, mais actuallement 
	// il y a des enfants, il faut faire l'update de l'arbre completement
	else if (treeFS.getRoot().numberOfChildren() != 0 && tree.getRoot().numberOfChildren() == 0) {
	    tree = treeFS;
	    addNewFiles(treeFS.getRoot());
	}
	// les roots étaient changés, il faut faire l'update de l'arbre
	// dans le fichier completement
	else if (treeFS.getRoot().numberOfChildren() == 0 && tree.getRoot().numberOfChildren() == 0) {
	    if (! treeFS.getRoot().equals(tree.getRoot()))
		tree = treeFS;
	}
	// parcourir les deux arbres simultanement et les comparer
	else {
	    while (iterator.hasNext()) {
		TreeNode<FileInfo> node = iterator.next();
		TreeNode<FileInfo> nodeFS = iteratorFS.next();
		
		// vérifier si les enfants du noeud étaient modifiés
		// d'abord vérifier si les nodes aient des enfants
		if (nodeFS.numberOfChildren() != 0 && node.numberOfChildren() != 0) {
		    // vérifier si quelqu'un enfant est manquant 
		    if (node.numberOfChildren() != nodeFS.numberOfChildren()) {
			differentSize(node, nodeFS);
		    }
		    // la quantité est la même, vérifie s'ii les enfants sont identiques
		    int i = 0;
		    for (i = 0; i < node.getChildren().size(); i++) {
			// en éxecutant la boucle for on pourrait ajouter ou 
			// supprimer les enfants, donc il faut vérifier la quantité encore
			if (node.getChildren().size() == nodeFS.getChildren().size()) {
			    // la quantité est la même, pourraient être modifiés
			    sameSize(node, nodeFS, i);
			}
			else {
			    // la quantité est differente, trouver les enfants supprimés ou ajoutés
			    differentSize(node, nodeFS);
			}
		    } // end of for
		} // end of hasChildren()
		// pas d'enfants, le cas pour le root, il faut recopier tout l'arbre
		else {
		    // tous les fichier sans enfants étaient modifiés 
		    // à partir de leurs parents, donc, rien à faire ici    
		}
	    } 
	} // fin de la boucle while
	// sauvegarder l'arbre après l'update dans le fichier
	try {
	    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("mytree.fstree"));
	    out.writeObject(tree);
	    out.close();
	}
	catch(IOException e) {
	    e.printStackTrace();
	}
    }
    /*
      La méthode compare deux nodes si le nombre initial des enfants sont le même
    */
    public static void sameSize(TreeNode<FileInfo> node, TreeNode<FileInfo> nodeFS, int i) {
	// l'arbre souvegardé n'a pas de noeud, donc il faul l'ajouter 
	if (! node.getChildren().contains(nodeFS.getChild(i))) {
	    System.out.println("added: " + nodeFS.getChild(i));
	    node.addChild(i, nodeFS.getChild(i));
	    if (node.getChild(i).hasChildren()) {
		// ajouter tous les enfants (sous-répertoires) de noeud ajouté
		addNewFiles(node.getChild(i));
	    }
	}
	// l'arbre souvegardé a un noeud qui n'existe plus dans le system, donc il faut le supprimer
	else if (! nodeFS.getChildren().contains(node.getChild(i))) {
	    if (node.getChild(i).hasChildren()) {
		System.out.println("deleted: " + node.getChild(i));
		// supprimer tous les enfants de noeud supprimé
		printDeletedChildren(node.getChild(i));
		node.removeChild(i);
	    }
	}
	// le nombre des enfants sont le même, vérifier si les fichier étaient modifiés
	else {
	    for (int j = 0; j < node.getChildren().size(); j++) {
		// vérifier la date de modofocation si les noms des fichier sont les mêmes 
		if (node.getChild(i).equals(nodeFS.getChild(j)) && (node.getChild(i).getData().getDateF().compareTo(nodeFS.getChild(j).getData().getDateF()) != 0)) {
		    // vérifier si le noeud est fichier, sinon, la modification de répertoire 
		    // ne signifie pas que le répertoire était modifié,
		    // mais seulement les fichier qu'il contient
		    if (node.getChild(i).numberOfChildren() == 0) {
			System.out.println("modified: " + nodeFS.getChild(j));
			// changer les fichiers
			node.removeChild(i);
			node.addChild(i, nodeFS.getChild(j));
		    }
		    else {
			// rien à faire si c'est un répertoire, 
			// parce que ses enfants (qui étaient modifiés) seront vérifier plus tard à leur tour
		    }
		}
	    }
	}
    } 
    /*
     * La méthode compare deux nodes si le nombre des enfants sont different
     */
    public static void differentSize(TreeNode<FileInfo> node, TreeNode<FileInfo> nodeFS) {
	// le cas quand l'arbre souvegardé a moins de noeuds que l'arbre du système
	if (node.numberOfChildren() < nodeFS.numberOfChildren()) {
	    for (int i = 0; i < nodeFS.getChildren().size(); i++) {
		if (! node.getChildren().contains(nodeFS.getChild(i))) {
		    System.out.println("added: " + nodeFS.getChild(i));
		    node.addChild(i, nodeFS.getChild(i));
		    if (node.getChild(i).hasChildren()) {
			addNewFiles(node.getChild(i));
		    }	
		}
	    }
	}
	// le cas quand l'arbre souvegardé a plus de noeuds que l'arbre du système
	if (node.numberOfChildren() > nodeFS.numberOfChildren()) {
	    for (int i = 0; i < node.getChildren().size(); i++) {
		if (! nodeFS.getChildren().contains(node.getChild(i))) {
		    System.out.println("deleted: " + node.getChild(i));
		    printDeletedChildren(node.getChild(i));
		    node.removeChild(i);
		}
	    }
	}
    }
    /*
     * La méthode affiche tous les fichiers qui étaient supprimés
     */
    public static void printDeletedChildren(TreeNode<FileInfo> parent) {
	for (TreeNode<FileInfo> child : parent.getChildren()) {
	    System.out.println("deleted: " + child);
	    if (child.hasChildren())
		printDeletedChildren(child);
	}
    }
    /*
     * La méthode affiche tous les enfants qui étaient ajoutés
     */
    public static void addNewFiles(TreeNode<FileInfo> parent) {
	 for (TreeNode<FileInfo> child : parent.getChildren()) {
	     System.out.println("added: " + child);
	     if (child.hasChildren())
		 addNewFiles(child);
	 }
    } 
    /**
     * La méthode recherche le fichier par son nom à partir du fichier File donné
     * @param root du type File à partir du lequel il faut commencer
     * @param goal le nom du fichier à retrouver
     */
    public List<String> locate(File root, String goal)
    {
    	List<String> list = new LinkedList<String>();
    	findFile(root, goal, list);
    	return list;
    }
    public void findFile(File root, String goal, List<String> list) {
    	//System.out.println(root.getName() + " " + goal);
	try {
	    if (root.isDirectory() && ! isSymlink(root)) {
		File[] files = root.listFiles();
		if (files != null) {
		    for (File child : files)
			findFile(child, goal, list);
		}
	    }
	    if (root.isFile()) {
		// le fichier à retrouver a du type "*.extension"
		if (goal.substring(0,1).equals("*")) {
		    if (root.getName().endsWith(goal.substring(1))) {
			fileFound = true;
			String s = root.getPath() + " " + root.length() + " " + new Date(root.lastModified());
			list.add(s);
			//System.out.println(root.getPath() + " " + root.length() + " " + new Date(root.lastModified()));
		    }
		}
		// le fichier à retrouver a du type "nom.*"
		else if (goal.endsWith("*")) {
		    String s = goal.substring(0,goal.length()-1);
		    if (root.getName().startsWith(s)) {
			fileFound = true;
			//System.out.println(root.getPath() + " " + root.length() + " " + new Date(root.lastModified()));
			String str = root.getPath() + " " + root.length() + " " + new Date(root.lastModified());
			list.add(str);
		    }
		}
		// le fichier à retrouver a du type "nom.extension"
		else if (root.getName().equals(goal) || root.getName().contains(goal)) {
		    fileFound = true;
		    //System.out.println(root.getPath() + " " + root.length() + " " + new Date(root.lastModified()));
		    String s = root.getPath() + " " + root.length() + " " + new Date(root.lastModified());
		    list.add(s);
		}
		else {
		}
	    }
	}
	catch (IOException ex) {
	    ex.printStackTrace();
	}
    }
    /**
     * La méthode qui crée l'arbre à partir du fichier "mytree.fstree"
     * @return l'arbre de la classe FSTree 
     */
    public static FSTree getTree() { 
	FSTree treeF = new FSTree();
	try {
	    File f = new File("mytree.fstree");
	    // lire le fichier
	    try {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		// créer l'arbre 
		treeF = (FSTree)in.readObject();
		in.close();
	    }
	    catch (ClassNotFoundException ex) {
		System.out.println("Classe n'est pas retrouvée");
	    }
	}
	catch (IOException ex) {
	    ex.printStackTrace();
	} 
	return treeF;
    }
    /**
     * La méthode recherche le fichier par son nom à partir du noeud du type TreeNode
     * @param tree du type FSTree, l'arbre qu'il faut traverser pour trouver le fichier
     * @param root le noeud à partir duquel il faut commencer
     * @param goal le nom du fichier à trouver
     * @param task le numero de la tâche à executer 
     */
    public static void traverse(FSTree tree, TreeNode<FileInfo> root, String goal, int task) {
	// le fichier à retrouver a du type "*.extension"
	if (goal.substring(0,1).equals("*")) {
	    if (root.getData().getNameF().endsWith(goal.substring(1))) {
		fileFound = true;
		if (task == 3)
		    print(root);
		else 
		    stat(root);
	    }
	}
	// le fichier à retrouver a du type "nom.*"
	if (goal.endsWith("*")) {
	    String s = goal.substring(0,goal.length()-1);
	    if (root.getData().getNameF().startsWith(s)) {
		fileFound = true;
		if (task == 3)
		    print(root);
		else 
		    stat(root);
	    }
	}
	// le fichier à retrouver a du type "name.extension"
	if (root.getData().getNameF().equals(goal)) {
	    fileFound = true;
	    if (task == 3)
		print(root);
	    else 
		stat(root);
	}
	// appel recursif
	ArrayList<TreeNode<FileInfo>> al = root.getChildren();
	for (TreeNode<FileInfo> child : al)
	    traverse(tree, child, goal, task);
    }
    /**
     * La méthode qui affiche le chemin à partir du répertoire qui est root pour l'arbre crée
     * @param tn le noeud root du type TreeNode
     */
    public static void print(TreeNode<FileInfo> tn) {
	String s = "";
	if (tn.hasParent()) {
	    printPath(tn, s); 
    	}
	System.out.println(tn);
    }
    /**
     * La méthode qui utilise l'appel recursif pour afficher tous les fichiers
     * @param tn le noeud root du type TreeNode
     * @param s string qui append les noms des fichiers precedents
     */
    public static void printPath(TreeNode<FileInfo> tn, String s) {
	if (tn.hasParent()) {
	    printPath(tn.getParent(), s);
	    s += tn.getParent().getData().getNameF() + "/";
	}
    	System.out.print(s);
    }
    /**
     * La méthode faire la statistique
     * @param f le noeud retrouvé pour lequel il faut obtenir l'information
     */
    public static void stat(TreeNode<FileInfo> f) {
	count++;
	sizeTotal += f.getData().getSizeF();
	if (f.getData().getSizeF() < sizeMin)
	    sizeMin = f.getData().getSizeF();
	if (f.getData().getSizeF() > sizeMax)
	    sizeMax = f.getData().getSizeF();
    }
    /**
     * La méthode qui vérifie si le répertoire a un lien symbolique
     */
    public static boolean isSymlink(File file) throws IOException {
	if (file == null)
	    throw new NullPointerException("File must not be null");
	File canon;
	if (file.getParent() == null) {
	    canon = file;
	} else {
	    File canonDir = file.getParentFile().getCanonicalFile();
	    canon = new File(canonDir, file.getName());
	}
	return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }
}
