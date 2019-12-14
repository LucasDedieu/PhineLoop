package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private HashMap<Variable, Integer> keyValues;
	
	public CSP() {
		this.variables = new ArrayList<>();
		this.domains = new ArrayList<>();
		this.constraints = new ArrayList<>();
		this.keyValues = new HashMap<>();
	}
	
	public List<Variable> getVariables(){
		return Collections.unmodifiableList(variables);
	}
	
	public List<Constraints> getConstraints(){
		return constraints;
	}
	
	public List<Domain> getDomain(){
		return domains;
	}

	
	public List<Variable> getScope(){ // List of variables satisfying the constraints
		return null;
	}
	
	public void addNewVariables(Variable newName) {//This function adds a new variable.
		boolean variableKey = keyValues.containsKey(newName);
		if(!variableKey) {
			variables.add(newName);
			int newSize = variables.size() - 1;
			keyValues.put(newName, newSize);
		}else {
			try {
				throw new Exception("This name already exists !");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static <T,U> void courseMap(Map<T,U> map) {//Showing the elements of a HashMap.
		Set<Map.Entry<T,U> > inputs = map.entrySet();
		Iterator<Map.Entry<T,U> > iter = inputs.iterator();
		
		while(iter.hasNext()) {
			Map.Entry<T, U> input = iter.next();
			System.out.println(input.getKey()+" : "+input.getValue());
		}
	}
}
