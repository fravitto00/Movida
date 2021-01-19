package perozzivittori;

public class ArrayOrdinato implements Comparable{
	
	protected KeyElem[] A = new KeyElem[0];
	//protected ArrayList<KeyElem> A = new ArrayList<KeyElem>();
	
	public static class KeyElem {
		public Comparable key;
		public Object elem;
		
		public KeyElem(Comparable k, Object e) {
			this.key = k;
			this.elem = e;
		}
	}
	public ArrayOrdinato(){}
	
	public ArrayOrdinato(Object[] Arr) {
		//for(KeyElem a: Arr)
		//	this.A.add(a);
		this.A = (KeyElem[]) Arr;
		if(Arr != null && !SortedCheck())
			throw new RuntimeException("Array NON ordinato");
	}
	private boolean SortedCheck() {
		int i;
		for(i=0; i<A.length-1; i++) {
			if(A[i].key.compareTo(A[i+1].key) > 0)
				return false;
		}
		return true;
	}
	
	//*****Implementazione tramite Array statico****
	public void insert(Comparable k, Object e) {
		int i,j;
		KeyElem[] tmp = new KeyElem[A.length+1];
		System.arraycopy(A, 0, tmp, 0, A.length);
		A = tmp;
		for( i=0; i < A.length-1; i++) 
			if(k.compareTo(A[i].key) <= 0) break;
		for( j = A.length-1; j > i; j--) 
			A[j] = A[j-1];
		A[i] = new KeyElem(k, e);	
	}
	
	public void delete(Comparable k) {
		int i,j;
		for(i=0; i<A.length; i++) {
			if(k.equals(A[i].key)) {
				for(j=i; j < A.length-1; j++)
					A[j]=A[j+1];
				break;
			}
		}
		if(i == A.length) throw new RuntimeException("Occorrenza non trovata");
		KeyElem[] tmp = new KeyElem[A.length-1];
		System.arraycopy(A, 0, tmp, 0, tmp.length);
		A = tmp;
	}
	
	public Object search(Comparable k) {
		int l=0, u=A.length-1, m; //lower, upper, m
		while(l <= u) {
			m =(int) (l+u) / 2;
			if(k.equals(A[m].key)) return A[m].elem;
			if(k.compareTo(A[m].key) <= 0) 	u = m-1;
			else l = m+1;
		}
		return null;
	}
	
	@Override
	public int compareTo(Object o) {
		ArrayOrdinato a = (ArrayOrdinato) o;
		return this.compareTo(a);
	}
	
	/*
	 * ***Implementazione tramite ArrayList*****
	public void insert(Comparable k, Object e) {
		int i;
		for( i=0; i < A.size(); i++) 
			if(k.compareTo(A.get(i).key) <= 0) break;
		A.add(i, new KeyElem(k, e));
	}
	
	public void delete(Comparable k) {
		if(!A.remove(k))
			throw new RuntimeException();
	}
	
	public Object search(Comparable k) {
		int l=0, u=A.size()-1, m; //lower, upper, m
		while(l <= u) {
			m =(int) (l+u) / 2;
			if(k.equals(A.get(m).key)) return A.get(m).elem;
			if(k.compareTo(A.get(m).key) <= 0) 	u = m-1;
			else l = m+1;
		}
		return null;
	}
	*/
}
