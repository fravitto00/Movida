package perozzivittori;

import java.io.*;
import java.util.Scanner;

import movida.commons.*;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch {
	/**
	 * alg : enum SortingAlgorithm value
	 * map : enum MapImplementation value
	 */
	private SortingAlgorithm alg;
	private MapImplementation map;
	/****/
	private ArrayOrdinato ArrMovie;
	private ArrayOrdinato ArrPerson;
	private BTree BTMovie;
	private BTree BTPerson;
	private sortingArray sortA;
	
	/** MovidaCore CONSTRUCTORS **/
	public MovidaCore() {
		inizializeMovidaCore();
		this.alg = null;
		this.map = null;
	}
	public MovidaCore(SortingAlgorithm a, MapImplementation m) {
		if(!setSort(a) || !setMap(m)) {
			System.err.println("SortingAlgorithm or MapImplementation value: invalid");
			System.exit(1); //Aborting
		}
		this.alg = a;
		this.map = m;
		inizializeMovidaCore();	
	}
	private void inizializeMovidaCore() {
		this.ArrMovie 	= null;
		this.ArrPerson 	= null;
		this.BTMovie 	= null;
		this.BTPerson 	= null;
		this.sortA		= null;
	}
	/** MovidaCore Methods **/
	private Object[] Core() {
		
		return null;
	}
	
	// IMovidaDB
	public void loadFromFile(File f) {
		try {
			Scanner in = new Scanner(f);
			String s = "";
			while(in.hasNext()) { 
				//Title
				s=in.nextLine();
				String title 	= s.substring(s.indexOf(':') + 1).trim();
				//Year
				s=in.nextLine();
				int year 		= Integer.parseInt(s.substring(s.indexOf(':') + 1).trim());
				//Director
				s=in.nextLine();
				Person director = new Person(s.substring(s.indexOf(':') + 1).trim());
				//Cast
				s=in.nextLine();
				String[] people = s.substring(s.indexOf(':') + 1).trim().split(",");
				Person[] cast = new Person[people.length];
				int i = 0;
				for (String p : people) {
					cast[i] = new Person(p.trim());
					i++;
				}
				//Votes
				s=in.nextLine();
				int votes = Integer.parseInt(s.substring(s.indexOf(':') + 1).trim());
				
				Movie newMovie = new Movie(title, year, votes, cast, director); // Film da inserire nella struttura dati (?)
				//System.out.println(newMovie.print()); //stampa record: Movie.print()
				if(in.hasNext()) in.next(); // skip space between records
				else break;
			}
			in.close();
			
		}catch (MovidaFileException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void saveToFile(File f) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
	        PrintStream ps = new PrintStream(fos);
	        
	        Person[] C = null;
	        for(Movie m: A) { // A = ArrayList<Movie>
	        	if(C != null) ps.println();
	        	String cast = "";	        	
	        	C = m.getCast();
	        	cast += C[0].getName();
	        	for(int i=1; i<C.length; i++) 
	        		cast += ", "+ C[i].getName();
	        	ps.println("Title: "+	m.getTitle());
	        	ps.println("Year: "+	m.getYear());
	        	ps.println("Director: "+m.getDirector().getName());
	        	ps.println("Cast: "+	cast);
	        	ps.println("Votes: "+	m.getVotes());
	        }
			ps.close();
			fos.close();
		}catch (MovidaFileException | IOException e) {
			System.out.println(e.getMessage());
		}
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
		if(a == null) {
			System.err.println("SortingAlgorithm value: NULL");
			System.exit(1); //Abort
		}
		switch (a) {
			case SelectionSort:
			case HeapSort:
				this.alg = a;
				return true;
		default:
			return false;
				
		}
	}

	@Override
	public boolean setMap(MapImplementation m) {
		if(m == null) {
			System.err.println("MapImplementation value: NULL");
			System.exit(1); //Abort
		}
		switch (m) {
			case BTree:
			case ArrayOrdinato:
				this.map = m;
				return true;
			default:
				return false;
			
		}
	}
}
