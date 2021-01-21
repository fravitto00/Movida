package perozzivittori;

public interface Dizionario {
	public Object search(Comparable key);

	public void insert(Comparable key, Object e);

	public void delete(Comparable key);
}
