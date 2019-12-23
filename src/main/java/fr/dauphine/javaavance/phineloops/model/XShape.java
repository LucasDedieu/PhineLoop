package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;

public class XShape extends Shape {
	private int[] domain = {40};
	private int type = 4;
	private int nbConnection = 4;
	
	public XShape(int orientation,int i, int j) {
		super(orientation,i,j);
		connections[NORTH]=true; connections[SOUTH] = true; connections[WEST] =true; connections[EAST] =true;
	}
	@Override
	public void rotate() {
	}

	public int[] getDomain() {
		return domain;
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
}
