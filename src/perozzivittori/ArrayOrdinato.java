package perozzivittori;

public class ArrayOrdinato implements Dizionario{
	
	protected KeyElem[] A ;
	/**
	 * classe che permette di memorizzare nell'array A, attributo di ArrayOrdinato un oggetto coppia (chiave, elemento)	
	 */
	public static class KeyElem {
		public String key;
		public Object elem;
		
		public KeyElem(String k, Object e) {
			this.key = k;
			this.elem = e;
		}
	}
	public ArrayOrdinato(){this.A= new KeyElem[0];}
	/* INSERT
	 * La prima operazione è aumentare di una unità la dimensione dell'array
	 * Si inserisce l'oggetto coppia (chiave, elemento) nella giusta posizione così da mantenere l'ordine non decrescente dell'array
	 * Individuata la posizione tutti i successivi subiscono lo shift verso destra cossicchè si liberi la posizione scelta,
	 * l'oggetto è quindi inserito all'indice individuato
	 */
	@Override
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
	/* DELETE
	 * Elimina l'oggetto coppia dall'array in base alla chiave, avviene una ricerca sequenziale a partire dall'inizio
	 * in caso positivo, è eseguito lo shift verso sinistra di tutti gli elementi successivi ad esso così da eliminare loggetto
	 * infine la dimensione dell'array è ristretto di una unità solo in caso di eliminazione
	 */
	@Override
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
	/* SEARCH
	 * Ricerca in base alla chiave stringa. La ricerca è dicotomica: il campo si dimezza ad ogni iterazione
	 * si cerca l'oggetto nel centro del range di ricerca, se la chiave dell'oggetto da ricercare è inferiore all'oggetto nel 
	 * centro, si sceglie come nuovo range la parte sinistra, altrimenti si cerca a destra del centro
	 */
	@Override
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
	 * Ritorna array contenente tutti gli elementi memorizzati nella struttura
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
