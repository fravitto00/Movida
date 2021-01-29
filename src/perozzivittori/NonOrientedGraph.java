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
	
	//Lista di Adiacenza sui nomi degli attori
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
	public void removeEdge(Collaboration edge) {
		if(!adjacentList.isEmpty() && edge != null) {
			List<Collaboration> edgeList = null;
			edgeList = this.adjacentList.get(edge.getActorA());
			edgeList.remove(edgeList.indexOf(edge));
		/*	for(int i=0; i<edgeList.size(); i++) {
				if(edgeList.get(i).equals(edge))
					this.adjacentList.get(edge.getActorA()).remove(i);					
			}*/
			edgeList = this.adjacentList.get(edge.getActorB());
			edgeList.remove(edgeList.indexOf(edge));
		/*	for(int i=0; i<edgeList.size(); i++) {
				if(edgeList.get(i).equals(edge))
					this.adjacentList.get(edge.getActorB()).remove(i);					
			}*/
		}
	}
	
	public void removeMovieFromEdge (Movie deletedMovie, Person ActorA, Person ActorB) {
		if(!adjacentList.isEmpty() && deletedMovie != null && ActorA != null && ActorB != null) {
			List<Collaboration> edgesList = adjacentList.get(ActorA);
			
			for (Collaboration edge: edgesList) {
				if(opposite(ActorA, edge).equals(ActorB)) {
					// Il suo simmetrico
					Collaboration symEdge = adjacentList.get(ActorB).get(adjacentList.get(ActorB).indexOf(edge));
					if(edge.getNumMovies() > 1) { 
						edge.deleteMovie(deletedMovie);
						symEdge.deleteMovie(deletedMovie);
					} else {
						removeEdge(edge);
						removeEdge(symEdge);
					}
				}	
			}
		}
	}
	
	public void addMovieInEdge(Movie movie, Person actorA, Person actorB) {
		if( movie != null && actorA != null && actorB != null) {
			// se la Collaboration esiste, aggiungo il film sia su A che su B
			List<Collaboration> edgesList = adjacentList.get(actorA);
			//if(!adjacentList.isEmpty()) {
				for (Collaboration edge: edgesList) {
					if(opposite(actorA, edge).equals(actorB)) {
						
						Collaboration symEdge = adjacentList.get(actorB).get(adjacentList.get(actorB).indexOf(edge));
						//aggiungo il film nella lista di entrambi
						edge.addMovie(movie);
						symEdge.addMovie(movie);
						return;
					}	
				}
			//}
			addEdge(actorA, actorB);
			//new Collaboration aggiunta in TESTA -> get(0)
			this.adjacentList.get(actorA).get(0).addMovie(movie);
			this.adjacentList.get(actorB).get(0).addMovie(movie);
		}
	}
	
	public Collaboration[] getAllEdges() {
		List<Collaboration> listC = new LinkedList<Collaboration>();
		if(!adjacentList.isEmpty()) return null;
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
