package perozzivittori;

import movida.commons.Person;
import movida.commons.Collaboration;

public interface Graph {
	public int countVertices();
	
	public int countEdges();
	
	/**
	* I vertici sono rappresentati dagli attori e gli archi da Collaboration
	*/
	public int degree(Person vertex);
	
	public Collaboration[] incidentEdges(Person vertex);
	
	public Person[] endVertices();
	
	public Person opposite(Person vertex, Collaboration edge);
	
	public boolean areAdjacent(Person vertexA, Person vertexB);
	
	public void addVertex(Person vertex);
	
	public void addEdge(Person vertexA, Person vertexB);
	
	public void removeVertex(Person vertex);
	
	public void removeEdge(Collaboration edge);
}
