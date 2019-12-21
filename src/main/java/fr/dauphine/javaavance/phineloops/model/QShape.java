package fr.dauphine.javaavance.phineloops.model;

public class QShape extends Shape {
	private int[] domain = {10,11,12,13};
	private int type = 1;

	public QShape(int orientation, int i, int j) {
		super(orientation, i, j);
		// TODO Auto-generated constructor stub
		switch(orientation)
		{
		case 0:
			connections[NORTH]=true; 
			break;
		case 1:
			connections[EAST] = true;
			break;
		case 2:
			connections[SOUTH] = true;
			break;
		case 3:
			connections[WEST] = true;
			break;
		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
	}
	
	@Override
	public void rotate() {
		super.rotate();
		orientation = (orientation + 1)%4;
	}
	
	public int[] getDomain() {
		return domain;
	}
	
	
	public String getSymbol() {
		switch(orientation)
		{
		case 0:
			return "╹";
		case 1:
			return "╺";
		case 2:
			return "╻";
		case 3:
			return "╸";
		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
	}
	public int getMaxRotation() {
		return 3;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
