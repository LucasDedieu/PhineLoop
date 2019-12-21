package fr.dauphine.javaavance.phineloops.model;

<<<<<<< HEAD
public class StateLineByLine {
	private int i;
	private int j;
	private int r;
	
	public StateLineByLine(int i, int j, int r) {
		this.i = i;
		this.j = j;
		this.r = r;
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

	public boolean canRotate(Shape shape) {
=======
public class State2 {
	private int i;
	private int j;
	private int r;
	
	public State2(int i, int j, int r) {
		this.i = i;
		this.j = j;
		this.r = r;
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

	public boolean canRotate(Shape shape) {
		if(shape.getType() == 0 || shape.getType() ==4) {
			return false;
		}
>>>>>>> branch 'master' of https://github.com/Dauphine-Java-M1/phineloops-alt.git
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

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//sb.append("grille : \n"+game);
		sb.append("i :"+i+", j :"+j+", r :"+r);
		sb.append("\n_________________\n");
		return sb.toString();
		
	}
	
}
