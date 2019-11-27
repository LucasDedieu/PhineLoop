package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;

public class EmptyShape extends Shape {
	
	public EmptyShape(int orientation,int i,int j) {
		super(ShapeType.EmptyShape, orientation,i,j);
		connections = new ArrayList<>();	
	}

}
