package fr.dauphine.javaavance.phineloops.model;

public class TShape extends Shape {
	
	public TShape(int orientation,int i, int j) {
		super(ShapeType.TShape, orientation,i,j);
		//TODO switch case en fonction orientation
		connections = new Connection[]{Connection.NORTH,Connection.EAST,Connection.WEST};
		
	
	}

}
