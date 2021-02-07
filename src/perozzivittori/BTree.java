package perozzivittori;

import java.util.LinkedList;
import java.util.List;
//import movida.commons.Person;
//import movida.commons.Movie;

public class BTree implements Dizionario {

	/**
	 * Dati del BTree
	 */
	private static final int t = 2; // Grado BTree t >= 2

	private Node root;				// Radice
	private int height;				// Altezza 	
	
	/**
     * Classe Nodo
     */
	private class Node {
		int m;                             	// Numero di coppie del nodo
		int c;								// Numero di figli del nodo
		InfoBT[] pairs = new InfoBT[2*t];	// Coppie Chiave-Elemento (maggiorate di una per le operazioni di split)
		Node[] children = new Node[2*t+1];	// Figli del nodo (maggiorati di uno per le operazioni di split)

		private Node() {m=0;}
    }
	
	/**
     * Classe Coppia Chiave-Elemento
     */
	private class InfoBT{
		Object elem;
		String key;

		private InfoBT(String k, Object e){
			key = k;
			elem = e;
		}
	}
	
	/**
	 * Costruttore
	 */
	public BTree() {
		root = new Node();
		height = 0;
    }
	
	@Override
	public Object search(String key) {
		if (key != null) return search(root, key, height);
		return null;
	}
	
	/**
	 * Ricerca della chiave tramite ricorsione	
	 * 
	 * @param v il nodo attuale
	 * @param k la chiave da ricercare
	 * @param ht l'altezza attuale
	 * 
	 * @return record con chiave k se esistente, null altrimenti
	 */
	public Object search(Node v, String k, int ht) {
		int pos = 0;
		
		// Ricerca posizione
		while(pos < v.m && k.compareTo(v.pairs[pos].key) > 0) pos++;
		
		if (pos < v.m && k.equals(v.pairs[pos].key)) return v.pairs[pos].elem;
		else {
			if(ht == 0) return null;
			else return search(v.children[pos], k, ht-1);
		}
	}
	
	@Override
	public void insert(String key, Object e) {
		Node tmp = insert(root, key, e, height);
		if (tmp == null) return;
		
		// Split sulla Radice
		Node newRoot = new Node();
		newRoot.pairs[0] = root.pairs[t-1];
		newRoot.children[0] = root;
		newRoot.children[1] = tmp;
		newRoot.m = 1;
		newRoot.c = 2;
		height++;
		root = newRoot;
	}
	
	/**
	 * Inserimento del record tramite ricorsione
	 * 
	 * @param v il nodo attuale
	 * @param k la chiave da inserire
	 * @param e il record da inserire
	 * @param ht l'altezza attuale
	 * 
	 * @return il nuovo nodo risultante dall'eventuale split (numero eccessivo di record nel nodo)
	 */
	public Node insert(Node v, String k, Object e, int ht) {
		int pos = 0;
		
		// Ricerca posizione
		while(pos < v.m && k.compareTo(v.pairs[pos].key) > 0) pos++;
		
		InfoBT tmp = new InfoBT(k,e);
		Node newNode = null;
		
		if(ht != 0)  {
			newNode = insert(v.children[pos], k, e, ht - 1); 				// Chiamata ricorsiva al figlio i | ritorna il nuovo nodo risultante dallo split
			if (newNode == null) return null;								// L'inserimento è riuscito
			
			// La coppia t da spostare nel padre
			tmp.key = v.children[pos].pairs[t-1].key;						  
			tmp.elem = v.children[pos].pairs[t-1].elem;   					
			v.children[pos].pairs[t-1] = null;
		}
		
		// Inserimento della coppia
		for (int j=v.m; j > pos; j--) v.pairs[j] = v.pairs[j-1]; 			 // Operazione di slide
		v.pairs[pos] = tmp;
		v.m++;
		
		// Inserimento del nuovo nodo come figlio, in caso di split
		if (newNode != null) {												
			for (int j=v.c; j > pos+1; j--) v.children[j] = v.children[j-1]; // Operazione di slide
			v.children[pos+1] = newNode;
			v.c++;
		} 
		
		// Se il nodo attuale ha più coppie del numero consentito, split
		if(v.m > 2*t-1) return split(v, ht);								// Ritorno del nuovo nodo generato
		return null; 														// L'inserimento è riuscito
	}
	
	/**
	 * Operazione di split del nodo
	 * 
	 * @param f il nodo sui cui effettuare lo split
	 * @param ht l'altezza del nodo f
	 * 
	 * @return il nuovo nodo risultante dallo split
	 */
	private Node split(Node f, int ht) {
		Node tmp = new Node();
		f.m = t-1;	// f   nodo di sinistra
		tmp.m = t;	// tmp nodo di destra
		
		// Redistribuzione delle coppie
		for (int j=0; j < tmp.m; j++) tmp.pairs[j] = f.pairs[t+j];
		
		// Se il nodo non è una foglia viene effettuata la redistribuzione dei figli 
		if (ht != 0) {
			tmp.c = t+1; 
			f.c = t;
			for (int j=0; j < tmp.c; j++) tmp.children[j] = f.children[t+j];
		}
		
		return tmp;
	}
	
	@Override
    public Object delete(String key) {
    	if (key != null) return delete(null, 0, root, key, height);
    	return null;
    }
    
    /**
     * Cancellazione del record tramite ricorsione
     * 
     * @param father il nodo padre del nodo attuale
     * @param childInd l'indice del nodo attuale tra i figli del padre
     * @param v il nodo attuale
     * @param k la chiave da eliminare
     * @param ht l'altezza attuale
     * 
     * @return il record eliminato se esistente, null altrimenti
     */
	public Object delete(Node father, int childInd, Node v, String k, int ht) {
		int pos = 0;
		Object deletedObj = null;
		
		// Ricerca posizione
		while(pos < v.m && k.compareTo(v.pairs[pos].key) > 0) pos++;
		
		if(pos < v.m && k.equals(v.pairs[pos].key)) {
			if (ht != 0) { 														 // Nodo Interno
				deletedObj = v.pairs[pos].elem;									 // Il record eliminato, per il return
				v.pairs[pos] = extractGreatest(v, pos, v.children[pos], ht - 1); // Ritorna il predecessore di v
				delete(v, pos, v.children[pos], v.pairs[pos].key, ht - 1);		 // Eliminazione del predecessore
			} else { 															 // Foglia
				deletedObj  = deleteFromLeaf(father, childInd, v, pos);
			}
		}
		
		// Chiamata ricorsiva
		if (ht != 0 && deletedObj==null) 
			deletedObj = delete(v, pos, v.children[pos], k, ht - 1);
		
		// Se il nodo attuale ha meno coppie del numero consentito, ribilanciamento
		if(v.m < t-1 && father != null)
			balance(father, childInd, v, t-2);
		
		return deletedObj;
	}
	
	/**
	 * Estrazione del predecessore
	 * 
	 * @param father il nodo padre del nodo attuale
	 * @param childInd l'indice del nodo attuale tra i figli del padre
	 * @param v il nodo attuale
	 * @param ht l'altezza attuale
	 * 
	 * @return il predecessore
	 */
	private InfoBT extractGreatest(Node father, int childInd, Node v, int ht) {
		if (ht != 0) return extractGreatest(v, v.c-1, v.children[v.c-1], ht - 1);		//v.c-1 equivale all'ultimo figlio (contiene la chiave più grande)
		return v.pairs[v.m-1];															//v.m-1 equivale all'ultima coppia (la chiave più grande)
	}
	
	/**
	 * Cancellazione da foglia
	 * 
	 * @param father il nodo padre del nodo attuale
	 * @param childInd l'indice del nodo attuale tra i figli del padre
	 * @param leaf il nodo foglia
	 * @param d l'indice della coppia da eliminare
	 * 
	 * @return il record eliminato
	 */
	private Object deleteFromLeaf(Node father, int childInd, Node leaf, int d) {
		Object deletedObj = leaf.pairs[d].elem;
		leaf.pairs[d] = null;
		
		if (leaf.m > t-1 || father == null) {								// Se l'eliminazione non compromette la foglia
			leaf.m--;														
			for(int i=d; i < leaf.m; i++) leaf.pairs[i] = leaf.pairs[i+1];	// Operazione di slide
		}
		else balance(father, childInd, leaf, d);							// Altrimenti ribilanciamento
		return deletedObj;
	}
	
	/**
	 * Ribilanciamento del BTree
	 * 
	 * @param father il nodo padre del nodo attuale
	 * @param childInd l'indice del nodo attuale tra i figli del padre
	 * @param v il nodo attuale da bilanciare
	 * @param d l'indice della coppia eliminata
	 */
	private void balance(Node father, int childInd, Node v, int d) {
		Node leftSibling  = null;
		Node rightSibling = null;
		
		// Acquisizione fratelli
		if(childInd > 0)			leftSibling  = father.children[childInd-1];
		if(childInd < father.c - 1)	rightSibling = father.children[childInd+1];
			
		// REDISTRIBUZIONE
		boolean redistribution = false;
			
		// Redistribuzione da sinistra
		if (leftSibling != null && leftSibling.m > t-1 ) {							
			for(int i=0; i < d; i++) v.pairs[i+1] = v.pairs[i];				// Operazione di slide
			v.pairs[0] = father.pairs[childInd-1];							// Coppia dal padre
			father.pairs[childInd-1] = leftSibling.pairs[leftSibling.m-1];	// Il padre riceve la coppia dal fratello di sinistra
			leftSibling.pairs[leftSibling.m-1] = null;
			leftSibling.m--;
			
			// Nodo interno (se v ha figli)
			if(v.c > 0) {
				for(int i=0; i < v.c; i++) v.children[i+1] = v.children[i];	// Operazione di slide sui figli
				v.c++;
				leftSibling.c--;
				v.children[0] = leftSibling.children[v.c];					// Il nodo v riceve il figlio dal fratello di sinistra (Rotazione)
				leftSibling.children[v.c] = null;
			}
			redistribution = true;
		}
			
		// Redistribuzione da destra
		if (rightSibling != null && rightSibling.m > t-1 ) {
			for(int i=v.m-1; i > d; i--) v.pairs[i-1] = v.pairs[i];		// Operazione di slide se d non è l'ultima coppia (v.m-1)
			if (v.m == 0) v.m++;										// Se t=2, in caso di precedente fusione, il nodo padre (v) può raggiungere quota 0 coppie
			v.pairs[v.m-1] = father.pairs[childInd];					// Coppia dal padre
			father.pairs[childInd] = rightSibling.pairs[0];				// Il padre riceve la coppia dal fratello di destra
			rightSibling.m--;
			for(int i=0; i < rightSibling.m; i++) 
				rightSibling.pairs[i] = rightSibling.pairs[i+1]; 		// Operazione di slide sul fratello
			
			// Nodo interno (se v ha figli)
			if(v.c > 0) {
				v.children[v.c] = rightSibling.children[0]; 			   // Il nodo v riceve il figlio dal fratello di destra (Rotazione)
				v.c++;	
				rightSibling.c--;
				for(int i=0; i < rightSibling.c; i++) 
					rightSibling.children[i] = rightSibling.children[i+1]; // Operazione di slide sui figli
			}
			redistribution = true;
		}
		
		if (redistribution)
			return;
		
		// FUSIONE
		boolean fusion = false;
		
		// Fusione a sinistra
		if (leftSibling != null) {										
			// Coppia dal padre (può causare sbilanciamenti)
			leftSibling.pairs[leftSibling.m] = father.pairs[childInd-1];
			leftSibling.m++;
			father.pairs[childInd-1] = null;
			int k = 0;			
			
			// Spostamento coppie
			for(int i=0; i < v.m-1; i++) {
				if(i==d) k++;												// Doppio indice per evitare un altro slide
				leftSibling.pairs[i+leftSibling.m] = v.pairs[k];			// Il fratello di sinistra acquisisce le coppie di v
				k++; 
			}
			int x = 1;
			if (v.m == 0) x--;
			leftSibling.m += v.m - x;	
			
			// Slide sul padre
			father.m--; father.c--;
			for(int i=childInd-1; i < father.m; i++) father.pairs[i] = father.pairs[i+1];
			for(int i=childInd; i < father.c; i++) father.children[i] = father.children[i+1];
			
			// Nodo interno (se v ha figli)
			if(v.c > 0) {
				for(int i=0; i < v.c; i++) 
					leftSibling.children[i+leftSibling.c] = v.children[i];	// Acquisizione figli
				leftSibling.c += v.c;
			}
			fusion = true;
				
		// Fusione a destra	
		} else if (rightSibling != null){
			if (v.m == 0) v.m++;
			for(int i=0; i < v.m; i++) 
				rightSibling.pairs[i+v.m] = rightSibling.pairs[i];	// Slide sul fratello di destra
			
			// Coppia dal padre
			rightSibling.pairs[v.m-1] = father.pairs[childInd]; 
			rightSibling.m++;

			// Spostamento coppie
			int k = 0;
			for(int i=0; i < v.m-1; i++) {
				if(i==d) k++;															
				rightSibling.pairs[i] = v.pairs[k]; // Il fratello di destra acquisisce le coppie di v
				k++;
			}
			rightSibling.m += v.m - 1;
			
			// Slide sul padre
			father.pairs[childInd] = null;
			father.m--; father.c--;
			for(int i=childInd; i < father.m; i++) father.pairs[i] = father.pairs[i+1];
			for(int i=childInd; i < father.c; i++) father.children[i] = father.children[i+1];
			
			// Nodo interno (se v ha figli)
			if(v.c > 0) {
				for(int i=0; i < rightSibling.c; i++) 
					rightSibling.children[rightSibling.c-1+v.c-i] = rightSibling.children[rightSibling.c-1-i];	// Slide sui figli
				for(int i=0; i < v.c; i++) rightSibling.children[i] = v.children[i];							// Acquisizione figli
				rightSibling.c += v.c;
			}
			fusion = true;
		}
			
		// Eliminazione della radice
		if (fusion && father==root && father.m==0) {
			// Può esistere un solo altro fratello (father.m era 1) 
			if(leftSibling != null)	root = leftSibling;
			if(rightSibling != null)root = rightSibling;
			height--;
		}
	}
    
	/**
	 * Metodo pubblico per la creazione di un array contenente i record del BTree
	 * 
	 * @return l'array di record
	 */
    public Object[] toArray() {
    	List<Object> ll = new LinkedList<>();
    	buildLL(ll, root);
    	if(ll.size() != 0) return ll.toArray();
    	else return null;
    }
    
    /**
     * Creazione di una lista contenente i record del BTree tramite ricorsione
     * 
     * @param ll la lista
     * @param v il nodo attuale
     */
    private void buildLL(List<Object> ll, Node v) {
    	if (v != null) {
    		int i;
    		for(i=0; i < v.m; i++) {
    			if(v.c > i) buildLL(ll, v.children[i]);
    			ll.add(v.pairs[i].elem);
    		}
    		
    		// L'ultimo figlio (nFigli = nCoppie + 1)
    		if(v.c > i) buildLL(ll, v.children[i]);
    	}
    }
}
