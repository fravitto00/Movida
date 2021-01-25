package perozzivittori;

import java.util.LinkedList;
import java.util.List;

public class BTree implements Dizionario {
	
	private static final int t = 2; // t >= 2

	private Node root;	//root of the B-tree
	private int height;	//height of the B-tree 	
	
	public BTree() {
        root = new Node();
        height = 0;
    }
	
	@Override
	public Object search(String k) {
		return search(root, k, height);
	}
	
	public Object search(Node v, String k, int ht) {
		int pos = 0;
		while(pos < v.m && k.compareTo(v.pairs[pos].key) > 0) pos++;
		if (pos < v.m && k.equals(v.pairs[pos].key)) return v.pairs[pos].elem;
		else {
			if(ht == 0) return null;
			else return search(v.children[pos], k, ht-1);
		}
	}
	
	public void insert(String k, Object e) {
		Node tmp = insert(root, k, e, height);
		if (tmp == null) return;
		// Root split
		Node newRoot = new Node();
		newRoot.pairs[0] = root.pairs[t-1];
		newRoot.children[0] = root;
		newRoot.children[1] = tmp;
		newRoot.m = 1;
		newRoot.c = 2;
		height++;
		root = newRoot;
		System.out.println("New Root! Ht: " + height);
	}
	
	public Node insert(Node v, String k, Object e, int ht) {
		int pos = 0;
		while(pos < v.m && k.compareTo(v.pairs[pos].key) > 0) pos++; 				//research right position
		InfoBT tmp = new InfoBT(k,e);
		Node newNode = null;
		
		if(ht != 0)  {
			newNode = insert(v.children[pos], k, e, ht - 1); 				//recursive call to i-th child | returns new split node
			if (newNode == null) return null;								//the insert was successful
			tmp.key = v.children[pos].pairs[t-1].key;						//t-th leaf to the father //return f.pairs[t-1] => v.children[i].pairs[t-1] 
			tmp.elem = v.children[pos].pairs[t-1].elem;   					//tmp will be used to insert t-th leaf from below
			v.children[pos].pairs[t-1] = null;
		}
		
		//pair (and child) insertion 
		for (int j=v.m; j > pos; j--) v.pairs[j] = v.pairs[j-1]; 			//slide operation for empty space for insert of the pair in i
		v.pairs[pos] = tmp;													//pair insert
		v.m++;																//keys counter update
		if (newNode != null) {												//if the node was split, insert of the new node as a child
			for (int j=v.c; j > pos+1; j--) v.children[j] = v.children[j-1];	//slide operation
			v.children[pos+1] = newNode;										//newNode insert
			v.c++;															//children counter update
		} 
		
		if(v.m > 2*t-1) return split(v, ht);								//return newNode generated from the split
		return null; 														//pair inserted			 
	}
	
	private Node split(Node f, int ht) {
		Node tmp = new Node();
		f.m = t-1;	//f   left node
		tmp.m = t;	//tmp right node
		
		//pairs split
		for (int j=0; j < tmp.m; j++) tmp.pairs[j] = f.pairs[t+j];
		
		//children split
		if (ht != 0) {
			tmp.c = t+1; 
			f.c = t;
			for (int j=0; j < tmp.c; j++) tmp.children[j] = f.children[t+j];
		}
		
		return tmp;	//return new node
	}
	
    public Object delete(String k) {
    	return delete(null, 0, root, k, height);
    }
    
	public Object delete(Node father, int childInd, Node v, String k, int ht) {
		int pos = 0;
		Object tmp = null;
		while(pos < v.m && k.compareTo(v.pairs[pos].key) > 0) pos++; //research right position
		if(pos < v.m && k.equals(v.pairs[pos].key)) {
			if (ht != 0) { // intern node 
				v.pairs[pos] = extractGreatest(v, pos, v.children[pos], ht - 1); //return v predecessor
				delete(v, pos, v.children[pos], v.pairs[pos].key, ht - 1);
			} else { //leaf
				deleteFromLeaf(father, childInd, v, pos);
			}
			tmp = v;
		}
		
		if (ht != 0 && tmp==null) tmp = delete(v, pos, v.children[pos], k, ht - 1);
		
		//Ribilanciamento
		if(v.m < t-1 && father != null) {
			System.out.println("Nodo interno invalido");
			balance(father, childInd, v, t-2);
		}
		
		return tmp;
	}
	
	private InfoBT extractGreatest(Node father, int childInd, Node v, int ht) {
		
		if (ht != 0) return extractGreatest(v, v.c-1, v.children[v.c-1], ht - 1);		//v.c-1 equals to the last children (the greatest)
		//System.out.println(v.pairs[v.m-1].key);
		return v.pairs[v.m-1];															//v.m-1 equals to the last pair (the greatest)
	}
	
	private void deleteFromLeaf(Node father, int childInd, Node leaf, int d) {
		leaf.pairs[d] = null;
		if (leaf.m > t-1 || father == null) {														
			leaf.m--;														//pairs counter update
			for(int i=d; i < leaf.m; i++) leaf.pairs[i] = leaf.pairs[i+1];	//slide operation 
			printTree();
		}
		else balance(father, childInd, leaf, d);
	}
	
	private void balance(Node father, int childInd, Node v, int d) {
		Node leftSibling  = null;
		Node rightSibling = null;
		if(childInd > 0)			leftSibling  = father.children[childInd-1];
		if(childInd < father.c - 1)	rightSibling = father.children[childInd+1];
			
		//REDISTRIBUTION
		boolean redistribution = false;
			
		//left redistribution
		if (leftSibling != null && leftSibling.m > t-1 ) {							//redistribution from left sibling
			for(int i=0; i < d; i++) v.pairs[i+1] = v.pairs[i];						//slide
			v.pairs[0] = father.pairs[childInd-1];								//pairs swap with left sibling
			father.pairs[childInd-1] = leftSibling.pairs[leftSibling.m-1];			//pairs swap
			leftSibling.pairs[leftSibling.m-1] = null;
			leftSibling.m--;
			String s= "";
			if(v.c > 0) {															//internal node (with children)
				for(int i=0; i < v.c; i++) v.children[i+1] = v.children[i];			//slide children
				v.c++;
				leftSibling.c--;
				v.children[0] = leftSibling.children[v.c];
				leftSibling.children[v.c] = null;
				s += "Nodo Interno - ";
			}
			redistribution = true;
			System.out.println(s + "LR");
		}
			
		//right redistribution
		if (rightSibling != null && rightSibling.m > t-1 ) {						//redistribution from right sibling
			for(int i=v.m-1; i > d; i--) v.pairs[i-1] = v.pairs[i];					//slide if d isn't last pair
			if (v.m == 0) v.m++;														//pairs swap with right sibling
			v.pairs[v.m-1] = father.pairs[childInd];								// v is invalid in case of t=2
			father.pairs[childInd] = rightSibling.pairs[0];							//pairs swap
			rightSibling.m--;
			for(int i=0; i < rightSibling.m; i++) rightSibling.pairs[i] = rightSibling.pairs[i+1];	//slide operation for sibling
			String s = "";
			if(v.c > 0) {																			//internal node (with children)
				v.children[v.c] = rightSibling.children[0]; 													//sottoalbero passing with rotation
				v.c++;	
				rightSibling.c--;
				for(int i=0; i < rightSibling.c; i++) rightSibling.children[i] = rightSibling.children[i+1];	//slide children
				s += "Nodo Interno - ";
			}
			redistribution = true;
			System.out.println(s + "RR");
		}
		if (redistribution) {
			printTree(root, height);
			return;
		}
		
		//FUSION
		boolean fusion = false;
		
		//left fusion
		if (leftSibling != null) {																			
			leftSibling.pairs[leftSibling.m] = father.pairs[childInd-1];	//pair from father
			leftSibling.m++;
			father.pairs[childInd-1] = null;
			int k = 0;			
			for(int i=0; i < v.m-1; i++) {
				if(i==d) k++;															//double index to avoid making another slide operation on v pairs
				leftSibling.pairs[i+leftSibling.m] = v.pairs[k];						//pairs transfer to left sibling
				k++; 
			}
			int x = 1;
			if (v.m == 0) x--;
			leftSibling.m += v.m - x;	
			
			//slide on father
			father.m--; father.c--;
			for(int i=childInd-1; i < father.m; i++) father.pairs[i] = father.pairs[i+1];
			for(int i=childInd; i < father.c; i++) father.children[i] = father.children[i+1];
			
			String s = "";
			if(v.c > 0) {																			//internal node (with children)
				for(int i=0; i < v.c; i++) leftSibling.children[i+leftSibling.c] = v.children[i];	//acquisition children
				leftSibling.c += v.c;
				s += "Nodo Interno - ";
			}
			fusion = true;
			System.out.println(s + "LF");
				
		//right fusion	
		} else if (rightSibling != null){
			if (v.m == 0) v.m++;																//internal node with 0 (t-2) keys
			for(int i=0; i < v.m; i++) rightSibling.pairs[i+v.m] = rightSibling.pairs[i];	//slide operation on sibling (right)
			
			//pair from father
			rightSibling.pairs[v.m-1] = father.pairs[childInd]; 
			rightSibling.m++;

			//pairs transfer to right sibling
			int k = 0;
			for(int i=0; i < v.m-1; i++) {
				if(i==d) k++;															
				rightSibling.pairs[i] = v.pairs[k];
				k++;
			}
			rightSibling.m += v.m - 1;
			
			//slide on father
			father.pairs[childInd] = null;
			father.m--; father.c--;
			for(int i=childInd; i < father.m; i++) father.pairs[i] = father.pairs[i+1];
			for(int i=childInd; i < father.c; i++) father.children[i] = father.children[i+1];
			String s = "";
			if(v.c > 0) {																						//internal node (with children)
				for(int i=0; i < rightSibling.c; i++) rightSibling.children[rightSibling.c-1+v.c-i] = rightSibling.children[rightSibling.c-1-i];	//slide children
				for(int i=0; i < v.c; i++) rightSibling.children[i] = v.children[i];							//acquisition children
				rightSibling.c += v.c;
				s += "Nodo Interno - ";
				System.out.println("v.child: " + rightSibling.children[1].pairs[0].key);
			}
			fusion = true;
			System.out.println(s + "RF");
		}
			
		//root vanishes
		if (fusion && father==root && father.m==0) {
			if(leftSibling != null)	root = leftSibling;
			if(rightSibling != null)root = rightSibling;
			height--;
			System.out.println("New Ht: " + height);
		}
			
		//probably useless
		if(!redistribution && !fusion) {
			v.m--;
			System.out.println("Inutile");
		}
		printTree(root, height);
	}
    
    public Object[] toArray() {
    	List<Object> ll = new LinkedList<>();
    	buildLL(ll, root);
    	if(ll.size() != 0) return ll.toArray();
    	else return null;
    }
    
    private void buildLL(List<Object> ll, Node v) {
    	if (v != null) {
    		int i;
    		for(i=0; i < v.m; i++) {
    			if(v.c > i) buildLL(ll, v.children[i]);
    			ll.add(v.pairs[i].elem);
    		}
    		
    		//last child
    		if(v.c > i) buildLL(ll, v.children[i]);
    	}
    }
    
    public void printTree() {
    	printTree(root, height);
    }
    
    private void printTree(Node v, int ht) {
    	if (v != null) {
    		String s = "";
    		if (v==root) s = ", root";
    		int i;
    		for(i=0; i < v.m; i++) {
    			if(v.c > i) printTree(v.children[i], ht - 1);
    			System.out.println(v.pairs[i].key + " ad " + ht + s);
    		}
    		//last child
    		if(v.c > i) printTree(v.children[i], ht - 1);
    	}
    	else System.out.println("do vai");
    }
   
	private class Node {
		int m;                             			// number of pairs
		int c;										// number of children
		InfoBT[] pairs = new InfoBT[2*t];				// array of key-value pairs (plus one for operations)
		Node[] children = new Node[2*t+1];   			// array of children

        // create a node
		private Node() {m=0;}
    }
	
	private class InfoBT{
		Object elem;
		String key;

		private InfoBT(String k, Object e){
			key = k;
			elem = e;
		}
	}
}
