package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class IShape extends Shape {
	
	private int type = 2;
	private int nbConnection = 2;
	
	public IShape(int orientation, int i, int j) {
		super(orientation, i, j);
		this.possibleOrientation = new boolean[]{true, true};
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

	@Override
	public void rotateTo(int orientation) {
		connections = new boolean[4];
		this.orientation= orientation;
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

}
