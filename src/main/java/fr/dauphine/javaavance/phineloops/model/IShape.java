package fr.dauphine.javaavance.phineloops.model;

public class IShape extends Shape {
	private int[] domain = {20,21};
	private int type = 2;
	private int nbConnection = 2;
	
	public IShape(int orientation, int i, int j) {
		super(orientation, i, j);
		// TODO Auto-generated constructor stub
		switch(orientation)
		{
		case 0:
			connections[NORTH]=true; connections[SOUTH] = true;
			break;
		case 1:
			connections[EAST]=true; connections[WEST] = true;
			break;
		default:
			throw new IllegalArgumentException("0<=orientation<=2");
		}
	}
	
	@Override
	public void rotate() {
		super.rotate();
		orientation = (orientation + 1)%2;
	}
	public int[] getDomain() {
		return domain;
	}
	
	
	public String getSymbol() {
		switch(orientation)
		{
		case 0:
			return "│";
		case 1:
			return "─";

		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
	}
	
	public int getMaxRotation() {
		return 1;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public int getNbConnection() {
		return nbConnection;
	}

	public void setNbConnection(int nbConnection) {
		this.nbConnection = nbConnection;
	}

}
