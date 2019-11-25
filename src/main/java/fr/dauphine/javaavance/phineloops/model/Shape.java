package fr.dauphine.javaavance.phineloops.model;

public abstract class Shape {
	protected Connection[] connections;
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
	
	public void rotate() {
		orientation = (orientation + 1)%4;
	}
	
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


	
	
	public String toString() {
		return getType()+" "+orientation;
	}
}
