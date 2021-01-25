package perozzivittori;

import movida.commons.Collaboration;
import movida.commons.Person;

public class NonOrientedGraph implements Graph {

	@Override
	public int countVertices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countEdges() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int degree(Person vertex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collaboration[] incidentEdges(Person vertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person[] endVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person opposite(Person vertex, Collaboration edge) {
		// TODO Auto-generated method stub
		return null;
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
	public void removeEdge(Collaboration edge) {
		// TODO Auto-generated method stub
		
	}

}
