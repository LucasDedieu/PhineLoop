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
	
	public boolean equals(Object o) {
		if(!(o instanceof Variable)) return false;
		
		Variable var = (Variable)o;
		
		return name == var.name;
	}
}
