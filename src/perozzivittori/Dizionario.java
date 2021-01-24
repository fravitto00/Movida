package perozzivittori;

public interface Dizionario {
	public Object search(String key);

	public void insert(String key, Object e);

	public Object delete(String key);
}
