package fr.dauphine.javaavance.phineloops.model;

public class XShape extends Shape {
	
	public XShape(int orientation,int i, int j) {
		super(ShapeType.XShape, orientation,i,j);
		connections = new Connection[]{Connection.NORTH,Connection.SOUTH,Connection.EAST,Connection.WEST};
		
	
	}

}
