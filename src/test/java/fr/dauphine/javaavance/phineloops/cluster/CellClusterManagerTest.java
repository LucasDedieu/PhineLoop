package fr.dauphine.javaavance.phineloops.cluster;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class CellClusterManagerTest {

	@Test
	public void testAddCell() {
				
		// Prepare
		String boardStr = 
				"x-x-x" +
				"xx-xx" + 
				"-xxx-" + 
				"x----" +
				"xx--x" +
				"-x--x";

		Cell[][] board = generateBoard(5, 6, boardStr);
		//     0 1 2 3 4
		//     =========
		// 0)  x - x - x
		// 1)  x x - x x
		// 2)  - x x x -
		// 3)  x - - - - 
		// 4)  x x - - x
		// 5)  - x - - x

		CellClusterManager mgr = CellClusterManager.getInstance();
		Set<CellCluster> clusterSet = mgr.getClusterSet();
		

		// Add cells, test cluster count
		mgr.addCell(board[0][0]);
		Assert.assertEquals(1, clusterSet.size());

		mgr.addCell(board[0][2]);
		Assert.assertEquals(2, clusterSet.size());

		
		mgr.addCell(board[0][4]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[1][0]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[1][1]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[1][3]);
		Assert.assertEquals(4, clusterSet.size());

		mgr.addCell(board[1][4]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[2][1]);
		Assert.assertEquals(3, clusterSet.size());

		mgr.addCell(board[2][2]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[2][3]);
		Assert.assertEquals(2, clusterSet.size());
		
		mgr.addCell(board[3][0]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[4][0]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[4][1]);
		Assert.assertEquals(3, clusterSet.size());
		
		mgr.addCell(board[4][4]);
		Assert.assertEquals(4, clusterSet.size());

		mgr.addCell(board[5][1]);
		Assert.assertEquals(4, clusterSet.size());
		
		mgr.addCell(board[5][4]);
		Assert.assertEquals(4, clusterSet.size());
		
		// Test c1
		CellCluster c1 = mgr.getCluster(board[0][0]);
		Assert.assertEquals(9, c1.getCellSet().size());
		Assert.assertEquals(0, c1.getMinI());
		Assert.assertEquals(2, c1.getMaxI());
		Assert.assertEquals(0, c1.getMinJ());
		Assert.assertEquals(4, c1.getMaxJ());		
		Assert.assertEquals(5, c1.getWidth());
		Assert.assertEquals(3, c1.getHeight());
		Assert.assertEquals(
				"x...x" +
			  "xx.xx" + 
				".xxx.", 
				getBoardString(mgr.getSubBoard(board, c1)));
		
		// Test c2
		CellCluster c2 = mgr.getCluster(board[0][2]);
		Assert.assertEquals(1, c2.getCellSet().size());
		Assert.assertEquals(0, c2.getMinI());
		Assert.assertEquals(0, c2.getMaxI());
		Assert.assertEquals(2, c2.getMinJ());
		Assert.assertEquals(2, c2.getMaxJ());
		Assert.assertEquals(1, c2.getWidth());
		Assert.assertEquals(1, c2.getHeight());
		Assert.assertEquals(
				"x", 
				getBoardString(mgr.getSubBoard(board, c2)));
		
		// Test c3
		CellCluster c3 = mgr.getCluster(board[3][0]);
		Assert.assertEquals(4, c3.getCellSet().size());
		Assert.assertEquals(3, c3.getMinI());
		Assert.assertEquals(5, c3.getMaxI());
		Assert.assertEquals(0, c3.getMinJ());
		Assert.assertEquals(1, c3.getMaxJ());
		Assert.assertEquals(2, c3.getWidth());
		Assert.assertEquals(3, c3.getHeight());
		Assert.assertEquals(
				"x." +
				"xx" + 
				".x", 
				getBoardString(mgr.getSubBoard(board, c3)));		
		
		
		// Test c4
		CellCluster c4 = mgr.getCluster(board[4][4]);
		Assert.assertEquals(2, c4.getCellSet().size());
		Assert.assertEquals(4, c4.getMinI());
		Assert.assertEquals(5, c4.getMaxI());
		Assert.assertEquals(4, c4.getMinJ());
		Assert.assertEquals(4, c4.getMaxJ());
		Assert.assertEquals(1, c4.getWidth());
		Assert.assertEquals(2, c4.getHeight());
		Assert.assertEquals(
				"x" +
		    "x", 
				getBoardString(mgr.getSubBoard(board, c4)));			
	}
	
	private Cell[][] generateBoard(int w, int h, String boardStr) {
		Cell[][] board = new Cell[h][w];
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				int pos = i * w + j;
				char c = boardStr.charAt(pos);
				board[i][j] = new Cell(i, j, c);
			}
		}
		
		return board;
	}
	
	private String getBoardString(Cell[][] board) {
		StringBuilder sb = new StringBuilder();
		
		
		int h = board.length;
		int w = board[0].length;
		
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				sb.append(board[i][j].getC());
			}
		}
		return sb.toString();
	}
	
}
