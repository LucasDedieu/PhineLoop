package fr.dauphine.javaavance.phineloops.model;

import java.util.Arrays;

public class TShape extends Shape {
	private int[] domain = {30,31,32,33};
	private int type = 3;
	
	public TShape(int orientation,int i, int j) {
		super(orientation,i,j);
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
	
	public int getMaxRotation() {
		return 3;
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
			return "┻";
		case 1:
			return "┣";
		case 2:
			return "┳";
		case 3:
			return "┫";
		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
