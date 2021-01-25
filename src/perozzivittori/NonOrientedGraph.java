package perozzivittori;

import movida.commons.Person;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonOrientedGraph implements Graph {
	
	//Lista di Adiacenza sui nomi degli attori
	Map<Person, List<Person>> adjacentList = new HashMap<>();
	
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addVertex(Person vertex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEdge(Person vertexA, Person vertexB) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeVertex(Person vertex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEdge(Edge edge) {
		// TODO Auto-generated method stub
		
	}

}
