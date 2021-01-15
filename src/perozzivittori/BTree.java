package perozzivittori;

public class BTree {

	protected class InfoBT {
		protected Object elem;
		protected Comparable key;
		protected Node next;

		protected InfoBT(Object e, Comparable k, Node next){
			elem = e; key = k; this.next = next;
		}
	}
	
	private static final int t = 4; // t >= 2

	private Node root;       // root of the B-tree
	private int height;      // height of the B-tree
	private int n;    
	
	private static final class Node {
        private int m;                             // number of children
        private InfoBT[] children = new InfoBT[t];   // the array of children

        // create a node with k children
        private Node(int k) {
            m = k;
        }
    }
	
	public BTree() {
        root = new Node(0);
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Comparable key;
        private final Object val;
        private Node next;     // helper field to iterate over array entries
        public Entry(Comparable key, Object val, Node next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }
    
    private static class bo{}
    
    
}
