package perozzivittori;

import java.io.*;
import java.util.Scanner;

import movida.commons.*;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch {
	public enum Operation{
		Insert,
		Search,
		Delete;
	}
	public enum KeyType {
		Movie,
		Person;
	}
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
		resetMovidaCore();
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
		this.ArrMovie 	= new ArrayOrdinato();
		this.ArrPerson 	= new ArrayOrdinato();
		this.BTMovie 	= new BTree();
		this.BTPerson 	= new BTree();
		this.sortA		= null;
	}
	private void resetMovidaCore() {
		this.ArrMovie 	= null;
		this.ArrPerson 	= null;
		this.BTMovie 	= null;
		this.BTPerson 	= null;
		this.sortA		= null;
	}
	/** MovidaCore Methods **/
	private Object selectMethod(MapImplementation m,KeyType ktype, Operation op, Comparable key, Object e) {
		switch(m) {
			case ArrayOrdinato:
				switch(ktype) {
					case Movie:
						switch(op) {
							case Insert:
								this.ArrMovie.insert(key, e);
							case Search:
								return this.ArrMovie.search(key);
							case Delete:
								this.ArrMovie.delete(key);
							default:
								System.err.println("selectMethod(): default case");
								System.exit(1);
								break;
						}
						break;
					case Person:
						switch(op) {
							case Insert:
								this.ArrPerson.insert(key, e);
							case Search:
								return this.ArrPerson.search(key);
							case Delete:
								this.ArrPerson.delete(key);
							default:
								System.err.println("selectMethod(): default case");
								System.exit(1);
								break;
						}
						break;
					default:
						System.err.println("selectMethod(): default case");
						System.exit(1);
						break;
				}
				break;
			case BTree:
				switch(ktype) {
				case Movie:
					switch(op) {
						case Insert:
							this.BTMovie.insert(key, e);
						case Search:
							return this.BTMovie.search(key);
						case Delete:
							this.BTMovie.delete(key);
						default:
							System.err.println("selectMethod(): default case");
							System.exit(1);
							break;
					}
					break;
				case Person:
					switch(op) {
						case Insert:
							this.BTPerson.insert(key, e);
						case Search:
							return this.BTPerson.search(key);
						case Delete:
							this.BTPerson.delete(key);
						default:
							System.err.println("selectMethod(): default case");
							System.exit(1);
							break;
					}
					break;
				default:
					System.err.println("selectMethod(): default case");
					System.exit(1);
					break;
				}
				break;
			default:
				System.err.println("selectMethod(): default case");
				System.exit(1);
				break;
				
		}
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
				// Caricamento record in TUTTE le strutture
				Movie movie = new Movie(title, year, votes, cast, director); 
				/** Se esiste un film con lo stesso titolo il record viene sovrascritto (IMovidaDB) */
				selectMethod(MapImplementation.ArrayOrdinato, KeyType.Movie, Operation.Delete, title, null);
				selectMethod(MapImplementation.ArrayOrdinato, KeyType.Movie, Operation.Insert, title, movie);
				selectMethod(MapImplementation.BTree, KeyType.Movie, Operation.Delete, title, null);
				selectMethod(MapImplementation.BTree, KeyType.Movie, Operation.Insert, title, movie);
				/** Se esiste una persona con lo stesso nome non ne viene creata un'altra (IMovidaDB)*/
				if(this.getPersonByName(director.getName()) != null) {
					selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, director.getName(), director);
					selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, director.getName(), director);
				}
				for(Person p: cast) {
					if(this.getPersonByName(p.getName()) != null) {
						selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, p.getName(), p);
						selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, p.getName(), p);
					}
				}
				//System.out.println(newMovie.print()); //stampa record: Movie.print()
				if(in.hasNext()) in.next(); // salta la riga vuota tra un record e un altro
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
	        
	        Movie[] Array = null;
	        switch(this.map) {
	        	case ArrayOrdinato:
	        		Array = (Movie[]) ArrMovie.toArray();
	        	case BTree:
	        		Array = (Movie[]) BTMovie.toArray();
	        }
	        Person[] C = null;
	        for(Movie m: Array) { // A = ArrayList<Movie>
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
		resetMovidaCore();
	}

	@Override
	public int countMovies() {
		Object[] Array = null;
		switch(this.map) {
		case ArrayOrdinato:
			Array = this.ArrMovie.toArray();
		case BTree:
			Array = this.BTMovie.toArray();
		}
		return Array.length;
	}

	@Override
	public int countPeople() {
		Object[] Array = null;
		switch(this.map) {
		case ArrayOrdinato:
			Array = this.ArrPerson.toArray();
		case BTree:
			Array = this.BTPerson.toArray();
		}
		return Array.length;
	}

	@Override
	public boolean deleteMovieByTitle(String title) {
		Object r = selectMethod(this.map, KeyType.Movie, Operation.Delete, title, null); 
		if (r == null) return false;
		return true;
	}
	
	@Override
	public Movie getMovieByTitle(String title) {
		return (Movie)selectMethod(this.map, KeyType.Movie, Operation.Search, title, null);
	}

	@Override
	public Person getPersonByName(String name) {
		return (Person)selectMethod(this.map, KeyType.Person, Operation.Search, name, null);
	}

	@Override
	public Movie[] getAllMovies() {
		Movie[] movies;
		switch (map) {
			case BTree: 		movies = (Movie[])BTMovie.toArray();
								break;
			case ArrayOrdinato: movies = (Movie[])ArrMovie.toArray();
								break;
			default: 			movies = null;
					 			break;
		}
		return movies;
	}

	@Override
	public Person[] getAllPeople() {
		Person[] people;
		switch (map) {
			case BTree: 		people = (Person[])BTPerson.toArray();
								break;
			case ArrayOrdinato: people = (Person[])ArrPerson.toArray();
								break;
			default: 			people = null;
					 			break;
		}
		return people;
	}
	
	//IMovidaSearch

	@Override
	public Movie[] searchMoviesByTitle(String title) {
		
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
