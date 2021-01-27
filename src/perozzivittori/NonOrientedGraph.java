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
		if(!adjacentList.isEmpty() && vertex != null)
			this.adjacentList.put(vertex, new LinkedList<Collaboration>());
	}

	@Override
	public void addEdge(Person vertexA, Person vertexB) {
		if(!adjacentList.isEmpty() && vertexA != null && vertexB != null) {
			this.adjacentList.get(vertexA).add(new Collaboration(vertexA, vertexB));
			this.adjacentList.get(vertexB).add(new Collaboration(vertexA, vertexB));
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

}
