package fr.dauphine.javaavance.phineloops.model;


/*
 * This class refers to all the parts.
 * */

public class Variable {
	
	private final String name;
	
	public Variable(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(Variable var) {
		return this.name == var.name;
	}
}
