package fr.dauphine.javaavance.phineloops.model;

import java.util.Arrays;

public class IShape extends Shape {

	public IShape(int orientation, int i, int j) {
		super(ShapeType.IShape, orientation, i, j);
		// TODO Auto-generated constructor stub
		switch(orientation)
		{
		case 0:
			connections = Arrays.asList(Connection.NORTH,Connection.SOUTH);
			break;
		case 1:
			connections = Arrays.asList(Connection.EAST,Connection.WEST);
			break;
		default:
			throw new IllegalArgumentException("0<=orientation<=2");
		}
	}
	
	public void rotate() {
		this.orientation = (this.orientation + 1)%2;
		//NORTH with EAST, EAST with SOUTH, SOUTH with WEST, WEST with NORTH 
		connections.replaceAll(x->x.getNextConnection());
	}

}
