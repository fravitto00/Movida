package perozzivittori;

public class BTree {
	
	private static final int t = 4; // t >= 2

	private Node root;       // root of the B-tree
	private int height;      // height of the B-tree 
	
	public BTree() {
        root = new Node();
        height = 0;
    }
	
	public Object search(Node v, Comparable k, int ht) {
		int i = 0;
		while(i < v.m && k.compareTo(v.pairs[i].key) > 0) i++;
		if (i < v.m && k.compareTo(v.pairs[i].key) == 0) return v.pairs[i].elem;
		else {
			if(ht == 0) return null;
			else return search(v.children[i], k, ht-1);
		}
	}
	
	public void insert(Comparable k, Object e) {
		Node tmp = insert(root, k, e, height);
		if (tmp == null) return;
		// Root split
		Node newRoot = new Node();
		newRoot.pairs[0] = root.pairs[t-1];
		newRoot.children[0] = root;
		newRoot.children[1] = tmp;
		newRoot.m = 2;
		height++;
		root = newRoot;
		root.children[0].father = root;
		root.children[1].father = root;
	}
	
	public Node insert(Node v, Comparable k, Object e, int ht) {
		int i=0;
		while(i < v.m && k.compareTo(v.pairs[i].key) > 0) i++; //research right position
		InfoBT tmp = new InfoBT(k,e);
		Node newNode = null;
		if(ht != 0)  {
			newNode = insert(v.children[i], k, e, ht - 1); //recursive call to i-th child
			if (newNode == null) return null;				//the insert was successful
			tmp.key = v.children[i].pairs[t-1].key;		//t-th leaf to the father //return f.pairs[t-1] => v.children[i].pairs[t-1] 
			tmp.elem = v.children[i].pairs[t-1].elem;   //tmp will be used to insert t-th leaf from below
		}
		
		if(v.m < 2*t-1) {	//if not full node
			for (int j=v.m; j > i; j--) v.pairs[j] = v.pairs[j-1]; // slide operation for empty space for insert of the pair in i
			v.pairs[i] = tmp;	//pair insert
			v.m++;				//keys counter update
			if (newNode != null) {	// if the node was split, insert of the new node as a child
				for (int j=v.m; j > i; j--) v.children[j] = v.children[j-1]; // slide operation
				v.children[i] = newNode;	//newNode insert
			}
		} else { //split
			return split(v);	//return newNode generated from the split
		}
		return null; // pair inserted 
	}
	
	private Node split(Node f) {
		Node tmp = new Node();
		tmp.father = f.father;	//same father
		f.m = t-1;				//f left node
		tmp.m = t;				//tmp right node
		for (int j=0; j < tmp.m; j++) {		//split operation
			tmp.pairs[j] = f.pairs[t+j];	
			tmp.children[j] = f.children[t+j];
		}
		//for (int j=0; j < t; j++) f.pairs[j] = f.pairs[t+j];	
		return tmp;		//return new node
	}
	
	public boolean delete() {return false;}
	
	protected class Node {
		protected Node father;
		protected int m;                             			// number of pairs
		protected InfoBT[] pairs = new InfoBT[2*t-1];			// array of key-value pairs
		protected Node[] children = new Node[2*t];   			// array of children

        // create a node
		protected Node() {m=0; father=null;}
    }
	
	protected class InfoBT {
		protected Object elem;
		protected Comparable key;

		protected InfoBT(Comparable k, Object e){
			key = k;
			elem = e;
		}
	}
}
