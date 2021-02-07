package perozzivittori;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

import movida.commons.*;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch, IMovidaCollaborations {
	/**
	 * lista di operazioni possibili sulle strutture dati
	 */
	public enum Operation{
		Insert,
		Search,
		Delete;
	}
	/**
	 * identi
	 */
	public enum KeyType {
		Movie,
		Person;
	}
	/**
	 * alg : valore enum SortingAlgorithm 
	 * map : valore enum MapImplementation 
	 */
	private SortingAlgorithm alg;
	private MapImplementation map;
	/**
	 * strutture dati implementate, una per ogni tipo di dato memorizzato: Movie, Person
	 */
	private ArrayOrdinato ArrMovie;
	private ArrayOrdinato ArrPerson;
	private BTree BTMovie;
	private BTree BTPerson;
	//TODO ne definiamo l'istanza col tipo adatto nei metodi in cui va usato (searchMost)
	/**
	 * struttura dati rappresentante il grafo di attori
	 */
	private NonOrientedGraph graph;
	
	
	/* MovidaCore CONSTRUCTORS */
	public MovidaCore() {
		resetMovidaCore();
		this.alg = null;
		this.map = null;
	}
	/**
	 * scelta dell'algoritmo e della struttura dati da utilizzare nel costruttore
	 * 
	 * @param a algoritmo da implementare
	 * @param m struttura dati da utilizzare
	 */
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
		this.graph 		= new NonOrientedGraph();
	}
	/**
	 * utilizzato per resettare l'applicativo e tutti i dati memorizzati
	 */
	private void resetMovidaCore() {
		this.ArrMovie 	= null;
		this.ArrPerson 	= null;
		this.BTMovie 	= null;
		this.BTPerson 	= null;
		//this.sortA		= null;
		this.graph		= null;
	}
	
	/* MovidaCore Methods **/
	//TODO String come tipo Comparable
	/**
	 * metodo che permette di operare sulle strutture dati scegliendo integrandole 
	 * 
	 * @param m struttura dati sulla quale operare
	 * @param ktype tipo di dato memorizzato dalla struttura
	 * @param op tipo di operazione da eseguire sulla struttura
	 * @param key chiave dell'elemento sul quale va eseguita l'operazione(insert, search, delete)
	 * @param e elemento da inserire nella struttura se op=insert, null altrimenti
	 * 
	 * @return elemento Object restituito da search o Object eliminato da delete, null se non trovato in entrambi i casi
	 * 			search non ha return
	 */
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
	/**
	 * applica la normalizzazione alla stringa, chiave degli oggetti. 
	 * La stringa è in carattere minuscolo con eventuali numeri e caratteri accentati, 
	 * i caratteri non-word e gli spazi vuoti sono eliminati
	 * 
	 * @param s stringa da normalizzare
	 * 
	 * @return stringa cui è stata applicata la normalizzazione
	 */
	private String normalizeString(String s) { 
		String accents = "ŠŽšžŸÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖÙÚÛÜÝàáâãäåçèéêëìíîïðñòóôõöùúûüýÿ";
		s = s.replaceAll("[^\\w["+accents+"]]", "").toLowerCase(); 
		/** elimina tutti i caratteri TRANNE(^) i WORD characters(\w =azAZ19) e i caratteri accentati*/
		return s;
	}
	
	/* IMovidaDB */
	
	@Override
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
				// Upload Movie
				Movie movie = new Movie(title, year, votes, cast, director); 
				/** Se esiste un film con lo stesso titolo il record viene sovrascritto (IMovidaDB) */
				deleteMovieByTitle(Ntitle);  //delete i entrambe le strutture 
				selectMethod(MapImplementation.ArrayOrdinato, KeyType.Movie, Operation.Insert, Ntitle, movie);
				selectMethod(MapImplementation.BTree, KeyType.Movie, Operation.Insert, Ntitle, movie);
				/** Se esiste una persona con lo stesso nome non ne viene creata un'altra (IMovidaDB)*/
				String Nname = null;
				// Upload Director
				if(this.getPersonByName(director.getName()) == null) {
					Nname = this.normalizeString(director.getName());
					selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, Nname, director);
					selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, Nname, director);
				}
				// Upload Attori parte del Cast
				for(Person p: cast) {
					if(this.getPersonByName(p.getName()) == null) {
						Nname = this.normalizeString(p.getName());
						selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Insert, Nname, p);
						selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Insert, Nname, p);
						// aggiornamento grafo: aggiunge i nodi Person solo se già non presenti
						if(cast.length > 1)
							this.graph.addVertex(p);
					}
				}
				
				if (cast.length > 1) {
					//Per ogni attore, aggiunge gli archi che vanno verso gli attori successivi
					for(i=0; i < cast.length-1; i++)
						for(int j=i+1; j < cast.length; j++) {
							graph.addMovieInEdge(movie, cast[i], cast[j]);
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
		return this.getAllMovies().length;
	}

	@Override
	public int countPeople() {
		return this.getAllPeople().length;
	}

	@Override
	public boolean deleteMovieByTitle(String title) {
		Object deletedObjA = selectMethod(MapImplementation.ArrayOrdinato, KeyType.Movie, Operation.Delete, this.normalizeString(title), null);
		Object deletedObjB = selectMethod(MapImplementation.BTree, KeyType.Movie, Operation.Delete, this.normalizeString(title), null); 
		
		if (deletedObjA == null) return false;
		
		if(!deletedObjA.equals(deletedObjB)) {
			System.err.println("deleteMovieByTitle(): Deleted Objects not Equal");
			System.exit(1);
		}
		
		//Aggiornamento Grafo
		Movie deletedMovie = (Movie) deletedObjA;
		Person[] cast = deletedMovie.getCast();
 
		//Per ogni attore, elimina il Movie dagli archi che vanno verso gli attori successivi
		boolean[] toRemove;
		for(int i=0; i < cast.length-1; i++)
			for(int j=i+1; j < cast.length; j++) {
				toRemove = graph.removeMovieFromEdge(deletedMovie, cast[i], cast[j]);
				
				// Necessità di rimuovere gli attori che non prendono parte in nessun Movie a causa della cancellazione
				if(toRemove[0]) deletePersonByName(cast[i].getName());
				if(toRemove[1]) deletePersonByName(cast[j].getName());
			}
		
		return true;
	}
	
	private void deletePersonByName(String name) {
		Object deletedObjA = selectMethod(MapImplementation.ArrayOrdinato, KeyType.Person, Operation.Delete, this.normalizeString(name), null);
		Object deletedObjB = selectMethod(MapImplementation.BTree, KeyType.Person, Operation.Delete, this.normalizeString(name), null); 
		
		if (deletedObjA == null) return;
		
		if(!deletedObjA.equals(deletedObjB)) {
			System.err.println("deletePersonByName(): Deleted Objects not Equal");
			System.exit(1);
		}
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
	
	/* IMovidaSearch */
	/**
	 * Incrementa la dimensione dell'array di 1 e inserisce in coda l'elemento, res
	 * 
	 * @param M array da modificare a al quale aggiungere l'oggetto
	 * @param m oggetto inserito in coda all'array
	 * @return Array più grande di una unità con l'elemento in coda
	 */
	private Movie[] addToArray(Movie[] M, Movie m) {
		Movie[] returnM = new Movie[M.length+1];
		System.arraycopy(M, 0, returnM, 0, M.length);
		returnM[M.length] = m;
		return returnM;
	}
	private Person[] addToArray(Person[] M, Person p) {
		Person[] returnP = new Person[M.length+1];
		System.arraycopy(M, 0, returnP, 0, M.length);
		returnP[M.length] = p;
		return returnP;
	}
	private Collaboration[] addToArray(Collaboration[] M, Collaboration p) {
		Collaboration[] returnC = new Collaboration[M.length+1];
		System.arraycopy(M, 0, returnC, 0, M.length);
		returnC[M.length] = p;
		return returnC;
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
		Movie[] movies = this.getAllMovies();
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
		Movie[] movies = this.getAllMovies();
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
		Movie[] movies = this.getAllMovies();
		int nMovies = movies.length;
		Person[] people = this.getAllActors();
		int nPeople = people.length;
		
		
		SortPairIntPerson[] starredCount = new SortPairIntPerson[nPeople];
		
		for (int i=0; i< nPeople; i++) 
			starredCount[i] = new SortPairIntPerson(0, people[i]);
		
		//Riempimento array dei contatori degli attori
		Person[] cast;
		
		for(int i=0; i < nMovies; i++) {
			cast = movies[i].getCast();
			
			// Itera tra cast del Movie
			for(int j=0; j < cast.length; j++) {
				
				// Itera tra gli attori
				for(int k=0; k < nPeople; k++) {

					// Confronto effettuato sul nome dell'attore (metodo equals sovrascritto in Person)
					if (cast[j].equals(people[k])) starredCount[k].increase();
				}
			}
		}
		
		sortingArray<SortPairIntPerson> countersToSort = new sortingArray<SortPairIntPerson>(starredCount);
		
		countersToSort.sort(this.alg);
		starredCount = countersToSort.getA();
		
		// buildArray(N, nMovies, SortPairIntPerson)
		Person[] returnArray = null;
		if(nPeople >= N)
			returnArray = new Person[N];
		else {
			returnArray = new Person[nPeople];
			N = nPeople;
		}
		
		
		// Ordine decrescente (mostActive)
		for(int i=0; i < N; i++)
			returnArray[i] = starredCount[nPeople-1-i].getPerson();
		
		return returnArray;
	}
	/**
	 * tramite il metodo getAllPeople() scorre tutti gli oggetti Person memorizzati nell'applicativo e ne estrae solo gli attori
	 * Gli oggetti Person hanno un attributo booleano 'director' che per gli attori è impostato a false
	 * 
	 * @return Array di oggetti Person composto da solo attori, senza registi
	 */
	private Person[] getAllActors() {
		Person[] A = new Person[0];
		for(Person p: this.getAllPeople()) {
			if(!p.isDirector()) {
				A = this.addToArray(A, p);
			}
		}
		return A;
	}
	
	/**
	 * Esegue il casting di un array di tipo Object in uno di tipo Movie
	 * 
	 * @param array l'array su cui eseguire il casting
	 * @return l'array di tipo Movie
	 */
	private Movie[] castToMovie(Object[] array) {
		Movie[] returnArray = new Movie[array.length];
		for (int i=0; i < array.length; i++)
			returnArray[i] = (Movie) array[i];
		return returnArray;
	}
	
	/**
	 * Esegue il casting di un array di tipo Object in uno di tipo Person
	 * 
	 * @param array l'array su cui eseguire il casting
	 * @return l'array di tipo Person
	 */
	private Person[] castToPerson(Object[] array) {
		Person[] returnArray = new Person[array.length];
		for (int i=0; i < array.length; i++)
			returnArray[i] = (Person) array[i];
		return returnArray;
	}
	
	/**
	 * Compone l'array ordinato
	 * 
	 * @param N il numero di Movie di cui dev'essere composto l'array
	 * @param nMovies il numero di Movie presenti nell'array ordinato
	 * @param pairedArray l'array ordinato
	 * @return l'array di N Movie in ordine decrescente
	 */
	private Movie[] buildArray(Integer N, int nMovies, SortPairIntMovie[] pairedArray) {
		Movie[] returnArray = null;
		if(nMovies >= N)
			returnArray = new Movie[N];
		else {
			returnArray = new Movie[nMovies];
			N = nMovies;
		}
		
		// Ordine decrescente (mostRecent e mostVoted)
		for(int i=0; i < N; i++)
			returnArray[i] = pairedArray[nMovies-1-i].getMovie();
		
		return returnArray;
	}
	
	/**
	 * Classe che implementa una coppia intero-Movie per effettuare il sorting
	 */
	private class SortPairIntMovie implements Comparable<SortPairIntMovie>{
		private Integer i;
		private Movie m;
		
		public SortPairIntMovie(Integer i, Movie m) { this.i=i; this.m=m;}
		
		public int compareTo(SortPairIntMovie sp) {
			return i.compareTo(sp.getInt());
		}
		
		public Integer getInt() {return i;}
		
		public Movie getMovie() {return m;}
	}
	
	/**
	 * Classe che implementa una coppia intero-Person per effettuare il sorting
	 */
	private class SortPairIntPerson implements Comparable<SortPairIntPerson>{
		private Integer i;
		private Person p;
		
		public SortPairIntPerson(Integer i, Person p) { this.i=i; this.p=p;}
		
		public int compareTo(SortPairIntPerson sp) {
			return i.compareTo(sp.getInt());
		}
		
		public void increase() {this.i++;}
		
		public Integer getInt() {return i;}
		
		public Person getPerson() {return p;}
	}
	
	/**
	 * Esegue l'operazione toArray() sulla struttura dati in uso
	 * 
	 * @param ktype il tipo di record, Movie/Person
	 * @return l'array di record Movie/Person
	 */
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
		Person[] team = new Person[0];
		if(actor != null) 	return this.getTeamOfREC(actor, team);
		else				return null;
	}
	
	/**
	 * Funzione ricorsiva che si occupa di formare l'array di Person facenti parte dello stesso team
	 * se l'attore non è già presente nell'array lo inserisce
	 * Visita poi nel grafo gli archi incidenti il vertice, relativo al parametro Person 'actor',
	 * richiamando la funzione per ognuno dei vertici adiacenti ad 'actor'
	 * 
	 * @param actor nodo attualmente in visita
	 * @param team array con i componeti del team
	 * @return
	 */
	private Person[] getTeamOfREC(Person actor, Person[] team) {
		if(this.notIn(actor, team)) 
			team = this.addToArray(team, actor);
		for(Collaboration edge: graph.incidentEdges(actor)) {
			Person p = graph.opposite(actor, edge);
			if(this.notIn(p, team))
				team = getTeamOfREC(p, team);
		}
		return team;
	}
	/**
	 * verifica se un determinato oggetto Person è presente nell'array dei componenti del team
	 * 
	 * @param actor: oggetto Person
	 * @param team: array componenti Team
	 * @return booleano che esprime la presenza o meno di 'actor' nell'array 'team'
	 */
	private boolean notIn(Person actor, Person[] team) {
		for(Person p: team) {
			if(p.equals(actor))
				return false;
		}
		return true;
	}
	@Override
	public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
		Person[] team = this.getTeamOf(actor); 											//array di attori facenti parte dello stesso team
		Collaboration[] teamEdges = new Collaboration[0];								//array delle Collaboration totali del team
		Collaboration[] MST = new Collaboration[0];										//array delle Collaboration facenti parte del Maximum Spanning Tree
		LinkedList<LinkedList<Person>> unionFind = new LinkedList<LinkedList<Person>>();//struttura emulante la Union Find
		int i = 0;
		//se team è null, actor è null o l'attore non è presente nelle strutture ritorna NULL
		if(team == null) return null;
		if(actor == null) return null;
		if(this.selectMethod(map, KeyType.Person, Operation.Search, this.normalizeString(actor.getName()), null) == null) return null;
		//inizializzazione struttura UnionFind
		for(int k=0; k<team.length; k++) {
			unionFind.add(new LinkedList<Person>());
			unionFind.get(k).add(team[k]);			
		}
		//creazione array degli archi Collaboration che collegano i nodi Person del team
		boolean isTeamEdge = false;
		
		for(Collaboration c: this.graph.getAllEdges()){
			for(int j=0; j<team.length; j++) {
				if(c.getActorA().equals(team[j]) || c.getActorB().equals(team[j])) {
					isTeamEdge = true; 	//basta che uno solo dei 2 vertici dell'arco faccia parte del team, di conseguenza
					break;				//anche l'altro capo dell'arco ne è parte (vale come è la struttura del team)	
				}
			}
			//aggiunga dell'arco all'array di archi facenti parti del team
			if(isTeamEdge) 
				teamEdges = this.addToArray(teamEdges, c);
		}
		//Ordinamento array Collaboration del team
		teamEdges = sortCollabArray(teamEdges);
		
		// generazione Maximum Spanning Tree
		Collaboration c = null;
		returnValues rValues = null;
		
		while(i < teamEdges.length && MST.length < team.length-1){ // numero archi = numero nodi - 1
			c = teamEdges[i];
			rValues = isMST(unionFind, c.getActorA(), c.getActorB()); //verifica se l'arco fa parte del MST
			// [0]= 1: true/else: false; [1]= indice actorA; [2]= indice actorB;
			if(rValues.isMST) { // c fa parte del MST
				MST =  this.addToArray(MST, c);
				//se l'insieme di diensione minore tra quello che contiene a e b viene accodato all'altro
				//si prendono tutti gli elementi dell'insieme più piccolo e vengono inseriti nell'altro
				//dopo l'append l'insieme più piccolo è eliminato
				if(unionFind.get(rValues.indexA).size() >= unionFind.get(rValues.indexB).size()) {
					unionFind.get(rValues.indexA).addAll(unionFind.get(rValues.indexB));
					unionFind.remove(rValues.indexB);
				}else{
					unionFind.get(rValues.indexB).addAll(unionFind.get(rValues.indexA));
					unionFind.remove(rValues.indexA);
				}
			}
			i = i+1;
		}		
		return MST;
	}
	/**
	 * verifica che i 2 oggetti Person (vertici di un arco del grafo) non facciano parte dello stesso albero nella struttura
	 * Union Find che viene visitata: se si verifica che i 2 vertici appartengono allo stesso insieme viene subito restituito
	 * false. Se invece i 2 vertici non sono connessi nemmeno indirettamente, viene scandita interamente la struttura, vengono
	 * identificati gli indici dei 2 oggetti all'interno della struttura Union Find e infine viene restituito true 
	 * (l'oggetto arco Collaboration che collega 'a' e 'b' fa farte del Maximum Spanning Tree) 
	 * 
	 * @param unionFind: struttura contenente i gruppi di vertici già uniti tra loro da almeno un arco
	 * @param a: vertice A dell'arco Collaboration
	 * @param b: vertice B dell'arco Collaboration
	 * @return oggetto istanza di 'returnValue'(boolean, int, int). Se l'arco fa parte del MST il return è:
	 * 			(true, indice di Person a, indice di Person b) altrimenti ritorna false e i valori degli indici non vengono utilizzati
	 * 			e l'esecuzione della funzione è interrotta 
	 * 
	 */
	private returnValues isMST(LinkedList<LinkedList<Person>> unionFind,  Person a, Person b) {
		returnValues rValues = new returnValues();
		rValues.isMST = true; //impostato a false solo se l'arco non fa parte del MST
		for(int k=0; k<unionFind.size(); k++) {
			if(unionFind.get(k).indexOf(a) > -1 && unionFind.get(k).indexOf(b) > -1) { //entrambi si trovano nello stesso insieme
				rValues.isMST = false; //return false
				return rValues;
			}
			//identificazione indice di a e b all'interno della Union Find
			if(unionFind.get(k).indexOf(a) > -1) rValues.indexA = k;
			if(unionFind.get(k).indexOf(b) > -1) rValues.indexB = k;	
		}
			
		return rValues; 
	}
	/**
	 * Ordinamento dell'array tipo Collaboration in ordine decrescente secondo l'attributo Score degli archi Collaboration
	 * viene utilizzato come algoritmo quello già implementato dell'HeapSort, tra dei migliori per il costo computazionale
	 * Le istruzione seguono la linea dei metodi 'searchMost *' nel quale vengono implementati gli algoritmi di ordinamento
	 * forniti dall'applicativo
	 * 
	 * @param edges: array di oggetti Collaboration, contiene singolarmente tutti gli archi del Team individuato
	 * @return  array di oggetti Collaboration ordinato secondo la chiave int Score (attributo della classe Collaboration)
	 */
	private Collaboration[] sortCollabArray(Collaboration[] edges) {
		int i, 
			nEdges = edges.length;
		SortPairDoubleCollab[] scores = new SortPairDoubleCollab[nEdges];
		
		for(i=0; i<nEdges; i++) 
			scores[i] = new SortPairDoubleCollab(edges[i].getScore(), edges[i]);
		
		sortingArray<SortPairDoubleCollab> scoresToSort = new sortingArray<SortPairDoubleCollab>(scores);
		scoresToSort.sort(SortingAlgorithm.HeapSort);
		scores = scoresToSort.getA();
		
		for(i=0; i<nEdges; i++)
			edges[nEdges-1-i] = scores[i].getCollaboration();
		
		return edges;
	}
	/**
	 * classe creata esclusivamete per permettere un ritorno di oggetti di tipo eterogeneo nel metodo 'isMST()'
	 *
	 */
	private class returnValues{
		
		boolean isMST;
		int indexA;
		int indexB;
		
		public returnValues() {
			this.isMST = true;
			this.indexA = 0;
			this.indexB = 0;
		} 
			
	}
	/**
	 * classe creata sull'impronta di SortPairIntMovie e SortPairIntPerson utilizzata per l'ordinamento di oggetti 
	 * istanze di Collaboration secondo il suo attributo di tipo Double 'score' nel metodo sortCollabArray() 
	 *
	 */
	private class SortPairDoubleCollab implements Comparable<SortPairDoubleCollab>{
		private Double d;
		private Collaboration c;
		
		public SortPairDoubleCollab(Double d, Collaboration c) { this.d=d; this.c=c;}
		
		public int compareTo(SortPairDoubleCollab sc) {
			return d.compareTo(sc.getDouble());
		}
			
		public Double getDouble() {return d;}
		
		public Collaboration getCollaboration() {return c;}
	}
}
