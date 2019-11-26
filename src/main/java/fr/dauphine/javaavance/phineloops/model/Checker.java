package fr.dauphine.javaavance.phineloops.model;

public class Checker {
	private Game game;
	
	public Checker(Game game) {
		this.game = game;
	}
	
	public boolean check() {
		Shape[][] board = game.getBoard();
		int height = game.getHeight();
		int width = game.getWidth();
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				if(!(game.shapeIsFullyConnected(board[i][j]))){
					return false;
				}
			}
		}
		return true;
 	}

}
