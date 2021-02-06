package perozzivittori;

public interface Dizionario {
	/**
	 * Ricerca del record identificato dalla chiave key
	 * 
	 * @param key titolo/nome da ricercare
	 * 
	 * @return record con chiave k se esistente, null altrimenti
	 */
	public Object search(String key);
	
	/**
	 * Inserimento del record
	 * 
	 * @param key il titolo/nome
	 * @param e il record
	 */
	public void insert(String key, Object e);
	
	/**
	 * Cancellazione del record identificato dalla chiave key
	 * 
	 * @param key la chiave da eliminare
	 * 
	 * @return il record eliminato se esistente, null altrimenti
	 */
	public Object delete(String key);
}
