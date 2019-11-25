package fr.dauphine.javaavance.phineloops.model;

public enum ShapeType {

	EmptyShape(0),
	QShape(1),
	IShape(2),
	TShape(3),
	XShape(4),
	LShape(5);
	
	private int id;
	
	private ShapeType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static ShapeType valueOf(int i) {
		return values()[i];
	}
	
	public Shape buildShape(int orientation,int i, int j) {
		switch(this) {
			case EmptyShape: return new EmptyShape(orientation,i,j);
			case QShape: return new TShape(orientation,i,j);
			case IShape: return new TShape(orientation,i,j);
			case TShape: return new TShape(orientation,i,j);
			case XShape: return new XShape(orientation,i,j);
			case LShape: return new TShape(orientation,i,j);
			
		}
		return null;
	}
}
