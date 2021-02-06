package perozzivittori;

import movida.commons.Person;
import movida.commons.Collaboration;
import movida.commons.Movie;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NonOrientedGraph implements Graph {
	
	//Lista di Adiacenza degli attori, lista di Collaboration come archi
	private Map<Person, List<Collaboration>> adjacentList = new HashMap<>();
	
	@Override
	public int countVertices() {
		if (!adjacentList.isEmpty())
			return adjacentList.size();
		else return 0;
	}
	
	@Override
	public int countEdges() {
		int edges = 0;
		if (!adjacentList.isEmpty()) {
			for(Person vertex : adjacentList.keySet())
				edges += adjacentList.get(vertex).size();
		}
		
		//Grafo non orientato, eliminazione dei duplicati 
		return edges/2;
	}

	@Override
	public int degree(Person vertex) {
		if (!adjacentList.isEmpty() && vertex != null)
			return adjacentList.get(vertex).size();
		else return 0;
	}
	
	@Override
	public Collaboration[] incidentEdges(Person vertex) {
		Collaboration[] edges = null;
		if (!adjacentList.isEmpty() && vertex != null) {
			List<Collaboration> edgesList = adjacentList.get(vertex);
			int nEdges = edgesList.size();
			edges = new Collaboration[nEdges];
			for (int i=0; i < nEdges; i++)
				edges[i] = (Collaboration) edgesList.get(i);
		}
		return edges;
	}
	
	@Override
	public Person[] endVertices(Collaboration edge) {
		Person[] people = null;
		if (!adjacentList.isEmpty() && edge != null) {
			people = new Person[2];
			people[0] = edge.getActorA();
			people[1] = edge.getActorB();
		}
		return people;
	}
	
	@Override
	public Person opposite(Person vertex, Collaboration edge) {
		Person opposite = null;
		if (!adjacentList.isEmpty() && vertex != null && edge != null) {
			if(vertex.equals(edge.getActorA()))
				opposite = edge.getActorB();
			else if (vertex.equals(edge.getActorB()))
				opposite = edge.getActorA();
		}
		return opposite;
	}
	
	@Override
	public boolean areAdjacent(Person vertexA, Person vertexB) {
		List<Collaboration> edgesList = null;
		if(!adjacentList.isEmpty() && vertexA != null && vertexB != null) {
			edgesList = this.adjacentList.get(vertexA);
			for(Collaboration edge: edgesList) {
				  if(this.opposite(vertexB, edge).equals(vertexA))
					  return true;
			}
		}
		return false;
	}
	
	@Override
	public void addVertex(Person vertex) {
		if(vertex != null)
			this.adjacentList.put(vertex, new LinkedList<Collaboration>());
	}
	
	@Override
	public void addEdge(Person vertexA, Person vertexB) {
		if( vertexA != null && vertexB != null) {
			Collaboration c = new Collaboration(vertexA, vertexB);
			//aggiungo new Collaboration in TESTA
			this.adjacentList.get(vertexA).add(0,c); 
			this.adjacentList.get(vertexB).add(0,c);
		}
	}

	@Override
	public void removeVertex(Person vertex) {
		if(!adjacentList.isEmpty() && vertex != null) {
			List<Collaboration> edgesList = null;
			this.adjacentList.remove(vertex);
			for(Entry<Person, List<Collaboration>> entry : this.adjacentList.entrySet()) {
				edgesList = entry.getValue();
				for(int i=0; i<edgesList.size(); i++) {
					Collaboration c = edgesList.get(i);
					if(vertex.equals(c.getActorA()) || vertex.equals(c.getActorB()))
						this.adjacentList.get(entry.getKey()).remove(i);
				}
			}
		}
		
	}

	@Override
	public boolean[] removeEdge(Collaboration edge) {
		boolean[] toRemove = {false, false};
		if(!adjacentList.isEmpty() && edge != null) {
			List<Collaboration> edgeList = null;
			//valori booleani che indicano la rimozione o meno dell'attore da movida 
			edgeList = this.adjacentList.get(edge.getActorA());
			if(edgeList.size() > 1)
				edgeList.remove(edgeList.indexOf(edge));
			else {
				adjacentList.remove(edge.getActorA());
				toRemove[0] = true;
			}
			
			edgeList = this.adjacentList.get(edge.getActorB());
			if(edgeList.size() > 1)
				edgeList.remove(edgeList.indexOf(edge));
			else {
				adjacentList.remove(edge.getActorB());
				toRemove[1] = true;
			}
		}
		return toRemove;
	}
	
	/**
	 * 
	 * Eliminazione di un Movie dal grafo, ossia da una Collaboration
	 * 
	 * @param deletedMovie il Movie da eliminare
	 * @param ActorA l'attore A della Collaboration
	 * @param ActorB l'attore B della Collaboration
	 * 
	 * @return coppia di valori booleani che indicano se gli attori vanno eliminati dalle strutture dati (non hanno Collaboration, ossia non recitano in nessun Movie)
	 */
	public boolean[] removeMovieFromEdge (Movie deletedMovie, Person ActorA, Person ActorB) {
		boolean[] toRemove = {false, false};
		
		if(!adjacentList.isEmpty() && deletedMovie != null && ActorA != null && ActorB != null) {
			// La lista delle Collaboration dell'attore A
			List<Collaboration> edgesList = adjacentList.get(ActorA);

			for (int i=0; i<edgesList.size(); i++) {
				Collaboration edge = edgesList.get(i);
				if(opposite(ActorA, edge).equals(ActorB)) {
					if(edge.getNumMovies() > 1)
						edge.deleteMovie(deletedMovie);	// La Collaboration ha pi� di un Movie, quindi viene solamente aggiornata
					else
						toRemove = removeEdge(edge);	// La Collaboration ha un solo Movie, quindi viene eliminata
				}	
			}
		}
		
		return toRemove;
	}
	/**
	 * Aggiunge un Movie in un determinato arco (tipo Collaboration)
	 * 
	 * @param movie Movie da inserire nella lista dell'arco Collaboration
	 * @param actorA vertice dell'arco
	 * @param actorB vertice dell'arco
	 */
	public void addMovieInEdge(Movie movie, Person actorA, Person actorB) {
		if( movie != null && actorA != null && actorB != null) {
			// se la Collaboration esiste, aggiungo il film sia su A che su B
			List<Collaboration> edgesList = adjacentList.get(actorA);
			if(edgesList != null) {
				for (Collaboration edge: edgesList) {
					if(opposite(actorA, edge).equals(actorB)) {
						
						edge.addMovie(movie);
						return;
					}	
				}
			}
			//se non esiste già un arco con i 2 vertici passati come parametro viene creato e poi aggiunto l'oggetto Movie
			addEdge(actorA, actorB);
			//new Collaboration aggiunta in TESTA -> get(0)
			this.adjacentList.get(actorA).get(0).addMovie(movie);
		}
	}
	/**
	 * Restituisce array contenente tutti gli archi presenti nel grafo
	 * 
	 * @return array Collaboration[]
	 */
	public Collaboration[] getAllEdges() {
		List<Collaboration> listC = new LinkedList<Collaboration>();
		if(adjacentList.isEmpty()) return null;
		for(Entry<Person, List<Collaboration>> entry : this.adjacentList.entrySet()) {
			for(Collaboration c: entry.getValue()) {
				if(listC.indexOf(c) == -1)
					listC.add(c);
			}
			
		}
		Collaboration[] returnC = listC.toArray(new Collaboration[0]);
		return returnC;
	}

}
