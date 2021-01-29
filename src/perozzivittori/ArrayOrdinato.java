package perozzivittori;

public class ArrayOrdinato{
	
	protected KeyElem[] A = new KeyElem[0];
	
	public static class KeyElem {
		public String key;
		public Object elem;
		
		public KeyElem(String k, Object e) {
			this.key = k;
			this.elem = e;
		}
	}
	public ArrayOrdinato(){}
	/*
	public ArrayOrdinato(Object[] Arr) {
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
	*/
	//*****Implementazione tramite Array statico****
	public void insert(String k, Object e) {
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
	
	public Object delete(String k) {
		Object deletedObj = null;
		if(this.A.length > 0 && k != null) {
			int i,j;
			for(i=0; i<A.length; i++) {
				if(k.equals(A[i].key)) {
					deletedObj = A[i].elem;
					for(j=i; j < A.length-1; j++)
						A[j]=A[j+1];
					break;
				}
			}
			KeyElem[] tmp = new KeyElem[A.length-1];
			System.arraycopy(A, 0, tmp, 0, tmp.length);
			A = tmp;
		}
		return deletedObj;
	}
	
	public Object search(String k) {
		if(this.A.length > 0 && k != null) {
			int l=0, u=A.length-1, m; //lower, upper, m
			while(l <= u) {
				m =(int) (l+u) / 2;
				if(k.equals(A[m].key)) return A[m].elem;
				if(k.compareTo(A[m].key) <= 0) 	u = m-1;
				else l = m+1;
			}
		}
		return null;
	}
	
	public int size() {
		return A.length;
	}
	
	public Object[] toArray() {
		Object[] O = new Object[A.length];
		for(int i=0; i<A.length; i++) {
			O[i] = A[i].elem;
		}
		return O;
	}
}
