package fr.dauphine.javaavance.phineloops.model;

import java.util.Arrays;

public class LShape extends Shape {
	private int[] domain = {50,51,52,53};

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
	public int[] getDomain() {
		return domain;
	}

	
	public String getSymbol() {
		switch(orientation)
		{
		case 0:
			return "╰";
		case 1:
			return "╭";
		case 2:
			return "╮";
		case 3:
			return "╯";
		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
	}
	
	public int getMaxRotation() {
		return 3;
	}
}
