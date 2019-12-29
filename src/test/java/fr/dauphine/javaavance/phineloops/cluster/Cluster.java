package fr.dauphine.javaavance.phineloops.cluster;

import java.util.HashSet;
import java.util.Set;

public class Cluster {
	private Set<Cell> cellSet = new HashSet<>();
	private boolean computed = false;
	private int minI;
	private int minJ;
	private int maxI;
	private int maxJ;
	
	
	public void add(Cell cell) {
		cellSet.add(cell);
		computed = false;
	}
	public boolean accept(Cell newCell) {
		for(Cell cell: cellSet) {
			if (newCell.isConnectedTo(cell)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<Cell> getCellSet() {
		return cellSet;
	}
	
	public void merge(Cluster cluster) {
		cellSet.addAll(cluster.getCellSet());
	}
	
	
	private void computeLimit() {
		
		minI = Integer.MAX_VALUE;
		minJ = Integer.MAX_VALUE;
		maxI = 0;
		maxJ = 0;
		
		for(Cell cell : cellSet) {
			if (cell.getI() < minI) {
				minI = cell.getI();
			}
			
			if (cell.getJ() < minJ) {
				minJ = cell.getJ();
			}
			
			
			if (cell.getI() > maxI) {
				maxI = cell.getI();
			}
			
			if (cell.getJ() > maxJ) {
				maxJ = cell.getJ();
			}
		}
		
		computed = true;
	}
	
	public boolean contains(Cell cell) {
		return cellSet.contains(cell);
	}
	public int getMinI() {
		if (!computed) {
			computeLimit();
		}
		return minI;
	}
	
	public int getMaxI() {
		if (!computed) {
			computeLimit();
		}
		return maxI;
	}
	public int getMinJ() {
		if (!computed) {
			computeLimit();
		}
		return minJ;
	}
	public int getMaxJ() {
		if (!computed) {
			computeLimit();
		}
		return maxJ;
	}

	
	public int getWidth() {
		return (getMaxJ() - getMinJ()) + 1;
	}
	
	public int getHeight() {
		return (getMaxI() - getMinI()) + 1;
	}

	@Override
	public String toString() {
		return getWidth() + "x" + getHeight() + " ("+ getMinI() + "," + getMinJ()+ ") ("+ getMaxI() + "," + getMaxJ() + ") " + cellSet.size() + " cells" ;
	}

}
