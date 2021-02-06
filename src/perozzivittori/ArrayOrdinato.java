package perozzivittori;

public class ArrayOrdinato{
	
	protected KeyElem[] A ;
	
	public static class KeyElem {
		public String key;
		public Object elem;
		
		public KeyElem(String k, Object e) {
			this.key = k;
			this.elem = e;
		}
	}
	public ArrayOrdinato(){this.A= new KeyElem[0];}
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
	/**
	 * La prima operazione è aumentare di una unità la dimensione dell'array
	 * Si inserisce l'oggetto coppia (chiave, elemento) nella giusta posizione così da mantenere l'ordine non decrescente dell'array
	 * Individuata la posizione tutti i successivi subiscono lo shift verso destra cossicchè si liberi la posizione scelta,
	 * l'oggetto è quindi inserito all'indice individuato
	 * 
	 * @param k Stringa che identifica l'oggetto
	 * @param e Object che trasporta l'informazione della coppia
	 */
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
	/**
	 * Elimina l'oggetto coppia dall'array in base alla chiave, avviene una ricerca sequenziale a partire dall'inizio
	 * in caso positivo, è eseguito lo shift verso sinistra di tutti gli elementi successivi ad esso così da eliminare loggetto
	 * infine la dimensione dell'array è ristretto di una unità solo in caso di eliminazione
	 * 
	 * @param k stringa che identifica l'oggetto da eliminare
	 * @return la parte elemento Object dell'oggetto eliminato, altrimenti null oggetto non trovato
	 */
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
			if(deletedObj != null) {
				KeyElem[] tmp = new KeyElem[A.length-1];
				System.arraycopy(A, 0, tmp, 0, tmp.length);
				A = tmp;
			}
		}
		return deletedObj;
	}
	/**
	 * Ricerca in base alla chiave stringa. La ricerca è dicotomica: il campo si dimezza ad ogni iterazione
	 * si cerca l'oggetto nel centro del range di ricerca, se la chiave dell'oggetto da ricercare è inferiore all'oggetto nel 
	 * centro, si sceglie come nuovo range la parte sinistra, altrimenti si cerca a destra del centro
	 * 
	 * @param k stringa che identifica l'oggetto
	 * @return l'elemento se trovato, null altrimenti
	 */
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
	/**
	 * A partire dagli oggetti coppia, viene creato un array composto dalla sola parte elemento di tutti gli oggetti
	 * contenuti nell'array.
	 * 
	 * @return array di Object 
	 */
	public Object[] toArray() {
		Object[] O = new Object[A.length];
		for(int i=0; i<A.length; i++) {
			O[i] = A[i].elem;
		}
		return O;
	}
}
