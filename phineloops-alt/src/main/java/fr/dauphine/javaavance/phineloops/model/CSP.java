package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

}
