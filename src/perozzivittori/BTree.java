package perozzivittori;

public class BTree<K extends Comparable<K>> {
	
	private static final int t = 2; // t >= 2

	private Node root;       // root of the B-tree
	private int height;      // height of the B-tree 
	
	public BTree() {
        root = new Node();
        height = 0;
    }
	
	public Object search(K k) {
		return search(root, k, height);
	}
	
	public Object search(Node v, K k, int ht) {
		int pos = 0;
		while(pos < v.m && greater(k,v.pairs[pos].key)) pos++;
		if (pos < v.m && eq(k,v.pairs[pos].key)) return v.pairs[pos].elem;
		else {
			if(ht == 0) return null;
			else return search(v.children[pos], k, ht-1);
		}
	}
	
	public void insert(K k, Object e) {
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
	}
	
	public Node insert(Node v, K k, Object e, int ht) {
		int pos = 0;
		while(pos < v.m && greater(k,v.pairs[pos].key)) pos++; 				//research right position
		InfoBT tmp = new InfoBT(k,e);
		Node newNode = null;
		
		if(ht != 0)  {
			newNode = insert(v.children[pos], k, e, ht - 1); 				//recursive call to i-th child
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
			for (int j=v.c; j > pos; j--) v.children[j] = v.children[j-1];	//slide operation
			v.children[pos] = newNode;										//newNode insert
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
	
    public void delete(K k) {
    	delete(null, 0, root, k, height);
    }
    
	public boolean delete(Node father, int childInd, Node v, K k, int ht) {
		int pos = 0;
		while(pos < v.m && greater(k,v.pairs[pos].key)) pos++; //research right position
		if(pos < v.m && eq(k,v.pairs[pos].key)) {
			if (ht != 0) { // intern node 
				v.pairs[pos] = extractGreatest(v, pos, v.children[pos], ht); //return v predecessor
			} else { //leaf
				deleteFromLeaf(father, childInd, v, pos);
			}
		} else {
			if (ht != 0) return delete(v, pos, v.children[pos], k, ht - 1);
			else return false;
		}
		return true;
	}
	
	private InfoBT extractGreatest(Node father, int childInd, Node v, int ht) {
		if (ht != 0) return extractGreatest(v, v.c-1, v.children[v.c-1], ht - 1);		//v.c-1 equals to the last children (the greatest)
		InfoBT tmp = v.pairs[v.m-1];												//v.m-1 equals to the last pair (the greatest)
		deleteFromLeaf(father, childInd, v, v.m-1);
		return tmp;
	}
	
	private void deleteFromLeaf(Node father, int childInd, Node leaf, int d) {
		leaf.pairs[d] = null;
		//normal delete
		if (leaf.m > t-1 || father == null) {														
			leaf.m--;														//pairs counter update
			for(int i=d; i < leaf.m; i++) leaf.pairs[i] = leaf.pairs[i+1];	//slide operation 
		} else {											//avoiding NullPointerException
			Node leftSibling  = null;
			Node rightSibling = null;
			if(childInd != 0)			 leftSibling  = father.children[childInd-1];
			if(childInd != father.c - 1) rightSibling = father.children[childInd+1];
			
			//redistribution
			boolean redistribution = false;
			
			//left redistribution
			if (leftSibling != null && leftSibling.m > t-1 ) {							//redistribution from left sibling
				for(int i=0; i < d; i++) leaf.pairs[i+1] = leaf.pairs[i];				//slide
				leaf.pairs[0] = father.pairs[childInd-1];								//pairs swap
				father.pairs[childInd-1] = leftSibling.pairs[leftSibling.m-1];			//pairs swap
				leftSibling.pairs[leftSibling.m-1] = null;
				leftSibling.m--;
				redistribution = true;
			}
			
			//right redistribution
			if (rightSibling != null && rightSibling.m > t-1 ) {						//redistribution from right sibling
				for(int i=leaf.m-1; i > d; i++) leaf.pairs[i-1] = leaf.pairs[i];		//slide
				leaf.pairs[leaf.m-1] = father.pairs[childInd];							//pairs swap
				father.pairs[childInd] = rightSibling.pairs[0];							//pairs swap
				rightSibling.m--;
				for(int i=0; i < rightSibling.m; i++) leaf.pairs[i] = leaf.pairs[i+1];	//slide operation for sibling
				redistribution = true;					
			}
			if (redistribution) return;
			
			// fusion
			// left fusion
			if (leftSibling != null) {																			
				leftSibling.pairs[leftSibling.m] = father.pairs[childInd]; leftSibling.m++;	//pair from father
				father.pairs[childInd] = null;
				int k = 0;			
				for(int i=0; i < leaf.m; i++) {
					if(i==d) k++;															//double index to avoid making another slide operation on leaf pairs
					leftSibling.pairs[i+leftSibling.m] = leaf.pairs[k];						//pairs transfer to left sibling
					k++; 
				}
				leftSibling.m += leaf.m - 1;																			
				for(int i=childInd-1; i < father.m; i++) father.pairs[i] = father.pairs[i+1];
				for(int i=childInd-1; i < father.c; i++) father.children[i] = father.children[i+1];
				father.m--; father.c--;
				
			//right fusion	
			} else if (rightSibling != null){
				for(int i=0; i < leaf.m-1; i++) rightSibling.pairs[i+leaf.m] = rightSibling.pairs[i];	//slide operation on sibling (right)
				rightSibling.pairs[leaf.m-1] = father.pairs[childInd-1]; rightSibling.m++;
				father.pairs[childInd-1] = null;
				int k = 0;
				for(int i=0; i < leaf.m; i++) {
					if(i==d) k++;															
					rightSibling.pairs[i] = leaf.pairs[k];												//pairs transfer to right sibling
					k++;
				}
				rightSibling.m += leaf.m - 1;
				for(int i=childInd-1; i < father.m; i++) father.pairs[i] = father.pairs[i+1];
				for(int i=childInd-1; i < father.c; i++) father.children[i] = father.children[i+1];
				father.m--; father.c--;
			}
		}
	}
	
	// comparison functions - make Comparable instead of K to avoid casts
    private boolean greater(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) > 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }    
    	
	protected static class Node {
		protected int m;                             			// number of pairs
		protected int c;										// number of children
		protected InfoBT[] pairs = new InfoBT[2*t];				// array of key-value pairs (plus one for operations)
		protected Node[] children = new Node[2*t];   			// array of children

        // create a node
		protected Node() {m=0;}
    }
	
	protected static class InfoBT{
		protected Object elem;
		protected Comparable key;

		protected InfoBT(Comparable k, Object e){
			key = k;
			elem = e;
		}
	}
}
