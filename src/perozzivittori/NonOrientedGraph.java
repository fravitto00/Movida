package perozzivittori;

import movida.commons.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NonOrientedGraph implements Graph {
	
	//Lista di Adiacenza sui nomi degli attori
	private Map<Person, List<Person>> adjacentList = new HashMap<>();
	
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
	public Edge[] incidentEdges(Person vertex) {
		Edge[] edges = null;
		if (!adjacentList.isEmpty() && vertex != null) {
			List<Person> edgesList = adjacentList.get(vertex);
			int nEdges = edgesList.size();
			edges = new Edge[nEdges];
			for (int i=0; i < nEdges; i++)
				edges[i] = new Edge(vertex, edgesList.get(i));
		}
		return edges;
	}

	@Override
	public Person[] endVertices(Edge edge) {
		Person[] people = null;
		if (!adjacentList.isEmpty() && edge != null) {
			people = new Person[2];
			people[0] = edge.getA();
			people[1] = edge.getB();
		}
		return people;
	}

	@Override
	public Person opposite(Person vertex, Edge edge) {
		Person opposite = null;
		if (!adjacentList.isEmpty() && vertex != null && edge != null) {
			if(vertex.equals(edge.getA()))
				opposite = edge.getB();
			else if (vertex.equals(edge.getB()))
				opposite = edge.getA();
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
	public void removeEdge(Edge edge) {
		if(!adjacentList.isEmpty() && edge != null) {
			List<Person> L = null;
			L = this.adjacentList.get(edge.getA());
			for(int i=0; i<L.size(); i++) {
				if(L.get(i).equals(edge.getB()))
					this.adjacentList.get(edge.getA()).remove(i);					
			}
			L = this.adjacentList.get(edge.getB());
			for(int i=0; i<L.size(); i++) {
				if(L.get(i).equals(edge.getA()))
					this.adjacentList.get(edge.getB()).remove(i);					
			}
		}
	}

}
