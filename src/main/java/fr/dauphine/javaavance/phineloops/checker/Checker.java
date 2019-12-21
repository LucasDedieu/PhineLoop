package fr.dauphine.javaavance.phineloops.checker;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.solver.line.State;

public class Checker {
	/*
	private Game game;
	
	public Checker(Game game) {
		this.game = game;
	}*/
	
	public static boolean check(Game game) {
		return checkFromI(game, 0);
 	}
	

	public static boolean check(State state) {
		return checkFromI(state, 0);
 	}
	
	public static boolean checkFromI(Game game, int iStart) {
		int height = game.getHeight();
		int width = game.getWidth();
		Shape[][] board = game.getBoard();
		for(int i =iStart;i<height;i++) {
			for(int j=0;j<width;j++) {
				if(!(game.isShapeFullyConnected(board[i][j]))){
					return false;
				}
			}
		}
		return true;
 	}
	
	
	public static boolean checkFromI(State state, int iStart) {
		Game game = state.getGame();
		return checkFromI(game, iStart);
	}
	
	public static boolean checkFromIJ(Game game, int iStart,int jStart) {
		int height = game.getHeight();
		int width = game.getWidth();
		Shape[][] board = game.getBoard();
		for(int i =iStart;i<height;i++) {
			for(int j=jStart;j<width;j++) {
				if(!(game.isShapeFullyConnected(board[i][j]))){
					return false;
				}
			}
		}
		return true;
 	}
}
