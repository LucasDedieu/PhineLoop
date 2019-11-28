package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 *  The area of each piece is the possible orientation set.
 * */

public class Domain { 
	
	private List<Shape> t_values;
	//private int position;
	
	public Domain(List<Shape> ls) {
		this.t_values = ls;
	}

	
	
	public int size() {
		return t_values.size();
	}
	
	public boolean isEmpty() {
		return t_values.isEmpty();
	}
	
	
	public int getValue(Shape shape){
      return shape.getOrientation();
	}
	
	public String setOfVariables() {
		StringBuilder strbuilder = new StringBuilder();
		strbuilder.append("{");
		
		for(Object t_value : t_values) {
			if(strbuilder.length() > 1) strbuilder.append(",");
			strbuilder.append(t_value);
		}
		
		strbuilder.append("}");
		return strbuilder.toString();
	}
	
}
