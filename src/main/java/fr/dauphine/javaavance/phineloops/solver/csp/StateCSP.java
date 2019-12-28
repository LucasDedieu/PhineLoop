package fr.dauphine.javaavance.phineloops.solver.csp;

import java.util.HashSet;
import java.util.Set;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class StateCSP {
	private Shape shape;
	//private Set<Shape> banSet;
	private boolean[] shapeDomain = new boolean[] {true, true, true, true};
	private boolean[] rightDomain;
	private boolean[] leftDomain;
	private boolean[] bottomDomain;
	private boolean[] topDomain;
	private int r;
	
	public StateCSP(Game game, Shape shape, int r) {
		this.shape = shape;
		if(shape!=null) {
			boolean [] array = shape.getPossibleOrientation();
			if(array!=null) {
				shapeDomain = new boolean[array.length];
				System.arraycopy(array, 0, shapeDomain, 0, array.length);
			}
			
		}

		Shape[] neighbors = game.getNeighbors(shape);
		if(neighbors[Game.NORTH] !=null && !neighbors[Game.NORTH].isFrozen() ) {
			boolean[] array =  neighbors[Game.NORTH].getPossibleOrientation();
			topDomain = new boolean[array.length];
			System.arraycopy(array, 0, topDomain, 0, array.length);
		}
		if(neighbors[Game.SOUTH] !=null && !neighbors[Game.SOUTH].isFrozen()) {
			boolean[] array =  neighbors[Game.SOUTH].getPossibleOrientation();
			bottomDomain = new boolean[array.length];
			System.arraycopy(array, 0, bottomDomain, 0, array.length);
		}
		if(neighbors[Game.EAST] !=null && !neighbors[Game.EAST].isFrozen()) {
			boolean[] array =  neighbors[Game.EAST].getPossibleOrientation();
			rightDomain = new boolean[array.length];
			System.arraycopy(array, 0, rightDomain, 0, array.length);
		}
		if(neighbors[Game.WEST] !=null && !neighbors[Game.WEST].isFrozen()) {
			boolean[] array =  neighbors[Game.WEST].getPossibleOrientation();
			leftDomain = new boolean[array.length];
			System.arraycopy(array, 0, leftDomain, 0, array.length);
		}
		
		this.r = r;
	}


	public int getI() {
		return shape.getI();
	}

	public int getJ() {
		return shape.getJ();
	}

	public int getR() {
		return r;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public boolean canRotate() {
		return shape.getDomainSize()!=0 && r <shape.getMaxRotation()+1;
	}
	public void rotate() {
		r++;
		shape.rotate();
	}
	/*
	public void addBan(Shape s) {
		if(banSet == null) {
			banSet = new HashSet<Shape>();
		}
		banSet.add(s);
	}

	public boolean isBan(Shape s) {
		if(banSet == null) {
			return false;
		}
		return banSet.contains(s);
	}*/


	public boolean[] getRightDomain() {
		
		return rightDomain;
	}


	public boolean[] getLeftDomain() {
		return leftDomain;
		
	}


	public boolean[] getBottomDomain() {
		return bottomDomain;
	
	}


	public boolean[] getTopDomain() {
		return topDomain;
		
	}


	public void setRightDomain(boolean[] rightDomain) {
		this.rightDomain = rightDomain;
	}


	public void setLeftDomain(boolean[] leftDomain) {
		this.leftDomain = leftDomain;
	}


	public void setBottomDomain(boolean[] bottomDomain) {
		this.bottomDomain = bottomDomain;
	}


	public void setTopDomain(boolean[] topDomain) {
		this.topDomain = topDomain;
	}


	public boolean[] getShapeDomain() {
		return shapeDomain;
	}

	
	

}
