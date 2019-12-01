package fr.dauphine.javaavance.phineloops.model;

import java.util.List;

public abstract class Shape {
	protected List<Connection> connections;
	private ShapeType type;
	protected int orientation;
	private int i;
	private int j;
	
	public Shape (ShapeType type, int orientation, int i, int j) {
		this.type = type;
		this.orientation = orientation;
		this.i=i;
		this.j=j;
	}
	
	public abstract void rotate();
	
	
	public int getType() {
		return type.getId();
	}
	
	
	public int getOrientation() {
		return orientation;
	}
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
	
	public List<Connection> getConnections(){
		return connections;
	}
	
	
	public String toString() {
		return getType()+" "+orientation;
	}
	
	public boolean hasConnection(Connection connection) {
		if(connections == null) {
			return false;
		}
		return connections.contains(connection);
	}
}
