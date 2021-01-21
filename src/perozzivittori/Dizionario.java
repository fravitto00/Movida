package perozzivittori;

public interface Dizionario<K extends Comparable<K>> {
	public Object search(K key);

	public void insert(K key, Object e);

	public void delete(K key);
}
