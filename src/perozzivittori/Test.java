package perozzivittori;

import java.io.File;
import movida.commons.*;

public class Test {

	public static void main(String[] args) {
		MovidaCore bona = new MovidaCore(SortingAlgorithm.SelectionSort, MapImplementation.BTree);
		File f = new File("C:\\Users\\Utente\\eclipse-workspace\\Movida\\src\\movida\\commons\\esempio-formato-dati.txt");
		//"C:\\Users\\Utente\\eclipse-workspace\\Movida\\src\\movida\\commons\\esempio-formato-dati.txt"
		bona.loadFromFile(f);
		/*
		Movie[] recent = bona.searchMostRecentMovies(3);
		for (Movie m : recent) 
			System.out.println(m.getTitle());
		
		Person[] active = bona.searchMostActiveActors(3);
		for (Person p : active) 
			System.out.println(p.getName());
		
		Movie[] voted = bona.searchMostVotedMovies(3);
		for (Movie m : voted) 
			System.out.println(m.getTitle());
		
		
		Person[] colab = bona.getDirectCollaboratorsOf(new Person("Robert De Niro", false));
		for (Person p : colab) 
			System.out.println(p.getName());
		*/
		
		//System.out.println(bona.getMovieByTitle("Die Hard").getTitle());
		//System.out.println(bona.getPersonByName("Robert De Niro").getName());
		
		Movie[] recent = bona.getAllMovies();
		for (Movie m : recent) 
			System.out.println(m.getTitle());
		
		Person[] active = bona.getAllPeople();
		for (Person p : active) 
			System.out.println(p.getName());
		
		System.out.println("Delete");
		
		if(bona.deleteMovieByTitle("Cape Fear")) {
			recent = bona.getAllMovies();
			for (Movie m : recent) 
				System.out.println(m.getTitle());
			
			active = bona.getAllPeople();
			for (Person p : active) 
				System.out.println(p.getName());
		}
	}
}
