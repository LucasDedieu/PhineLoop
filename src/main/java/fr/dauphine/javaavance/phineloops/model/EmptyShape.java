package fr.dauphine.javaavance.phineloops.model;

public class EmptyShape extends Shape {
	
	public EmptyShape(int orientation,int i,int j) {
		super(ShapeType.EmptyShape, orientation,i,j);
		connections = new Connection[]{};
		
	
	}

}
