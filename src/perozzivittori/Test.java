package perozzivittori;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		MovidaCore bona = new MovidaCore();
		File f = new File("C:\\Users\\Utente\\eclipse-workspace\\movida\\commons\\esempio-formato-dati.txt");
		bona.loadFromFile(f);
	}
}
