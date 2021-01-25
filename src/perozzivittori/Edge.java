package perozzivittori;

import movida.commons.Person;

public class Edge {
	Person A;
	Person B;
	
	public Edge(Person a, Person b) {
		A=a;
		B=b;
	}
	
	public Person getA() {return A;}
	public Person getB() {return B;}
}
