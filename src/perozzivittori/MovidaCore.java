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
	private String deleteAccent(String s) {
		char[] cAcc= "Å Å½Å¡Å¾Å¸Ã€Ã�Ã‚ÃƒÃ„Ã…Ã‡ÃˆÃ‰ÃŠÃ‹ÃŒÃ�ÃŽÃ�Ã�Ã‘Ã’Ã“Ã”Ã•Ã–Ã™ÃšÃ›ÃœÃ�Ã Ã¡Ã¢Ã£Ã¤Ã¥Ã§Ã¨Ã©ÃªÃ«Ã¬Ã­Ã®Ã¯Ã°Ã±Ã²Ã³Ã´ÃµÃ¶Ã¹ÃºÃ»Ã¼Ã½Ã¿".toCharArray();
		char[] cReg= "SZszYAAAAAACEEEEIIIIDNOOOOOUUUUYaaaaaaceeeeiiiidnooooouuuuyy".toCharArray();
		char[] c = s.toCharArray();
		for(int i=0; i<c.length; i++) {
			for(int j=0; j<cAcc.length; j++) {
				if(c[i] == cAcc[j])
					c[i] = cReg[j];
			}
		}
		return String.valueOf(c);
	}
	public String normalizeString(String s) { 
		s = this.deleteAccent(s);
		s = s.replaceAll("[^\\w]", "").toLowerCase(); 
		/** elimina tutti i caratteri TRANNE(^) i WORD characters(\w =azAZ19)*/
		return s;
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
				//Normalizzazione Titolo
				String Ntitle = this.normalizeString(title);
				// Caricamento record in TUTTE le strutture
				Movie movie = new Movie(title, year, votes, cast, director); 
				/** Se esiste un film con lo stesso titolo il record viene sovrascritto (IMovidaDB) */
				selectMethod(MapImplementation.ArrayOrdinato, KeyType.Movie, Operation.Delete, Ntitle, null);
				selectMethod(MapImplementation.ArrayOrdinato, KeyType.Movie, Operation.Insert, Ntitle, movie);
				selectMethod(MapImplementation.BTree, KeyType.Movie, Operation.Delete, Ntitle, null);
				selectMethod(MapImplementation.BTree, KeyType.Movie, Operation.Insert, Ntitle, movie);
				/** Se esiste una persona con lo stesso nome non ne viene creata un'altra (IMovidaDB)*/
				String Nname = null;
				if(this.getPersonByName(director.getName()) != null) {
					Nname = this.normalizeString(director.getName());
					selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, Nname, director);
					selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, Nname, director);
				}
				for(Person p: cast) {
					if(this.getPersonByName(p.getName()) != null) {
						Nname = this.normalizeString(p.getName());
						selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, Nname, p);
						selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, Nname, p);
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
			break;
		case BTree:
			Array = this.BTMovie.toArray();
			break;
		default:
			Array = null;
			break;
		}
		return Array.length;
	}

	@Override
	public int countPeople() {
		Object[] Array = null;
		switch(this.map) {
		case ArrayOrdinato:
			Array = this.ArrPerson.toArray();
			break;
		case BTree:
			Array = this.BTPerson.toArray();
			break;
		default:
			Array = null;
			break;
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
		return (Movie[]) toArray(KeyType.Person);
	}

	@Override
	public Person[] getAllPeople() {
		return (Person[]) toArray(KeyType.Person);
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
		Movie[] movies = (Movie[]) toArray(KeyType.Movie);
		int nMovies = movies.length;
		SortPairIntMovie[] votes = new SortPairIntMovie[nMovies];
		
		for (int i=0; i< nMovies; i++) 
			votes[i] = new SortPairIntMovie(movies[i].getVotes(), movies[i]);
		
		sortingArray<SortPairIntMovie> votesToSort = new sortingArray<SortPairIntMovie>(votes);
		
		votesToSort.sort(this.alg);
		
		return buildArray(N, nMovies, votesToSort.getA());
	}

	@Override
	public Movie[] searchMostRecentMovies(Integer N) {
		Movie[] movies = (Movie[]) toArray(KeyType.Movie);
		int nMovies = movies.length;
		SortPairIntMovie[] years = new SortPairIntMovie[nMovies];
		
		for (int i=0; i< nMovies; i++) 
			years[i] = new SortPairIntMovie(movies[i].getYear(), movies[i]);
		
		sortingArray<SortPairIntMovie> yearsToSort = new sortingArray<SortPairIntMovie>(years);
		
		yearsToSort.sort(this.alg);
		
		return buildArray(N, nMovies, yearsToSort.getA());
	}

	@Override
	public Person[] searchMostActiveActors(Integer N) {
		// TODO Auto-generated method stub
		Movie[] movies = (Movie[]) toArray(KeyType.Movie);
		int nMovies = movies.length;
		Person[] people = (Person[]) toArray(KeyType.Person);
		int nPeople = people.length;
		
		SortPairIntPerson[] nStarred = new SortPairIntPerson[nPeople];
		
		for (int i=0; i< nPeople; i++) 
			nStarred[i] = new SortPairIntPerson(0, people[i]);
		
		for(int i=0; i < nMovies; i++) {
			Person[] cast = movies[i].getCast();
			for(int j=0; i < cast.length; i++) {
				for(int k=0; i < nPeople; i++) {
					//checks name instead of intere object for bo (optimization?)
					if (cast[j].getName() == people[k].getName())  nStarred[k].increase();
				}
			}
		}
		
		sortingArray<SortPairIntPerson> countersToSort = new sortingArray<SortPairIntPerson>(nStarred);
		
		countersToSort.sort(this.alg);
		nStarred = countersToSort.getA();
		
		//return buildArray(N, nMovies, countersToSort.getA());
		Person[] returnArray = null;
		if(nPeople >= N)	returnArray = new Person[N];
		else				returnArray = new Person[nMovies]; N = nMovies;
		
		
		//ordine decrescente (mostRecent e mostVoted)
		for(int i=0; i < N; i++)
			returnArray[i] = nStarred[N-1-i].getPerson();
		
		return returnArray;
	}
	
	private Movie[] buildArray(Integer N, int nMovies, SortPairIntMovie[] pairedArray) {
		Movie[] returnArray = null;
		if(nMovies >= N)	returnArray = new Movie[N];
		else				returnArray = new Movie[nMovies]; N = nMovies;
		
		//ordine decrescente (mostRecent e mostVoted)
		for(int i=0; i < N; i++)
			returnArray[i] = pairedArray[N-1-i].getMovie();
		
		return returnArray;
	}
	
	private class SortPairIntMovie implements Comparable<SortPairIntMovie>{
		Integer i;
		Movie m;
		
		public SortPairIntMovie(Integer i, Movie m) { this.i=i; this.m=m;}
		
		public int compareTo(SortPairIntMovie sp) {
			return i.compareTo(sp.getInt());
		}
		
		public Integer getInt() {return i;}
		
		public Movie getMovie() {return m;}
	}
	
	private class SortPairIntPerson implements Comparable<SortPairIntPerson>{
		Integer i;
		Person p;
		
		public SortPairIntPerson(Integer i, Person p) { this.i=i; this.p=p;}
		
		public int compareTo(SortPairIntPerson sp) {
			return i.compareTo(sp.getInt());
		}
		
		public void increase() {i++;}
		
		public Integer getInt() {return i;}
		
		public Person getPerson() {return p;}
	}
	
	private Object[] toArray(KeyType ktype) {
		Object[] r = null;
		switch(this.map) {
			case ArrayOrdinato:
				switch(ktype) {
					case Movie:	r = ArrMovie.toArray();
								break;
					case Person:r = ArrPerson.toArray();
								break;
					default:
						System.err.println("selectMethod(): default case");
						System.exit(1);
						break;
				}
				break;
			case BTree:
				switch(ktype) {
					case Movie:	r = BTMovie.toArray();
								break;
					case Person:r = BTPerson.toArray();
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
		
		return r;
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
