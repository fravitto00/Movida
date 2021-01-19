package perozzivittori;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		MovidaCore bona = new MovidaCore();
		File f = new File("C:\\Users\\UtenteHP\\Documents\\movida\\Movida\\src\\movida\\commons\\esempio-formato-dati.txt");
		//"C:\\Users\\Utente\\eclipse-workspace\\Movida\\src\\movida\\commons\\esempio-formato-dati.txt"
		bona.loadFromFile(f);
	}
}
