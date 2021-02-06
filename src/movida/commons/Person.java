/* 
 * Copyright (C) 2020 - Angelo Di Iorio
 * 
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 * 
*/
package movida.commons;

/**
 * Classe usata per rappresentare una persona, attore o regista,
 * nell'applicazione Movida.
 * 
 * Una persona ? identificata in modo univoco dal nome 
 * case-insensitive, senza spazi iniziali e finali, senza spazi doppi. 
 * 
 * Semplificazione: <code>name</code> ? usato per memorizzare il nome completo (nome e cognome)
 * 
 * La classe puï¿½ essere modicata o estesa ma deve implementare il metodo getName().
 * 
 */
public class Person {

	private String name;
	private boolean director; // true: director; false: actor
	
	public Person(String name, boolean d) {
		this.name = name;
		this.director = d;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean isDirector() {
		return this.director;
	}
	
	/**
	 * Redefinizione del metodo equals (e con sé hashCode) affinchè il metodo Map.get(Object key),
	 * utilizzato in NonOrientedGraph, ritorni i valori desiderati (e non null)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Person)) return false;
		if (o == this) return true;
		return  this.name.equals(((Person) o).getName());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
