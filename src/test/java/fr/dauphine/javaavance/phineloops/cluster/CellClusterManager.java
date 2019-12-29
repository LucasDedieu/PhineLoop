package fr.dauphine.javaavance.phineloops.cluster;

import java.util.HashSet;
import java.util.Set;

public class CellClusterManager {
	
	private Set<Cluster> clusterSet = new HashSet<>();

	private static final CellClusterManager SINGLETON = new CellClusterManager();

	// --------------------------------------------------------
	// Singleton & init
	// --------------------------------------------------------
	private CellClusterManager() {
		// EMPTY
	}

	public static CellClusterManager getInstance() {
		return SINGLETON;
	}

	
	
	public void addCell(Cell cell) {
		Set<Cluster> acceptSet = new HashSet<>();
		for(Cluster cluster: clusterSet) {
			if (cluster.accept(cell)) {
				cluster.add(cell);
				acceptSet.add(cluster);
			}
		}
		
		if (acceptSet.isEmpty()) {
			Cluster cluster = new Cluster();
			cluster.add(cell);
			clusterSet.add(cluster);
		} else {
			mergeClusters(acceptSet);
		}
	}
	
	
	private void mergeClusters(Set<Cluster> mergeSet) {
		if (mergeSet.size() == 1) {
			return; 
		}
		
		Cluster kept = mergeSet.iterator().next();
		
		mergeSet.remove(kept);
		
		for(Cluster c: mergeSet) {
			kept.merge(c);
			clusterSet.remove(c);
		}
	}
	
	public Set<Cluster> getClusterSet() {
		return clusterSet;
	}

	public Cluster getCluster(Cell cell) {

		for(Cluster cluster : clusterSet) {
			if (cluster.contains(cell)) {
				return cluster;
			}
		}
		
		return null;
	}

	public Cell[][] getSubBoard(Cell[][] board, Cluster c) {

		int minI = c.getMinI();
		int minJ = c.getMinJ();
		
		int maxI = c.getMaxI();
		int maxJ = c.getMaxJ();
		
		int h = (maxI - minI) + 1;
		int w = (maxJ - minJ) + 1;
		
		
		Cell[][] subBoard = new Cell[h][w];
		
		for(int i = minI; i <= maxI; i++) {
			for(int j = minJ; j <= maxJ; j++) {
				Cell cell = board[i][j];
				
				subBoard[i-minI][j-minJ] = cell;
			}
		}
 		
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				Cell cell = subBoard[i][j];
				if (!c.contains(cell)) {
					subBoard[i][j] = new Cell(i, j, '.');
				}
			}
	 }
		
		return subBoard;
	}
}
