package fr.dauphine.javaavance.phineloops.model;

import java.util.Arrays;

public class TShape extends Shape {
	
	public TShape(int orientation,int i, int j) {
		super(ShapeType.TShape, orientation,i,j);
		switch(orientation)
		{
		case 0:
			connections = Arrays.asList(Connection.NORTH,Connection.EAST,Connection.WEST);
			break;
		case 1:
			connections = Arrays.asList(Connection.NORTH,Connection.SOUTH,Connection.EAST);
			break;
		case 2:
			connections = Arrays.asList(Connection.EAST,Connection.SOUTH,Connection.WEST);
			break;
		case 3:
			connections = Arrays.asList(Connection.WEST,Connection.NORTH,Connection.SOUTH);
			break;
		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
	
	}

}
