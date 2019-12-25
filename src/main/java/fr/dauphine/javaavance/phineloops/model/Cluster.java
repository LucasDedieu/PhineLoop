package fr.dauphine.javaavance.phineloops.model;

import java.util.HashSet;
import java.util.Set;

public class Cluster {
	private Set<Shape> shapeSet = new HashSet<>();
	private boolean computed = false;
	private int minI;
	private int minJ;
	private int maxI;
	private int maxJ;

	public void add(Shape shape) {
		shapeSet.add(shape);
		computed = false;
	}

	public boolean accept(Shape newShape) {
		for(Shape s : shapeSet) {
			if(newShape.isConnectedTo(s)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Shape s) {
		return shapeSet.contains(s);
	}

	public Set<Shape> getShapeSet(){
		return shapeSet;
	}

	public void merge(Cluster c) {
		shapeSet.addAll(c.getShapeSet());
	}

	private void computeLimit() {
		minI = Integer.MAX_VALUE;
		minJ = Integer.MAX_VALUE;
		maxI = 0;
		maxJ = 0;

		for(Shape s : shapeSet) {
			if(s.getI()<minI) {
				minI=s.getI();
			}
			if(s.getJ()<minJ) {
				minJ=s.getJ();
			}
			if(s.getI()>maxI) {
				maxI=s.getI();
			}
			if(s.getJ()>maxJ) {
				maxJ=s.getJ();
			}
		}
		computed = true;
	}

	public int getMinI() {
		if(!computed){
			computeLimit();
		}
		return minI;
	}

	public int getMinJ() {
		if(!computed){
			computeLimit();
		}
		return minJ;
	}

	public int getMaxI() {
		if(!computed){
			computeLimit();
		}
		return maxI;
	}

	public int getMaxJ() {
		if(!computed){
			computeLimit();
		}
		return maxJ;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("min I :"+getMinI()+"   max i :"+getMaxI()+" ");
		sb.append("min J :"+getMinJ()+"   max J :"+getMaxJ()+" ");
		sb.append(shapeSet.size());
		return sb.toString();
	}



}
