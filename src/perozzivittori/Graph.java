package perozzivittori;

import movida.commons.Person;
import movida.commons.Collaboration;

public interface Graph {
	/**
	 * Calcola il numero dei vertici
	 * 
	 * @return il numero dei vertici
	 */
	public int countVertices();
	
	/**
	 * Calcola il numero degli archi
	 * 
	 * @return il numero degli archi
	 */
	public int countEdges();
	
	/**
	* I vertici sono rappresentati dagli attori e gli archi da Collaboration
	*/
	
	/**
	 * Calcola il grado di un vertice, ossia il numero di archi uscenti/entranti
	 * 
	 * @param vertex il vertice
	 * 
	 * @return il numero di archi
	 */
	public int degree(Person vertex);
	
	/**
	 * Acquisizione degli archi incidenti da/a un vertice
	 * 
	 * @param vertex il vertice
	 * 
	 * @return array di archi incidenti
	 */
	public Collaboration[] incidentEdges(Person vertex);
	
	/**
	 * Acquisizione degli estremi di un arco
	 * 
	 * @param edge l'arco
	 * 
	 * @return i due vertici estremi dell'arco
	 */
	public Person[] endVertices(Collaboration edge);
	
	/**
	 * Acquisizione del vertice opposto, dato un vertice e un suo arco
	 * 
	 * @param vertex il vertice
	 * @param edge l'arco
	 * 
	 * @return il vertice opposto
	 */
	public Person opposite(Person vertex, Collaboration edge);
	
	public boolean areAdjacent(Person vertexA, Person vertexB);
	
	public void addVertex(Person vertex);
	
	public void addEdge(Person vertexA, Person vertexB);
	
	public void removeVertex(Person vertex);
	
	public boolean[] removeEdge(Collaboration edge);
}
