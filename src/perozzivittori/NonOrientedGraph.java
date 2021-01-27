package perozzivittori;

import movida.commons.Person;
import movida.commons.Collaboration;
import movida.commons.Movie;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		List<Person> L = null;
		if(!adjacentList.isEmpty() && vertexA != null && vertexB != null) {
			L = this.adjacentList.get(vertexA);
			for(Person adjacentV: L) {
				if (adjacentV.equals(vertexB))
					return true;
			}
		}
		return false;
	}

	@Override
	public void addVertex(Person vertex) {
		if(!adjacentList.isEmpty() && vertex != null)
			this.adjacentList.put(vertex, new LinkedList<Person>());
	}

	@Override
	public void addEdge(Person vertexA, Person vertexB) {
		if(!adjacentList.isEmpty() && vertexA != null && vertexB != null) {
			this.adjacentList.get(vertexA).add(vertexB);
			this.adjacentList.get(vertexB).add(vertexA);
		}
	}

	@Override
	public void removeVertex(Person vertex) {
		if(!adjacentList.isEmpty() && vertex != null) {
			List<Person> L = null;
			this.adjacentList.remove(vertex);
			for(Map.Entry<Person, List<Person>> entry : this.adjacentList.entrySet()) {
				L = entry.getValue();
				for(int i=0; i<L.size(); i++) {
					if(L.get(i).equals(vertex))
						this.adjacentList.get(entry.getKey()).remove(i);
				}
					
			}
		}
		
	}

	@Override
	public void removeEdge(Collaboration edge) {
		if(!adjacentList.isEmpty() && edge != null) {
			List<Person> L = null;
			L = this.adjacentList.get(edge.getActorA());
			for(int i=0; i<L.size(); i++) {
				if(L.get(i).equals(edge.getActorB()))
					this.adjacentList.get(edge.getActorA()).remove(i);					
			}
			L = this.adjacentList.get(edge.getActorB());
			for(int i=0; i<L.size(); i++) {
				if(L.get(i).equals(edge.getActorA()))
					this.adjacentList.get(edge.getActorB()).remove(i);					
			}
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

}
