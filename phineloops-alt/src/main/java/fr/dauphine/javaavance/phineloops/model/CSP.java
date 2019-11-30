package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * In this constraint satisfaction algorithm (CSP), we need:
   a) A set of variables v = {x1, x2, ..., Xn}
       Each variable Xi has a domain Di of the possible values.
   b) A finite set of constraints C1, ...., Cm on the variables.
   c) Each node is defined by an assignment of values ​​{Xi = Vi, Xj = Vj, ....}
 * */
public class CSP {
	
	private List<Variable> variables;
	private List<Domain> domains;
	private List<Constraints> constraints;
	
	
	public CSP() {
		this.variables = new ArrayList<>();
		this.domains = new ArrayList<>();
		this.constraints = new ArrayList<>();
	}
	
	public List<Variable> my_variables(){
		return Collections.unmodifiableList(variables);
	}
	
	public List<Constraints> my_constraints(){
		return constraints;
	}
	
	public List<Domain> my_domains(){
		return domains;
	}

	
	public List<Variable> getScope(){ // List of variables satisfying the constraints
		return null;
	}
}
