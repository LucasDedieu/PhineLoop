package fr.dauphine.javaavance.phineloops.model;

import java.util.Arrays;

public class TShape extends Shape {
	
	public TShape(int orientation,int i, int j) {
		super(ShapeType.TShape, orientation,i,j);
		//TODO switch case en fonction orientation
		connections = Arrays.asList(Connection.NORTH,Connection.EAST,Connection.WEST);
		
	
	}

}
