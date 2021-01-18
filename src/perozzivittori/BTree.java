package perozzivittori;

public class BTree {
	
	private static final int t = 4; // t >= 2

	private Node root;       // root of the B-tree
	private int height;      // height of the B-tree
	private int n;    
	
	public BTree() {
        root = new Node();
        height = 0;
    }
	
	public Object search(Node v, Comparable k, int ht) {
		int i = 0;
		while(i < v.m && k.compareTo(v.keys[i].key) > 0) i++;
		if (i < v.m && k.compareTo(v.keys[i].key) == 0) return v.keys[i].elem;
		else {
			if(ht == 0) return null;
			else return search(v.children[i], k, ht-1);
		}
	}
	
	public void insert(Node v, Comparable k, Object e, int ht) {
		int i=0;
		while(i < v.m && k.compareTo(v.keys[i].key) > 0) i++;
		if(ht == 0)  {
			if(v.m < 2*t-1 ) {	//not full leaf
				for (int j=v.m; j > i; j--) v.keys[j] = v.keys[j-1]; // slide operation for empty space for insert in i 
				v.keys[i] = new InfoBT(k,e);
				v.m++;
			} else { //split
				
			}
		} else {
			insert(v.children[i], k, e, ht - 1);
		}
		
		for (int j=v.m; j > i; j--) v.keys[j] = v.keys[j-1]; // slide operation for empty space for insert in i
		v.keys[i] = new InfoBT(k,e);
		v.m++;
	}
	
	private InfoBT split(Node f) {
		Node tmp = new Node();
		tmp.father = f.father;
		f.m = t-1;
		for (int j=0; j < tmp.m; j++) {
			tmp.keys[j] = f.keys[j];
			tmp.children[j] = f.children[j];
		}
		for (int j=0; j < t; j++) f.keys[j] = f.keys[t+j];	
		return f.keys[t-1];
	}
		
	public boolean insert(Comparable k, Object e) {
		InfoBT couple = new InfoBT(k,e);
		int i=0;
		int ht = height;
		Node v = root;
		while (v!=null) {
			for (i=0; i < v.m ; i++) {
				if(k.compareTo(v.keys[i].key) < 0) break;
			}
			v = v.children[i];
		}
		return false;
	}
	
	public boolean delete() {return false;}
	
	protected class Node {
		protected Node father;
		protected int m;                             			// number of keys
		protected InfoBT[] keys = new InfoBT[2*t-1];			// array of key-value pairs
		protected Node[] children = new Node[2*t];   			// array of children

        // create a node
		protected Node() {m=0;}
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
