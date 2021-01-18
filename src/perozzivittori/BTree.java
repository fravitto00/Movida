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
			else return search(v.children[i],k,ht-1);
		}
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
