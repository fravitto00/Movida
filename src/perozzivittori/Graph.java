package perozzivittori;

import movida.commons.Person;

public interface Graph {
	public int countVertices();
	
	public int countEdges();
	
	/**
	* I vertici sono rappresentati dagli attori e gli archi da Collaboration
	*/
	public int degree(Person vertex);
	
	public Edge[] incidentEdges(Person vertex);
	
	public Person[] endVertices(Edge edge);
	
	public Person opposite(Person vertex, Edge edge);
	
	public boolean areAdjacent(Person vertexA, Person vertexB);
	
	public void addVertex(Person vertex);
	
	public void addEdge(Person vertexA, Person vertexB);
	
	public void removeVertex(Person vertex);
	
	public void removeEdge(Edge edge);
}
