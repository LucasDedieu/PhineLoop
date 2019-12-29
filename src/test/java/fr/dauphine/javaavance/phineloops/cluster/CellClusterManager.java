package fr.dauphine.javaavance.phineloops.cluster;

import java.util.HashSet;
import java.util.Set;

public class CellClusterManager {
	
	private Set<CellCluster> clusterSet = new HashSet<>();

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
		Set<CellCluster> acceptSet = new HashSet<>();
		for(CellCluster cellCluster: clusterSet) {
			if (cellCluster.accept(cell)) {
				cellCluster.add(cell);
				acceptSet.add(cellCluster);
			}
		}
		
		if (acceptSet.isEmpty()) {
			CellCluster cellCluster = new CellCluster();
			cellCluster.add(cell);
			clusterSet.add(cellCluster);
		} else {
			mergeClusters(acceptSet);
		}
	}
	
	
	private void mergeClusters(Set<CellCluster> mergeSet) {
		if (mergeSet.size() == 1) {
			return; 
		}
		
		CellCluster kept = mergeSet.iterator().next();
		
		mergeSet.remove(kept);
		
		for(CellCluster c: mergeSet) {
			kept.merge(c);
			clusterSet.remove(c);
		}
	}
	
	public Set<CellCluster> getClusterSet() {
		return clusterSet;
	}

	public CellCluster getCluster(Cell cell) {

		for(CellCluster cellCluster : clusterSet) {
			if (cellCluster.contains(cell)) {
				return cellCluster;
			}
		}
		
		return null;
	}

	public Cell[][] getSubBoard(Cell[][] board, CellCluster c) {

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
