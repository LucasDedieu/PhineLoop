package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class LShape extends Shape {
	
	private int type = 5;
	private int nbConnection = 2;
	
	public LShape(int orientation, int i, int j) {
		super(orientation, i, j);
		this.possibleOrientation = new boolean[]{true, true, true, true};
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
}
