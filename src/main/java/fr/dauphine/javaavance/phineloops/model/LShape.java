package fr.dauphine.javaavance.phineloops.model;

import java.util.Arrays;

public class LShape extends Shape {

	public LShape(int orientation, int i, int j) {
		super(ShapeType.LShape, orientation, i, j);
		// TODO Auto-generated constructor stub
		switch(orientation)
		{
		case 0:
			connections = Arrays.asList(Connection.NORTH,Connection.EAST);
			break;
		case 1:
			connections = Arrays.asList(Connection.SOUTH,Connection.EAST);
			break;
		case 2:
			connections = Arrays.asList(Connection.SOUTH,Connection.WEST);
			break;
		case 3:
			connections = Arrays.asList(Connection.WEST,Connection.NORTH);
			break;
		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
		
	}
	
	public void rotate() {
		orientation = (orientation + 1)%4;
		//NORTH with EAST, EAST with SOUTH, SOUTH with WEST, WEST with NORTH 
		connections.replaceAll(x->x.getNextConnection());
	}

}
