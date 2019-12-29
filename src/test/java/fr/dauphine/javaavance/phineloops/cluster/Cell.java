package fr.dauphine.javaavance.phineloops.cluster;

public class Cell {
	private int i;
	private int j;
	private char c;

	public Cell(int i, int j, char c) {
		this.i = i;
		this.j = j;
		this.c = c;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public char getC() {
		return c;
	}

	public boolean isConnectedTo(Cell cell) {
		
		if (i == cell.getI()) {
			return Math.abs(j - cell.getJ()) == 1;
		}
		
		if (j == cell.getJ()) {
			return Math.abs(i - cell.getI()) == 1;
		}
	
		return false;
	}
	
	
	@Override
	public String toString() {
		return "(" + i + ", " + j + ", " + c + ")";
	}
}
