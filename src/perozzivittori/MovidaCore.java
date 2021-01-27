package perozzivittori;

import java.io.*;
import java.util.Scanner;

import movida.commons.*;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch, IMovidaCollaborations {
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
	//TODO ne definiamo l'istanza col tipo adatto nei metodi in cui va usato (searchMost)
	//private sortingArray sortA;
	NonOrientedGraph graph;
	
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
		//TODO String come tipo Comparable
		this.BTMovie 	= new BTree();
		this.BTPerson 	= new BTree();
		//this.sortA		= null;
		this.graph 		= new NonOrientedGraph();
	}
	private void resetMovidaCore() {
		this.ArrMovie 	= null;
		this.ArrPerson 	= null;
		this.BTMovie 	= null;
		this.BTPerson 	= null;
		//this.sortA		= null;
		this.graph		= null;
	}
	
	/** MovidaCore Methods **/
	//TODO String come tipo Comparable
	private Object selectMethod(MapImplementation m,KeyType ktype, Operation op, String key, Object e) {
		switch(m) {
			case ArrayOrdinato:
				switch(ktype) {
					case Movie:
						switch(op) {
							case Insert:
								this.ArrMovie.insert(key, e); break;
							case Search:
								return this.ArrMovie.search(key);
							case Delete:
								return this.ArrMovie.delete(key);
							default:
								System.err.println("selectMethod(): default case");
								System.exit(1);
								break;
						}
						break;
					case Person:
						switch(op) {
							case Insert:
								this.ArrPerson.insert(key, e); break;
							case Search:
								return this.ArrPerson.search(key);
							case Delete:
								return this.ArrPerson.delete(key); 
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
							this.BTMovie.insert(key, e); break;
						case Search:
							return this.BTMovie.search(key);
						case Delete:
							return this.BTMovie.delete(key);
						default:
							System.err.println("selectMethod(): default case");
							System.exit(1);
							break;
					}
					break;
				case Person:
					switch(op) {
						case Insert:
							this.BTPerson.insert(key, e); break;
						case Search:
							return this.BTPerson.search(key);
						case Delete:
							return this.BTPerson.delete(key);
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
	
	private String normalizeString(String s) { 
		String accents = "ŠŽšžŸÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖÙÚÛÜÝàáâãäåçèéêëìíîïðñòóôõöùúûüýÿ";
		s = s.replaceAll("[^\\w["+accents+"]]", "").toLowerCase(); 
		/** elimina tutti i caratteri TRANNE(^) i WORD characters(\w =azAZ19) e i caratteri accentati*/
		return s;
	}
	
	/**
	 * IMovidaDB
	 */
	
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
				Person director = new Person(s.substring(s.indexOf(':') + 1).trim(), true);
				//Cast
				s=in.nextLine();
				String[] people = s.substring(s.indexOf(':') + 1).trim().split(",");
				Person[] cast = new Person[people.length];
				int i = 0;
				for (String p : people) {
					cast[i] = new Person(p.trim(), false);
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
				
				if(this.getPersonByName(director.getName()) == null) {
					Nname = this.normalizeString(director.getName());
					selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, Nname, director);
					selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, Nname, director);
				}
				
				for(Person p: cast) {
					if(this.getPersonByName(p.getName()) == null) {
						Nname = this.normalizeString(p.getName());
						selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, Nname, p);
						selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, Nname, p);
					}
				}

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
	        
	        Movie[] Array = this.castToMovie(this.toArray(KeyType.Movie)); 
	        
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
		//Object[] Array = this.toArray(KeyType.Movie);
		//return Array.length;
		return this.getAllMovies().length;
	}

	@Override
	public int countPeople() {
		return this.getAllPeople().length;
	}

	@Override
	public boolean deleteMovieByTitle(String title) {
		Object deletedObj = selectMethod(this.map, KeyType.Movie, Operation.Delete, this.normalizeString(title), null); 
		if (deletedObj == null) return false;
		
		//Aggiornamento Grafo
		Movie deletedMovie = (Movie) deletedObj;
		Person[] cast = deletedMovie.getCast();
 
		//No solo
		if (cast.length > 1) {
			//Per ogni attore, elimina gli archi che vanno verso gli attori successivi (l'implementazione di removeEdge evita la simmetria dell'operazione verso i precedenti)
			for(int i=0; i < cast.length-1; i++)
				for(int j=i; j < cast.length; j++)
					graph.removeMovieFromEdge(deletedMovie, cast[i], cast[j]);
		}
		return true;
	}
	
	@Override
	public Movie getMovieByTitle(String title) {
		return (Movie)selectMethod(this.map, KeyType.Movie, Operation.Search, this.normalizeString(title), null);
	}

	@Override
	public Person getPersonByName(String name) {
		return (Person)selectMethod(this.map, KeyType.Person, Operation.Search, this.normalizeString(name), null);
	}

	@Override
	public Movie[] getAllMovies() {
		return castToMovie(toArray(KeyType.Movie));
	}

	@Override
	public Person[] getAllPeople() {
		return castToPerson(toArray(KeyType.Person));
	}
	
	/**
	 * IMovidaSearch
	 */
	
	private Movie[] addToArray(Movie[] M, Movie m) {
		Movie[] returnM = new Movie[M.length+1];
		System.arraycopy(M, 0, returnM, 0, M.length);
		returnM[M.length] = m;
		return returnM;
	}

	@Override
	public Movie[] searchMoviesByTitle(String title) {
		Movie[] returnM = new Movie[0];
		Movie[] movies = this.getAllMovies();
		String t = "";
		
		for(int i=0; i<movies.length; i++) {
			t = movies[i].getTitle();
			if(t.contains(title))
				returnM = this.addToArray(returnM, movies[i]);
		}
		
		return returnM;
	}

	@Override
	public Movie[] searchMoviesInYear(Integer year) {
		Movie[] returnM = new Movie[0];
		Movie[] movies = this.getAllMovies();
		Integer y = 0;
		
		for(int i=0; i<movies.length; i++) {
			y = movies[i].getYear();
			if(y.compareTo(year) == 0)
				returnM = this.addToArray(returnM, movies[i]);
		}
		
		return returnM;
	}

	@Override
	public Movie[] searchMoviesDirectedBy(String name) {
		Movie[] returnM = new Movie[0];
		Movie[] movies = this.getAllMovies();
		String n = "";
		
		for(int i=0; i<movies.length; i++) {
			n = movies[i].getDirector().getName();
			if(n.compareTo(name) == 0)
				returnM = this.addToArray(returnM, movies[i]);
		}
		
		return returnM;
	}

	@Override
	public Movie[] searchMoviesStarredBy(String name) {
		Movie[] returnM = new Movie[0];
		Movie[] movies = this.getAllMovies();
		String pn = "";
		
		for(int i=0; i<movies.length; i++) {
			for(Person p: movies[i].getCast()) {
				pn = p.getName();
				if(pn.compareTo(name) == 0) {
					returnM = this.addToArray(returnM, movies[i]);
					break;
				}
				
			}
		}
		
		return returnM;
	}

	@Override
	public Movie[] searchMostVotedMovies(Integer N) {
		Movie[] movies = castToMovie(toArray(KeyType.Movie));
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
		Movie[] movies = castToMovie(toArray(KeyType.Movie));
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
		Movie[] movies = this.getAllMovies(); //castToMovie(toArray(KeyType.Movie));
		int nMovies = movies.length;
		Person[] people = this.getAllActors(); //castToPerson(toArray(KeyType.Person));
		int nPeople = people.length;
		
		
		SortPairIntPerson[] starredCount = new SortPairIntPerson[nPeople];
		
		for (int i=0; i< nPeople; i++) 
			starredCount[i] = new SortPairIntPerson(0, people[i]);
		
		//Riempimento array dei contatori degli attori
		Person[] cast;
		
		for(int i=0; i < nMovies; i++) {
			cast = movies[i].getCast();
			//Iterates through movie's cast
			for(int j=0; j < cast.length; j++) {
				//Iterates through actors
				for(int k=0; k < nPeople; k++) {
					//checks name instead of intere object for bo (optimization?)
					if (cast[j].getName().compareTo(people[k].getName()) == 0)  starredCount[k].increase();
				}
			}
		}
		
		sortingArray<SortPairIntPerson> countersToSort = new sortingArray<SortPairIntPerson>(starredCount);
		
		countersToSort.sort(this.alg);
		starredCount = countersToSort.getA();
		
		//return buildArray(N, nMovies, countersToSort.getA());
		Person[] returnArray = null;
		if(nPeople >= N)	returnArray = new Person[N];
		else				{returnArray = new Person[nPeople]; N = nPeople;}
		
		
		//ordine decrescente (mostActive)
		for(int i=0; i < N; i++)
			returnArray[i] = starredCount[nPeople-1-i].getPerson();
		
		return returnArray;
	}
	private Person[] getAllActors() {
		Person[] A = new Person[0];
		for(Person p: this.getAllPeople()) {
			if(!p.isDirector()) {
				Person[] tmp = new Person[A.length+1];
				System.arraycopy(A, 0, tmp, 0, A.length);
				tmp[A.length] = p;
				A = tmp;
			}
		}
		return A;
	}
	
	private Movie[] castToMovie(Object[] array) {
		Movie[] returnArray = new Movie[array.length];
		for (int i=0; i < array.length; i++)
			returnArray[i] = (Movie) array[i];
		return returnArray;
	}
	
	private Person[] castToPerson(Object[] array) {
		Person[] returnArray = new Person[array.length];
		for (int i=0; i < array.length; i++)
			returnArray[i] = (Person) array[i];
		return returnArray;
	}
	
	private Movie[] buildArray(Integer N, int nMovies, SortPairIntMovie[] pairedArray) {
		Movie[] returnArray = null;
		if(nMovies >= N)	returnArray = new Movie[N];
		else				{returnArray = new Movie[nMovies]; N = nMovies;}
		
		//ordine decrescente (mostRecent e mostVoted)
		for(int i=0; i < N; i++)
			returnArray[i] = pairedArray[nMovies-1-i].getMovie();
		
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
		
		public void increase() {this.i++;}
		
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
	
	/**
	 * IMovidaConfig
	 */
	
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
	
	/**
	 * IMovidaCollaborations
	 */
	
	@Override
	public Person[] getDirectCollaboratorsOf(Person actor) {
		Collaboration[] actorEdges = graph.incidentEdges(actor);
		int nCollaborators = actorEdges.length;
		Person[] collaborators = new Person[nCollaborators];
		
		for (int i=0; i < nCollaborators; i++)
			collaborators[i] = 	graph.opposite(actor,actorEdges[i]);
		
		return collaborators;
	}
	@Override
	public Person[] getTeamOf(Person actor) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
		// TODO Auto-generated method stub
		return null;
	}
}
