package perozzivittori;

import java.io.*;

import movida.commons.*;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch {
	
	// IMovidaDB
	
	public void loadFromFile(File f) {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(f)); 
			  
			String st; 
			while ((st = br.readLine()) != null) {
				String title 	= st.substring(st.indexOf(':') + 1).trim();
				int year 		= Integer.parseInt( br.readLine().substring(st.indexOf(':') + 1).trim() );
				Person director = new Person( br.readLine().substring(st.indexOf(':') + 1).trim() );
				String[] people = br.readLine().substring(st.indexOf(':') + 1).trim().split(",");
				Person[] cast = new Person[people.length];
				int i = 0;
				for (String p : people) {
					cast[i] = new Person(p.trim());
					i++;
				}
				int votes = Integer.parseInt( br.readLine().substring(st.indexOf(':') + 1).trim() );
				Movie newMovie = new Movie(title, year, votes, cast, director); // Film da inserire nella struttura dati (?)
				//System.out.println(newMovie.getCast()[0].getName());
				br.readLine();
			}
			br.close();
			
		}catch (MovidaFileException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void saveToFile(File f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int countMovies() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countPeople() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteMovieByTitle(String title) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Movie getMovieByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person getPersonByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Movie[] getAllMovies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person[] getAllPeople() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//IMovidaSearch

	@Override
	public Movie[] searchMoviesByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Movie[] searchMoviesInYear(Integer year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Movie[] searchMoviesDirectedBy(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Movie[] searchMoviesStarredBy(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Movie[] searchMostVotedMovies(Integer N) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Movie[] searchMostRecentMovies(Integer N) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person[] searchMostActiveActors(Integer N) {
		// TODO Auto-generated method stub
		return null;
	}

	// IMovidaConfig
	
	@Override
	public boolean setSort(SortingAlgorithm a) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setMap(MapImplementation m) {
		// TODO Auto-generated method stub
		return false;
	}
}
