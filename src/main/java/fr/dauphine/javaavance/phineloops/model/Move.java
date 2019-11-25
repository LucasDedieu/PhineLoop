package fr.dauphine.javaavance.phineloops.model;

public class Move {
	private Shape shape;
	private int rotationCount;
	
	public Move(Shape shape, int rotationCount) {
		this.shape = shape;
		this.rotationCount = rotationCount;
	}
}
