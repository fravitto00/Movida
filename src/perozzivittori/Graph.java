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
	/**
	 * Verifica se 2 vertici sono adiacenti (fanno parte dello stesso arco Collaboration)
	 * l'arco può essere (vertexA, vertexB) o viceversa, (vertexB, vertexA)
	 * 
	 * @param vertexA primo vertice
	 * @param vertexB secondo vertice
	 * @return true se i 2 vertici sono adiacenti, false altrimenti
	 */
	public boolean areAdjacent(Person vertexA, Person vertexB);
	/**
	 * Aggiunge un nuovo vertice al grafo e istanzia il suo insieme di archi
	 * 
	 * @param vertex vertice da aggiungere all'arco
	 */
	public void addVertex(Person vertex);
	/**
	 * Aggiunge un nuovo arco al grafo, crea l'arco e aggiunge la stessa istanza dell'oggetto Collaboration
	 * a entrambi i vertici dell'arco
	 * 
	 * @param vertexA vertice dell'arco
	 * @param vertexB vertice dell'arco
	 */
	public void addEdge(Person vertexA, Person vertexB);
	/**
	 * Elimina un determinato vertice dal grafo
	 * 
	 * @param vertex vertice da rimuovere
	 */
	public void removeVertex(Person vertex);
	/**
	 * Elimina un determinato arco dal grafo
	 * 
	 * @param edge arco da rimuovere
	 * @return 	array di 2 valori booleani che indicano se e quali dei due vertici dell'arco sono stati eliminati
	 * 			interamente dalla struttura grafo 
	 */
	public boolean[] removeEdge(Collaboration edge);
}
