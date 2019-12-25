package fr.dauphine.javaavance.phineloops.solver.line;

import fr.dauphine.javaavance.phineloops.model.Shape;

public class StateLineByLine {
	//private int i;
//	private int j;
	private Shape shape;
	private int r;
	
	public StateLineByLine(Shape shape, int r) {
		this.shape=shape;
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

	


	public boolean canRotate() {
		return r <shape.getMaxRotation()+1;
	}
	
	public void rotate() {
		r++;
		shape.rotate();
	}
	

	public void setR(int r) {
		this.r=r;
		
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//sb.append("grille : \n"+game);
		sb.append("i :"+getI()+", j :"+getJ()+", r :"+r);
		sb.append("\n_________________\n");
		return sb.toString();
		
	}


	
}
