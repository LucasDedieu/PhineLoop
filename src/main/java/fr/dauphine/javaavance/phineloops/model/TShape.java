package fr.dauphine.javaavance.phineloops.model;

import java.util.Arrays;

public class TShape extends Shape {
	private int[] domain = {0,1,2,3};
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
	
	public int[] getDomainWithPruning(Game game) {
		if (super.getI()==0 && super.getJ()==0)
			return null;
		else if (super.getI()==0 && super.getJ()!=0)
			return new int[]{2};
		else if (super.getI()==0 && super.getJ()==game.getWidth()-1)
			return null;
		else if (super.getI()!=0 && super.getJ()==0)
			return new int[]{1};
		else if (super.getI()!=0 && super.getJ()!=game.getWidth()-1)
			return new int[]{3};
		else if (super.getI()==game.getHeight()-1 && super.getJ()==0)
			return null;
		else if (super.getI()==game.getHeight()-1 && super.getJ()!=0)
			return new int[]{0};
		else if (super.getI()==game.getHeight()-1 && super.getJ()==game.getWidth()-1)
			return null;
		else 
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
	
	public void setOrientation(int orientation)
	{
		this.orientation=orientation;
		switch(orientation)
		{
		case 0:
			connections[NORTH]=true; connections[WEST] = true; connections[EAST] =true; connections[SOUTH] = false;
			break;
		case 1:
			connections[NORTH]=true; connections[SOUTH] = true; connections[EAST] =true; connections[WEST] = false;
			break;
		case 2:
			connections[WEST]=true; connections[SOUTH] = true; connections[EAST] =true; connections[NORTH] = false;
			break;
		case 3:
			connections[NORTH]=true; connections[SOUTH] = true; connections[WEST] =true; connections[EAST] = false;
			break;
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
