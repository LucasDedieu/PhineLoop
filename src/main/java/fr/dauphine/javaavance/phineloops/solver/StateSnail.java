package fr.dauphine.javaavance.phineloops.solver;

import fr.dauphine.javaavance.phineloops.model.Shape;

public class StateSnail {
	Direction dir;
	private int i;
	private int j;
	private int r;
	private int level = 0;
	int nb;
	
	public StateSnail(Direction dir, int level, int i, int j, int r,int nb) {

		this.dir = dir;
		this.i = i;
		this.j = j;
		this.r = r;
		this.level = level;
		this.nb = nb;
	}


	public Direction getDir() {
		return dir;
	}
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public int getR() {
		return r;
	}
	
	public int getNb() {
		return nb;
	}

	public boolean canRotate(Shape shape) {
		return r <shape.getMaxRotation()+1;
	}
	
	public void rotate(Shape shape) {
		r++;
		shape.rotate();
	}

	public void setJ(int j) {
		this.j=j;
		
	}

	public void setI(int i) {
		this.i=i;
	}
	
	public void setR(int r) {
		this.r=r;
		
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//sb.append("grille : \n"+game);
		sb.append("i :"+i+", j :"+j+", r :"+r);
		sb.append("\n_________________\n");
		return sb.toString();
		
	}
	
}
