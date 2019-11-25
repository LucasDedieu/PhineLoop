package fr.dauphine.javaavance.phineloops.model;

import java.util.Random;

public class Generator {
	private Game game;
	
	
	public Generator(Game game) {
		this.game = game;
	}
	
	public Game generate() {
		int h = game.getHeight();
		int w = game.getWidth();
		Shape[][] board = game.getBoard();
		Random randType = new Random();
		for(int i=0; i<h;i++) {
			for(int j =0; j<w;j++) {
				int rand = randType.nextInt(6);
				ShapeType type = ShapeType.valueOf(rand);
				
				board[i][j] = type.buildShape(0,i,j);
			}
		}
		return null;
	}
}
