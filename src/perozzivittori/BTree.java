package perozzivittori;

public class BTree<Key extends Comparable<Key>> {
	
	private static final int t = 4; // t >= 2

	private Node root;       // root of the B-tree
	private int height;      // height of the B-tree 
	
	public BTree() {
        root = new Node();
        height = 0;
    }
	
	public Object search(Node v, Key k, int ht) {
		int i = 0;
		while(i < v.m && greater(k,v.pairs[i].key)) i++;
		if (i < v.m && eq(k,v.pairs[i].key)) return v.pairs[i].elem;
		else {
			if(ht == 0) return null;
			else return search(v.children[i], k, ht-1);
		}
	}
	
	public void insert(Key k, Object e) {
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
		root.children[0].father = root;
		root.children[1].father = root;
	}
	
	public Node insert(Node v, Key k, Object e, int ht) {
		int i=0;
		while(i < v.m && greater(k,v.pairs[i].key)) i++; //research right position
		InfoBT tmp = new InfoBT(k,e);
		Node newNode = null;
		if(ht != 0)  {
			newNode = insert(v.children[i], k, e, ht - 1); //recursive call to i-th child
			if (newNode == null) return null;				//the insert was successful
			tmp.key = v.children[i].pairs[t-1].key;		//t-th leaf to the father //return f.pairs[t-1] => v.children[i].pairs[t-1] 
			tmp.elem = v.children[i].pairs[t-1].elem;   //tmp will be used to insert t-th leaf from below
			v.children[i].pairs[t-1] = null;
		}
		
		if(v.m < 2*t-1) {	//if not full node
			for (int j=v.m; j > i; j--) v.pairs[j] = v.pairs[j-1]; // slide operation for empty space for insert of the pair in i
			v.pairs[i] = tmp;	//pair insert
			v.m++;				//keys counter update
			if (newNode != null) {	// if the node was split, insert of the new node as a child
				for (int j=v.c; j > i; j--) v.children[j] = v.children[j-1]; // slide operation
				v.children[i] = newNode;	//newNode insert
				v.c++;			//children counter update
			}
		} else { //split
			return split(v, ht);	//return newNode generated from the split
		}
		return null; // pair inserted 
	}
	
	private Node split(Node f, int ht) {
		Node tmp = new Node();
		tmp.father = f.father;	//same father
		f.m = t-1;				//f left node
		tmp.m = t;				//tmp right node
		if (ht != 0) {			//children division (DA RIVEDERE)
			tmp.c = f.c/2; 
			f.c = f.c/2 - 1;		
		}
		for (int j=0; j < tmp.m; j++) {		//split operation (DA RIVEDERE)
			tmp.pairs[j] = f.pairs[t+j];	
			if (ht != 0) tmp.children[j] = f.children[t+j];
		}
		//for (int j=0; j < t; j++) f.pairs[j] = f.pairs[t+j];	
		return tmp;		//return new node
	}
	
    public void delete(Key k) {
    	delete(null, 0, root, k, height);
    }
    
	public boolean delete(Node father, int childInd, Node v, Key k, int ht) {
		int i=0;
		while(i < v.m && greater(k,v.pairs[i].key)) i++; //research right position
		if(i < v.m && eq(k,v.pairs[i].key)) {
			if (ht != 0) { // intern node 
				v.pairs[i] = extractGreatest(v, i, v.children[i], ht); //return v predecessor
			} else { //leaf
				deleteFromLeaf(father, childInd, v, i);
			}
		} else {
			if (ht != 0) return delete(v, i, v.children[i], k, ht - 1);
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
		if (leaf.m > t-1) {													//normal delete	
			leaf.m--;														//pairs counter update
			for(int i=d; i < leaf.m; i++) leaf.pairs[i] = leaf.pairs[i+1];	//slide operation 
		} else {
			Node leftSibling  = null;
			Node rightSibling = null;
			if(childInd != 0)			 leftSibling  = father.children[childInd-1];
			if(childInd != father.c - 1) rightSibling = father.children[childInd+1];
			boolean redistribution = false;
			if (leftSibling != null && leftSibling.m > t-1 ) {							//redistribution from left sibling
				for(int i=0; i < d; i++) leaf.pairs[i+1] = leaf.pairs[i];				//slide
				leaf.pairs[0] = father.pairs[childInd-1];								//pairs swap
				father.pairs[childInd-1] = leftSibling.pairs[leftSibling.m-1];			//pairs swap
				leftSibling.pairs[leftSibling.m-1] = null;
				leftSibling.m--;
				redistribution = true;
			} 
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
			if (leftSibling != null) {																			//left fusion
				leftSibling.pairs[leftSibling.m] = father.pairs[childInd]; leftSibling.m++; 					//pair from father
				father.pairs[childInd] = null;
				int k = 0;			
				for(int i=0; i < leaf.m; i++) {
					if(i==d) k++;											//double index to avoid making another slide operation on leaf pairs
					leftSibling.pairs[i+leftSibling.m] = leaf.pairs[k];		//pairs transfer to left sibling
					k++; 
				}
				leftSibling.m += leaf.m - 1;																			
				for(int i=childInd; i < father.m; i++) {														//slide operation on the father
					father.pairs[i] = father.pairs[i+1];
					father.children[i] = father.children[i+1];
				}
				father.m--; father.c--;
			} else if (rightSibling != null){
				for(int i=0; i < leaf.m-1; i++) rightSibling.pairs[i+leaf.m] = rightSibling.pairs[i];			//slide operation on sibling (right)
				rightSibling.pairs[leaf.m-1] = father.pairs[childInd-1]; rightSibling.m++;
				father.pairs[childInd-1] = null;
				int k = 0;
				for(int i=0; i < leaf.m; i++) {
					if(i==d) k++;											//double index to avoid making another slide operation on leaf pairs
					rightSibling.pairs[i] = leaf.pairs[k];					//pairs transfer to right sibling
					k++;
				}
				rightSibling.m += leaf.m - 1;
				for(int i=childInd-1; i < father.m; i++) {
					father.pairs[i] = father.pairs[i+1];
					father.children[i] = father.children[i+1];
				}
				father.m--; father.c--;
			}
		}
	}
	
	// comparison functions - make Comparable instead of Key to avoid casts
    private boolean greater(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) > 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }
    	
	protected static class Node {
		protected Node father;
		protected int m;                             			// number of pairs
		protected int c;										// number of children
		protected InfoBT[] pairs = new InfoBT[2*t-1];			// array of key-value pairs
		protected Node[] children = new Node[2*t];   			// array of children

        // create a node
		protected Node() {m=0; father=null;}
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
