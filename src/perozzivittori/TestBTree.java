package perozzivittori;

public class TestBTree {
	
	public static void main(String[] args) {
		BTree btree = new BTree();
		for(int i=0;i<15;i++) {
			System.out.println("Insert " + i);
			btree.insert(""+i, i);
			System.out.println("Done");
		}

		for(int i=0;i<14;i++) {
			System.out.println("Delete " + i + ", " + btree.delete(""+i));
			System.out.println("Done");
		}

		Object[] bo = btree.toArray();
		//System.out.println(bo==null);
		for(Object k: bo) System.out.println(k);
		//btree.printTree();
	}

}
