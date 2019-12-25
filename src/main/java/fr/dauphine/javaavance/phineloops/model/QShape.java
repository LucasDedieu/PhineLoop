package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class QShape extends Shape {

	private int[] domain = {0,1,2,3};

	private int type = 1;
	private int nbConnection = 1;

	public QShape(int orientation, int i, int j) {
		super(orientation, i, j);
		this.possibleOrientation = new boolean[]{true, true, true, true};
		this.domainSize = 4;
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
	
	
	public int[] getDomainWithPruning(Game game) {
		if (super.getI()==0 && super.getJ()==0)
			return new int[]{1,2};
		else if (super.getI()==0 && super.getJ()!=0)
			return new int[]{1,2,3};
		else if (super.getI()==0 && super.getJ()==game.getWidth()-1)
			return new int[]{2,3};
		else if (super.getI()!=0 && super.getJ()==0)
			return new int[]{0,1,2};
		else if (super.getI()!=0 && super.getJ()!=game.getWidth()-1)
			return new int[]{0,2,3};
		else if (super.getI()==game.getHeight()-1 && super.getJ()==0)
			return new int[]{0,1};
		else if (super.getI()==game.getHeight()-1 && super.getJ()!=0)
			return new int[]{0,1,3};
		else if (super.getI()==game.getHeight()-1 && super.getJ()==game.getWidth()-1)
			return new int[]{0,3};
		else 
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
	
	public void setOrientation(int orientation)
	{
		this.orientation=orientation;
		switch(orientation)
		{
		case 0:
			connections[NORTH]=true; 
			connections[SOUTH] = false;
			connections[EAST] = false;
			connections[WEST] = false;
			break;
		case 1:
			connections[NORTH]=false; 
			connections[SOUTH] = false;
			connections[EAST] = true;
			connections[WEST] = false;
			break;
		case 2:
			connections[NORTH]=false; 
			connections[SOUTH] = true;
			connections[EAST] = false;
			connections[WEST] = false;
			break;
		case 3:
			connections[NORTH]=false; 
			connections[SOUTH] = false;
			connections[EAST] = false;
			connections[WEST] = true;
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

	public int getNbConnection() {
		return nbConnection;
	}

	public void setNbConnection(int nbConnection) {
		this.nbConnection = nbConnection;
	}

	@Override
	public void rotateTo(int orientation) {
		connections = new boolean[4];
		this.orientation= orientation;
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
	public int[] getDomain() {
		// TODO Auto-generated method stub
		return null;
	}

}
