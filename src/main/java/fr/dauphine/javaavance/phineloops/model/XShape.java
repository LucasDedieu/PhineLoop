package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class XShape extends Shape {

	private int type = 4;
	private int nbConnection = 4;
	
	public XShape(int orientation,int i, int j) {
		super(orientation,i,j);
		this.possibleOrientation = new boolean[]{true};
		connections[NORTH]=true; connections[SOUTH] = true; connections[WEST] =true; connections[EAST] =true;
	}
	@Override
	public void rotate() {
	}


	
	public String getSymbol() {
		return "╋";
	}
	
	public int getMaxRotation() {
		return 0;
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
		// TODO Auto-generated method stub
		
	}
}
