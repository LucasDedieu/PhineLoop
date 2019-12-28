package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;

public abstract class Shape {
	protected static int NORTH = 0;
	protected static int EAST = 1;
	protected static int SOUTH = 2;
	protected static int WEST = 3;
	
	protected boolean[] connections = new boolean[4];
	protected int orientation;
	private int i;
	private int j;
	private boolean isFrozen;
	public boolean[] possibleOrientation;

	private ArrayList<Shape> reservedBy = new ArrayList<Shape>();

	protected int domainSize;

	
	

	public Shape (int orientation, int i, int j) {
		this.orientation = orientation;
		this.i=i;
		this.j=j;
	}
	
	public Shape(int orientation)
	{
		this.orientation=orientation;
	}
	
	
	/**
	 * Construct the shape object from its string id 
	 * @param line :the id
	 * @param i :the coordinate i of the shape
	 * @param j :the coordinate i of the shape
	 * @return shape if the id is a correct id. Otherwise return null
	 */
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
	
	
	
	public abstract int[] getDomain();
	
	//public abstract void rotate();
	
	public abstract int getMaxRotation();
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * JAVA DOC ALBAN 
	 */
	public  abstract int[] getDomainWithPruning(Game game);
	
	public abstract String getSymbol();
	
	public abstract int getNbConnection();
	
	
	/**
	 * Rotate the shape 90° to the right.
	 * Change the shape connections accordly to the new rotation
	 */
	public void rotate() {
		//NORTH with EAST, EAST with SOUTH, SOUTH with WEST, WEST with NORTH 
		boolean tmp = connections[WEST];
		connections[WEST] = connections[SOUTH];
		connections[SOUTH] = connections[EAST];
		connections[EAST] = connections[NORTH];
		connections[NORTH] = tmp;
	}
	
	
	/**
	 * Rotate to a specific orientation
	 * @param orientation : the orientation
	 */
	public  abstract void rotateTo(int orientation);

	/**
	 * Remove specifics orientations from shape possible orientations
	 * @param orientations : orientations to remove
	 */
	public void removePossibleOrientation(int[] orientations) {
		for(int o : orientations) {
			possibleOrientation[o] = false;
		}
		domainSize=0;
		for(int i=0;i<possibleOrientation.length;i++) {
			if(possibleOrientation[i]) {
				domainSize++;
			}
		}
		
	}
	
	
	
	public boolean[] getPossibleOrientation() {
		return possibleOrientation;
	}
	
	public void setPossibleOrientation(boolean[] d) {

		this.possibleOrientation = new boolean[d.length];
		System.arraycopy(d, 0, possibleOrientation, 0, d.length);
		
		domainSize=0;
		for(int i=0;i<possibleOrientation.length;i++) {
			if(possibleOrientation[i]) {
				domainSize++;
			}
		}
	}
	

	public abstract int getType();
	
	public int getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int orientation) {
	}
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
	
	/**
	 * @param i the i to set
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * @param j the j to set
	 */
	public void setJ(int j) {
		this.j = j;
	}

	public boolean[] getConnections(){
		return connections;
	}
	
	public int getVConnections() {
		int vconnection=0;
		for (boolean bl: connections)
		{
			if(bl==true) vconnection++;
		}
		return vconnection;
	}
	
	
	/**
	 * Return the string id corresponding to the shape
	 */
	public String toString() {
		return getType()+" "+orientation;
	}
	
	/**
	 * Check if shape has a specific connection
	 * @param connection : the connection to check
	 * @return true if shape has this orientation
	 */
	public boolean hasConnection(int connection) {
		if(connections == null) {
			return false;
		}
		return connections[connection];
	}

	
	public boolean isFrozen() {
		return isFrozen;
	}

	public void setFroze(boolean isFrozen) {
		
		this.isFrozen = isFrozen;
	}

	
	public ArrayList<Shape> getReservedBy() {
		return reservedBy;
	}
	
	/**
	 * 
	 * @param reservedBy
	 */
	public void setReservedBy(Shape reservedBy) {
		
		this.reservedBy.add(reservedBy);
		
	}

	public int  getDomainSize() {
		return domainSize;
		
	}


	public boolean isConnectedTo(Shape s) {
		if(i == s.getI()) {
			return Math.abs(j-s.getJ()) == 1;
		}
		if(j == s.getJ()) {
			return Math.abs(i-s.getI()) == 1;
		}
		return false;
	}
	
	


	
	
	
}
