package fr.dauphine.javaavance.phineloops.model;

public class LShape extends Shape {
	private int[] domain = {50,51,52,53};
	private int type = 5;

	public LShape(int orientation, int i, int j) {
		super(orientation, i, j);
		// TODO Auto-generated constructor stub
		switch(orientation)
		{
		case 0:
			connections[NORTH] = true; connections[EAST]=true;
			break;
		case 1:
			connections[SOUTH] = true; connections[EAST]=true;
			break;
		case 2:
			connections[SOUTH] = true; connections[WEST]=true;
			break;
		case 3:
			connections[WEST] = true; connections[NORTH]=true;
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

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}
}
