package fr.dauphine.javaavance.phineloops.model;

public class TShape extends Shape {
	private int[] domain = {30,31,32,33};
	private int type = 3;
	
	public TShape(int orientation,int i, int j) {
		super(orientation,i,j);
		switch(orientation)
		{
		case 0:
			connections[NORTH]=true; connections[WEST] = true; connections[EAST] =true;
			break;
		case 1:
			connections[NORTH]=true; connections[SOUTH] = true; connections[EAST] =true;
			break;
		case 2:
			connections[WEST]=true; connections[SOUTH] = true; connections[EAST] =true;
			break;
		case 3:
			connections[NORTH]=true; connections[SOUTH] = true; connections[WEST] =true;
			break;
		default:
			throw new IllegalArgumentException("0<=orientation<=3");
		}
	
	}
	
	public int getMaxRotation() {
		return 3;
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
