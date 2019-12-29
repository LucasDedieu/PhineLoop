package fr.dauphine.javaavance.phineloops.solver.line;

import fr.dauphine.javaavance.phineloops.model.Shape;

public class StateLineByLine {
	private int i;
	private int j;
	

	private int r;
	
	public StateLineByLine(int i, int j, int r) {
		this.r = r;
		this.i = i;
		this.j = j;
	}


	public int getI() {
		return  i;
	}

	public int getJ() {
		return j;
	}

	public int getR() {
		return r;
	}

	


	public boolean canRotate(Shape shape) {
		return r <shape.getMaxRotation()+1;
	}
	
	public void rotate(Shape shape) {
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
