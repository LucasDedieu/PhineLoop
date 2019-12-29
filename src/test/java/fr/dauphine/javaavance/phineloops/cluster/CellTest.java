package fr.dauphine.javaavance.phineloops.cluster;

import org.junit.Assert;
import org.junit.Test;


public class CellTest {

	@Test
	public void testIsConnectedTo() {
		
		// Prepare
		int w = 5;
		int h = 5;
		Cell[][] board = new Cell[h][w];
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				char c = (char)('A' + (i * w + j));
				board[i][j] = new Cell(i, j, c);
			}
		}

		// Test
		Assert.assertFalse(board[0][0].isConnectedTo(board[1][1]));
		Assert.assertTrue(board[0][1].isConnectedTo(board[1][1]));
		Assert.assertFalse(board[0][2].isConnectedTo(board[1][1]));

		Assert.assertTrue(board[1][0].isConnectedTo(board[1][1]));
		Assert.assertTrue(board[1][2].isConnectedTo(board[1][1]));

		Assert.assertFalse(board[2][0].isConnectedTo(board[1][1]));
		Assert.assertTrue(board[2][1].isConnectedTo(board[1][1]));
		Assert.assertFalse(board[2][2].isConnectedTo(board[1][1]));

				
		Assert.assertFalse(board[0][0].isConnectedTo(board[2][0]));
		Assert.assertFalse(board[0][0].isConnectedTo(board[0][2]));
		
	}
}