package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;

public class EmptyShape extends Shape {
	private int[] domain = {0};
	private int type = 0;
;	
	public EmptyShape(int orientation,int i,int j) {
		super(orientation,i,j);
		connections = new ArrayList<>();	
	}
	public void rotate() {
	}
	public int[] getDomain() {
		return domain;
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
}
