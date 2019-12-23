package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class EmptyShape extends Shape {
	
	private int type = 0;
	private int nbConnection = 0;
	
	public EmptyShape(int orientation,int i,int j) {
		super(orientation,i,j);
		this.possibleOrientation = new boolean[]{true};
	}

	
	
	
	public String getSymbol() {
		return "  ";
	}
	
	public int getMaxRotation() {
		return 0;
	}
	@Override
	public int getType() {
		return type;
	}
	
	@Override
	public void rotate() {
	}

	public int getNbConnection() {
		return nbConnection;
	}

	public void setNbConnection(int nbConnection) {
		this.nbConnection = nbConnection;
	}

	@Override
	public void rotateTo(int orientation) {
		
	}
}
