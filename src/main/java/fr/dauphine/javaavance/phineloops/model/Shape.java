package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Shape {
	//protected List<Connection> connections;
	//private ShapeType shapeType;
	protected static int NORTH = 0;
	protected static int EAST = 1;
	protected static int SOUTH = 2;
	protected static int WEST = 3;
	
	protected boolean[] connections = new boolean[4];
	protected int orientation;
	private int i;
	private int j;
	private boolean isFroze;
	public boolean[] possibleOrientation;
	
	

	
	public Shape (/*ShapeType type,*/ int orientation, int i, int j) {
		//this.shapeType = type;
		this.orientation = orientation;
		this.i=i;
		this.j=j;
	}
	
	public static Shape getShapeFromStringId(String line, int i, int j) {
		switch(line) {
			case"0 0": return new EmptyShape(0,i,j);
			case"1 0": return new QShape(0,i,j);
			case"1 1": return new QShape(1,i,j);
			case"1 2": return new QShape(2,i,j);
			case"1 3": return new QShape(3,i,j);
			case"2 0": return new IShape(0,i,j);
			case"2 1": return new IShape(1,i,j);
			case"3 0": return new TShape(0,i,j);
			case"3 1": return new TShape(1,i,j);
			case"3 2": return new TShape(2,i,j);
			case"3 3": return new TShape(3,i,j);
			case"4 0": return new XShape(0,i,j);
			case"5 0": return new LShape(0,i,j);
			case"5 1": return new LShape(1,i,j);
			case"5 2": return new LShape(2,i,j);
			case"5 3": return new LShape(3,i,j);
		}
		return null;
	}
	
	
	
	
	
	//public abstract void rotate();
	
	public abstract int getMaxRotation();
	
	
	
	public abstract String getSymbol();
	
	public abstract int getNbConnection();
	
	public void rotate() {
		//NORTH with EAST, EAST with SOUTH, SOUTH with WEST, WEST with NORTH 
		boolean tmp = connections[WEST];
		connections[WEST] = connections[SOUTH];
		connections[SOUTH] = connections[EAST];
		connections[EAST] = connections[NORTH];
		connections[NORTH] = tmp;
	}
	
	/*
	public void rotateTo(int orientation) {
		while(this.orientation != orientation) {
			rotate();
		}	
	}*/
	public  abstract void rotateTo(int orientation);

	public void removePossibleOrientation(int[] orientations) {
		for(int o : orientations) {
			possibleOrientation[o] = false;
		}
	}
	
	public boolean[] getPossibleOrientation() {
		return possibleOrientation;
	}
	
	/*
	public int getType() {
		return shapeType.getId();
	}
	
	public ShapeType getShapeType() {
		return shapeType;
	}*/
	public abstract int getType();
	
	public int getOrientation() {
		return orientation;
	}
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
	
	public boolean[] getConnections(){
		return connections;
	}
	
	
	public String toString() {
		return getType()+" "+orientation;
	}
	
	public boolean hasConnection(int connection) {
		if(connections == null) {
			return false;
		}
		return connections[connection];
	}

	public boolean isFroze() {
		return isFroze;
	}

	public void setFroze(boolean isFixed) {
		this.isFroze = isFixed;
	}
	
	
}
