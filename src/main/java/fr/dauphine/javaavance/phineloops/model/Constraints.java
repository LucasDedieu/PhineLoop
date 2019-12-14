package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.List;

/*
 *  The rooms that are neighbors must connect.
 * */

public class Constraints {
	
	private Variable variable_i;
	private Variable variable_j;
	
	private List<Variable> list_variables;
	
	public Constraints(Variable vi, Variable vj) {
		this.variable_i = vi;
		this.variable_j = vj;
		
		this.list_variables = new ArrayList<Variable>();
		list_variables.add(vi);
		list_variables.add(vj);
	}
	
	public List<Variable> getListVariables(){
		return list_variables;
	}
	
	public boolean neighbors(Variable v1, Variable v2) {
		//.....
		return true;
	}
	
	public boolean isConnected(Variable v1, Variable v2) {
		//....
		
		return true;
	}

}
