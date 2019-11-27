package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;

public class XShape extends Shape {
	
	public XShape(int orientation,int i, int j) {
		super(ShapeType.XShape, orientation,i,j);
		connections = Arrays.asList(Connection.NORTH,Connection.SOUTH,Connection.EAST,Connection.WEST);
	}
	public void rotate() {
	}

}
